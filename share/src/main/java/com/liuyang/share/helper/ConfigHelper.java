package com.liuyang.share.helper;

import com.liuyang.share.R;
import com.liuyang.share.ShareConstants;
import com.liuyang.share.ShareData;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.model.ShareTarget;

/**
 * 配置中心
 */
public class ConfigHelper {


    public static ShareTarget[] getShareTargets(ShareData shareInfoParams) {
        ShareTarget[] shareTargets = new ShareTarget[shareInfoParams.getPlatform().length];
        for (int i = 0; i < shareTargets.length; i++) {
            if (ShareConstants.QZONE.equals(shareInfoParams.getPlatform()[i])) {
                shareTargets[i] = new ShareTarget(SocializeMedia.QZONE, R.string.share_socialize_text_qq_zone_key, R.drawable.share_socialize_qzone_on);
            } else if (ShareConstants.QQ.equals(shareInfoParams.getPlatform()[i])) {
                shareTargets[i] = new ShareTarget(SocializeMedia.QQ, R.string.share_socialize_text_qq_key, R.drawable.share_socialize_qq_on);
            }
            if (ShareConstants.WEI_CHAT.equals(shareInfoParams.getPlatform()[i])) {
                shareTargets[i] = new ShareTarget(SocializeMedia.WEIXIN, R.string.share_socialize_text_weixin_key, R.drawable.share_socialize_wechat);
            }
            if (ShareConstants.WE_CHAT_MOMENTS.equals(shareInfoParams.getPlatform()[i])) {
                shareTargets[i] = new ShareTarget(SocializeMedia.WEIXIN_MONMENT, R.string.share_socialize_text_weixin_circle_key, R.drawable.share_socialize_wxcircle);
            }
            if (ShareConstants.WEIBO.equals(shareInfoParams.getPlatform()[i])) {
                shareTargets[i] = new ShareTarget(SocializeMedia.SINA, R.string.share_socialize_text_sina_key, R.drawable.share_socialize_sina_on);
            }
        }

        return shareTargets;
    }

}
