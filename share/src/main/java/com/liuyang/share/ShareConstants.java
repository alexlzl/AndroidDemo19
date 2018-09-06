package com.liuyang.share;

/**
 * 分享常量
 */
public class ShareConstants {
    public static final String QQ_APPID = "100825615";
    public static final String QQ_SCOPE = "all";
    public static final String WECHAT_APPID = "wx28f63a05c1ee1424";
    public static final String WECHAT_APPSECRET = "abf27897df4c5c9ad61cdf80c956495a";
    public static final String SINA_APPKEY = "686166542";
    public static final String DEFAULT_REDIRECT_URL = "http://www.gome.com.cn";
    public static final String DEFAULT_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    public static final String  QZONE="QZone";
    public static final String  QQ="QQ";
    public static final String  WEI_CHAT="Wechat";
    public static final String  WE_CHAT_MOMENTS="WechatMoments";
    public static final String  WEIBO="WeiBo";
    public static final String  COPY_LINK="CopyLink";
    public static final String  SHARE_IMAGE="share_image";
    public static final String  SHARE_TEXT="share_text";
    public static final String  SHARE_VIDEO="share_video";
    public static final int ST_CODE_SUCCESSED = 200;
    public static final int ST_CODE_ERROR_CANCEL = 201;
    public static final int ST_CODE_ERROR = 202;
    public static final int ST_CODE_SHARE_ERROR_NOT_CONFIG = -233;//没有配置appkey, appId
    public static final int ST_CODE_SHARE_ERROR_NOT_INSTALL = -234;//第三方软件未安装
    public static final int ST_CODE_SHARE_ERROR_PARAM_INVALID = -235;//ShareParam参数不正确
    public static final int ST_CODE_SHARE_ERROR_PARAM_UNSUPPORTED = -241;//上下文类型和需求不负
    public static final int ST_CODE_SHARE_ERROR_IMAGE = -242;//图片处理异常
    public static final int ST_CODE_SHARE_ERROR_THREAD = -243;//工作线程异常
}
