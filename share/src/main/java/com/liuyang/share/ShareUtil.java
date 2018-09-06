package com.liuyang.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareImage;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareVideo;

import java.io.File;

/**
 * @author lzl
 * @ describe   分享入口
 * @ time 2018/9/4 18:09
 */
public class ShareUtil {
    private static volatile ShareUtil mShareUtil;
    private Context mContext;
    private final static Object object = new Object();

    private ShareUtil() {

    }

    public static ShareUtil getShareUtil() {
        if (mShareUtil == null) {
            synchronized (object) {
                if (mShareUtil == null) {
                    mShareUtil = new ShareUtil();
                }

            }
        }
        return mShareUtil;
    }

    public void share(ShareHelper shareHelper, ShareData shareInfoParams, Context context) {
        if (shareInfoParams != null && shareInfoParams.getPlatform().length > 0) {
            mContext = context.getApplicationContext();
            shareHelper.doShare(getShareDataType(shareInfoParams));
        } else {
            Toast.makeText(context, "分享异常", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * @ describe  初始化分享数据类型
     * @author lzl
     * @ time 2018/9/5 11:09
     * @ param
     * @ return
     */
    private BaseShareParam getShareDataType(ShareData shareInfo) {
        BaseShareParam param = null;

        if (ShareConstants.SHARE_IMAGE.equals(shareInfo.getShareType())) {
            /**
             * 图片类型分享
             */
            param = new ShareParamImage(shareInfo.getTitle(), shareInfo.getShareDesc(), shareInfo.getShareUrl());
            ((ShareParamImage) param).setImage(generateUrlImage(shareInfo.getShareImageUrl()));
        } else if (ShareConstants.SHARE_TEXT.equals(shareInfo.getShareType())) {
            /**
             * 文本类型分享
             */
            param = new ShareParamText(shareInfo.getTitle(), shareInfo.getShareDesc(), shareInfo.getShareUrl());
        } else if (ShareConstants.SHARE_VIDEO.equals(shareInfo.getShareType())) {
            /**
             * 视频类型分享
             */
            param = new ShareParamVideo(shareInfo.getTitle(), shareInfo.getShareDesc(), shareInfo.getShareUrl());
            ((ShareParamVideo) param).setVideo(getShareVideo(shareInfo));
        } else {
            param = new ShareParamImage(shareInfo.getTitle(), shareInfo.getShareDesc(), shareInfo.getShareUrl());
            ((ShareParamImage) param).setImage(generateUrlImage(shareInfo.getShareImageUrl()));
        }
        return param;

    }

    private ShareVideo getShareVideo(ShareData shareData) {
        ShareVideo shareVideo = new ShareVideo();
        ShareImage shareImage = new ShareImage(shareData.getShareImageUrl());
        shareVideo.setThumb(shareImage);
        shareVideo.setDescription(shareData.getShareDesc());
        shareVideo.setVideoSrcUrl(shareData.getShareUrl());
        return shareVideo;
    }

    /**
     * @ describe 通过资源ID获取ShareImage
     * @author lzl
     * @ time 2018/9/5 11:26
     * @ param
     * @ return
     */
    private ShareImage generateResImage(int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resourceId, options);
        return new ShareImage(bitmap);
    }

    /**
     * @ describe 通过URL获取ShareImage
     * @author lzl
     * @ time 2018/9/5 11:26
     * @ param
     * @ return
     */
    private ShareImage generateUrlImage(String url) {
        return new ShareImage(url);
    }

    /**
     * @ describe 通过文件获取ShareImage
     * @author lzl
     * @ time 2018/9/5 11:26
     * @ param
     * @ return
     */
    private ShareImage generateFileImage(File file) {
        return new ShareImage(file);
    }


}
