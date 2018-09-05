package com.liuyang.share.platform.weixin;

import android.app.Activity;

import com.liuyang.share.ShareBuilder;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamWebPage;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 微信朋友圈分享
 */
public class WxMomentShareHandler extends BaseWxShareHandler {

    public WxMomentShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        if (params.getImage() != null && (!params.getImage().isUnknowImage())) {
            super.shareImage(params);
        } else {
            ShareParamWebPage webpage = new ShareParamWebPage(params.getTitle(), params.getContent(), params.getTargetUrl());
            webpage.setThumb(params.getImage());
            shareWebPage(webpage);
        }
    }

    @Override
    int getShareType() {
        return SendMessageToWX.Req.WXSceneTimeline;
    }


    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.WEIXIN_MONMENT;
    }
}

