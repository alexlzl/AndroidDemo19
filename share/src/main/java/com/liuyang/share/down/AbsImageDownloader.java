package com.liuyang.share.down;

import android.content.Context;
import android.text.TextUtils;

import com.liuyang.share.exception.ShareException;

import java.io.File;

/**
 * 抽象的图片下载器
 */
public abstract class AbsImageDownloader implements ImageDownLoader {

    @Override
    public final void download(Context context, String imageUrl, String targetFileDirPath, OnImageDownloadListener listener) throws ShareException {
        if (TextUtils.isEmpty(imageUrl)) {
            if (listener != null) {
                listener.onFailed(imageUrl);
            }
        } else {
            String filePath = createFileIfNeed(context, imageUrl, targetFileDirPath);
            if (TextUtils.isEmpty(filePath)) {
                if (listener != null) {
                    listener.onFailed(imageUrl);
                }
                return;
            }

            File targetFile = new File(filePath);
            if (targetFile.exists()) {
                if (listener != null) {
                    listener.onSuccess(targetFile.getAbsolutePath());
                }
                return;
            }

            downloadDirectly(imageUrl, filePath, listener);
        }
    }

    protected abstract void downloadDirectly(String imageUrl, String filePath, OnImageDownloadListener listener);

    protected String createFileIfNeed(Context context, String imageUrl, String targetFileDirPath) throws ShareException {
        if (context == null || TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(targetFileDirPath)) {
            return null;
        }

        File targetFileDir = new File(targetFileDirPath);
        String fileName = String.valueOf(imageUrl.hashCode());
        File targetFile = new File(targetFileDir, fileName);
        if (targetFile.exists()) {
            return targetFile.getAbsolutePath();
        }

        if (!targetFileDir.exists() && !targetFileDir.mkdirs()) {
            return null;
        }

        return targetFile.getAbsolutePath();
    }

}
