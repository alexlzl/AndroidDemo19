package com.liuyang.share.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.down.DefaultImageDownLoader;
import com.liuyang.share.down.ImageDownLoader;
import com.liuyang.share.exception.ImageDisposeException;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareImage;
import com.liuyang.share.params.ShareParamAudio;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareParamWebPage;
import com.liuyang.share.utils.BitmapUtil;
import com.liuyang.share.utils.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * 图片处理帮助工具
 */
public class ShareImageHelper {
    private static final int THUMB_RESOLUTION_SIZE = 150;
    private static final int THUMB_MAX_SIZE = 30 * 1024;
    private static final int BITMAP_SAVE_THRESHOLD = 32 * 1024;

    private Context mContext;
    private ShareResultCallBack mShareResultCallBack;
    private ShareBuilder mShareBuilder;
    private SocializeMedia mSocializeMedia;

    public ShareImageHelper(Context context,ShareBuilder shareBuilder,SocializeMedia socializeMedia) {
        mContext = context.getApplicationContext();
        this.mSocializeMedia = socializeMedia;
        this.mShareBuilder = shareBuilder;
    }

    public void setShareResultCallBack(ShareResultCallBack shareResultCallBack){
        this.mShareResultCallBack = shareResultCallBack;
    }
    /**
     * 如果Bitmap/Res体积太大，保存到本地
     */
    public ShareImage saveBitmapToExternalIfNeed(BaseShareParam params) {
        return saveBitmapToExternalIfNeed(getShareImage(params));
    }

    public ShareImage saveBitmapToExternalIfNeed(ShareImage image) {
        if (image == null) {
            return null;
        } else if (image.isBitmapImage()) {
            if (image.getBitmap().getByteCount() > BITMAP_SAVE_THRESHOLD) {
                if (checkImageCachePath()) {
                    File file = BitmapUtil.saveBitmapToExternal(image.getBitmap(), mShareBuilder.getImageCachePath());
                    if (file != null && file.exists()) {
                        image.setLocalFile(file);
                    }
                }
            }
        } else if (image.isResImage()) {
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), image.getResId());
            if (bmp.getByteCount() > BITMAP_SAVE_THRESHOLD) {
                if (checkImageCachePath()) {
                    File file = BitmapUtil.saveBitmapToExternal(bmp,mShareBuilder.getImageCachePath());
                    if (file != null && file.exists()) {
                        image.setLocalFile(file);
                        bmp.recycle();
                    }
                }
            }
        }
        return image;
    }

    public void copyImageToCacheFileDirIfNeed(BaseShareParam params) {
        copyImageToCacheFileDirIfNeed(getShareImage(params));
    }

    public void copyImageToCacheFileDirIfNeed(ShareImage shareImage) {
        if (shareImage == null) {
            return;
        }

        File localFile = shareImage.getLocalFile();
        if (localFile == null || !localFile.exists()) {
            return;
        }

        if (!checkImageCachePath()) {
            return;
        }

        String localFilePath = localFile.getAbsolutePath();
        if (!localFilePath.startsWith(mContext.getCacheDir().getParentFile().getAbsolutePath())
                && localFilePath.startsWith(mShareBuilder.getImageCachePath())) {
            return;
        }

        File targetFile = copyFile(localFile, mShareBuilder.getImageCachePath());
        if (targetFile != null && targetFile.exists()) {
            shareImage.setLocalFile(targetFile);
        }
    }

    private File copyFile(File srcFile, String targetCacheDirPath) {
        if (srcFile == null || !srcFile.exists()) {
            return null;
        }

        File targetFileDir = new File(targetCacheDirPath);
        File targetFile = new File(targetFileDir, srcFile.getName());

        if (!targetFileDir.exists() && !targetFileDir.mkdirs()) {
            return null;
        }

        try {
            FileUtil.copyFile(srcFile, targetFile);
            return targetFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 缩略图。32kb限制。
     * 注意：在工作线程调用。
     *
     * @param image
     * @return
     */
    public byte[] buildThumbData(final ShareImage image) {
        return buildThumbData(image, THUMB_MAX_SIZE, THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE, false);
    }

    public byte[] buildThumbData(final ShareImage image, int maxSize, int widthMax, int heightMax, boolean isFixSize) {
        if (image == null) {
            return new byte[0];
        }

        boolean isRecycleSrcBitmap = true;
        Bitmap bmp = null;

        if (image.isNetImage()) {
            if (mShareResultCallBack != null) {
                mShareResultCallBack.onDealing(mSocializeMedia);
            }
            bmp = BitmapUtil.decodeUrl(image.getNetImageUrl());
        } else if (image.isLocalImage()) {
            bmp = BitmapUtil.decodeFile(image.getLocalPath(), THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE);
        } else if (image.isResImage()) {
            bmp = BitmapFactory.decodeResource(mContext.getResources(), image.getResId());
        } else if (image.isBitmapImage()) {
            if (mShareResultCallBack != null) {
                mShareResultCallBack.onDealing(mSocializeMedia);
            }
            isRecycleSrcBitmap = false;
            bmp = image.getBitmap();
        }

        if (bmp != null && !bmp.isRecycled()) {
            if (!isFixSize) {
                int bmpWidth = bmp.getWidth();
                int bmpHeight = bmp.getHeight();
                double scale = BitmapUtil.getScale(widthMax, heightMax, bmpWidth, bmpHeight);
                widthMax = (int) (bmpWidth / scale);
                heightMax = (int) (bmpHeight / scale);
            }

            final Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, widthMax, heightMax, true);
            if (isRecycleSrcBitmap && thumbBmp != bmp) {
                bmp.recycle();
            }
            byte[] tempArr = BitmapUtil.bmpToByteArray(thumbBmp, maxSize, true);
            return tempArr == null ? new byte[0] : tempArr;
        }

        return new byte[0];
    }

    public void downloadImageIfNeed(final BaseShareParam params, final Runnable task) throws ShareException {
        downloadImageIfNeed(getShareImage(params), task);
    }

    /**
     * @param image
     * @param task  图片下载完成后待执行的任务
     * @throws ShareException
     */
    public void downloadImageIfNeed(final ShareImage image, final Runnable task) throws ShareException {
        if (image == null || !image.isNetImage()) {
            task.run();
        } else {
            if (!checkImageCachePath()) {
                if (mShareResultCallBack != null) {
                    mShareResultCallBack.onError(mSocializeMedia,new ImageDisposeException(mContext.getString(R.string.share_sdk_compress_image_failed)));
                }
                return;
            }

            new DefaultImageDownLoader().download(mContext, image.getNetImageUrl(), mShareBuilder.getImageCachePath(),
                    new ImageDownLoader.OnImageDownloadListener() {
                        @Override
                        public void onStart() {
                            if (mShareResultCallBack != null) {
                                mShareResultCallBack.onDealing(mSocializeMedia);
                            }
                        }

                        @Override
                        public void onSuccess(String filePath) {
                            image.setLocalFile(new File(filePath));
                            copyImageToCacheFileDirIfNeed(image);
                            task.run();
                        }

                        @Override
                        public void onFailed(String url) {
                            if (mShareResultCallBack != null) {
                                mShareResultCallBack.onError(mSocializeMedia,new ImageDisposeException(mContext.getString(R.string.share_sdk_compress_image_failed)));
                            }
                        }
                    });
        }
    }

    private boolean checkImageCachePath() {
        if (TextUtils.isEmpty(mShareBuilder.getImageCachePath())) {
            Toast.makeText(mContext.getApplicationContext(), "存储设备不可用", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    protected ShareImage getShareImage(BaseShareParam params) {
        ShareImage image = null;
        if (params == null || params instanceof ShareParamText) {
            return null;
        } else if (params instanceof ShareParamImage) {
            image = ((ShareParamImage) params).getImage();
        } else if (params instanceof ShareParamWebPage) {
            image = ((ShareParamWebPage) params).getThumb();
        } else if (params instanceof ShareParamAudio) {
            image = ((ShareParamAudio) params).getThumb();
        } else if (params instanceof ShareParamVideo) {
            image = ((ShareParamVideo) params).getThumb();
        }
        return image;
    }

}
