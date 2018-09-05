package com.liuyang.share.helper;

import com.liuyang.share.R;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.model.ShareTarget;

/**
 * 配置中心
 */
public class ConfigHelper {

    public static ShareTarget[] getShareTargets() {
        ShareTarget[] shareTargets = {
                new ShareTarget(SocializeMedia.SINA, R.string.share_socialize_text_sina_key, R.drawable.share_socialize_sina_on),
                new ShareTarget(SocializeMedia.WEIXIN, R.string.share_socialize_text_weixin_key, R.drawable.share_socialize_wechat),
                new ShareTarget(SocializeMedia.WEIXIN_MONMENT, R.string.share_socialize_text_weixin_circle_key, R.drawable.share_socialize_wxcircle),
                new ShareTarget(SocializeMedia.QQ, R.string.share_socialize_text_qq_key, R.drawable.share_socialize_qq_on),
                new ShareTarget(SocializeMedia.QZONE, R.string.share_socialize_text_qq_zone_key, R.drawable.share_socialize_qzone_on),
                new ShareTarget(SocializeMedia.GENERIC, R.string.share_sdk_others, R.drawable.share_socialize_sms_on),
                new ShareTarget(SocializeMedia.COPY, R.string.share_socialize_text_copy_url, R.drawable.share_socialize_copy_url)

        };
        return shareTargets;
    }

}
