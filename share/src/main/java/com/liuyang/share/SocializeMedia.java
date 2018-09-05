
package com.liuyang.share;

/**
 * 平台枚举
 */
public enum SocializeMedia {

    GENERIC("generic"),
    SINA("sina"),
    QZONE("qzone"),
    QQ("qq"),
    WEIXIN("weixin"),
    WEIXIN_MONMENT("weixin_moment"),
    COPY("copy");

    private String mName;

    SocializeMedia(String name) {
        this.mName = name;
    }

    public String toString() {
        return mName;
    }

}