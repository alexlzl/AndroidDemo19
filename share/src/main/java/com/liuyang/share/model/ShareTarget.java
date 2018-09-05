package com.liuyang.share.model;


import com.liuyang.share.SocializeMedia;

/**
 * 分享item实例
 * Created by liuyang-ds on 2016/11/29.
 */
public class ShareTarget {
    public int titleId;
    public int iconId;
    public SocializeMedia media;

    public ShareTarget(SocializeMedia media, int titleId, int iconId) {
        this.media = media;
        this.iconId = iconId;
        this.titleId = titleId;
    }
}
