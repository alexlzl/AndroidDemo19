package com.liuyang.share;

import java.util.Arrays;

/**
 * @author lzl
 * @ describe  传入的分享数据对应实体
 * @ time 2018/9/4 12:01
 */
public class ShareInfoParams {
    /**
     * 分享的平台支持的类型
     */
    private String[] platform;
    /**
     * 必传参数， 分享的标题
     */
    private String title;
    /**
     * 必传参数， 分享的描述
     */
    private String shareDesc;
    /**
     * 必传参数， 分享的图片链接
     */
    private String shareImageUrl;
    /**
     * 必传参数， 分享url链接
     */
    private String shareUrl;
    /**
     * 分享数据类型
     */
    private String shareType;


    public ShareInfoParams() {
    }

    public String[] getPlatform() {
        return platform;
    }

    public void setPlatform(String[] platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    @Override
    public String toString() {
        return "ShareInfoParams{" +
                "platform=" + Arrays.toString(platform) +
                ", title='" + title + '\'' +
                ", shareDesc='" + shareDesc + '\'' +
                ", shareImageUrl='" + shareImageUrl + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareType='" + shareType + '\'' +
                '}';
    }
}
