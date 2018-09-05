package com.liuyang.share.platform.weixin;

import android.app.Activity;

import com.liuyang.share.ShareBuilder;
import com.liuyang.share.SocializeMedia;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 微信分享
 */
public class WxChatShareHandler extends BaseWxShareHandler {

    public WxChatShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    @Override
    int getShareType() {
        return SendMessageToWX.Req.WXSceneSession;
    }


    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.WEIXIN;
    }
}
