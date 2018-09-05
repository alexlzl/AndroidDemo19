package com.liuyang.share.down;

import android.content.Context;

import com.liuyang.share.exception.ShareException;

/**
 * 图片加载监听接口
 */
public interface ImageDownLoader {

    void download(Context context, String imageUrl, String targetFileDirPath, OnImageDownloadListener listener) throws ShareException;

    interface OnImageDownloadListener {

        void onStart();

        void onSuccess(String filePath);

        void onFailed(String url);

    }
}
