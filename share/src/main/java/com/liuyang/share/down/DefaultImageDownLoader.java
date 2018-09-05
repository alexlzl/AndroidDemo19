package com.liuyang.share.down;


/**
 * 默认图片下载器
 * Created by liuyang-ds on 2016/11/28.
 */
public class DefaultImageDownLoader extends AbsImageDownloader {


    @Override
    protected void downloadDirectly(String imageUrl, String filePath, OnImageDownloadListener listener) {
        new DefaultImageDownLoadTask(imageUrl, filePath, listener).start();
    }
}
