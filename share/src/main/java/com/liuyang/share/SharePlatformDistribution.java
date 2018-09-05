package com.liuyang.share;

import android.app.Activity;

import com.liuyang.share.exception.ShareException;
import com.liuyang.share.platform.base.ShareHandler;
import com.liuyang.share.platform.generic.CopyShareHandler;
import com.liuyang.share.platform.generic.GenericShareHandler;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.platform.qq.QQChatShareHandler;
import com.liuyang.share.platform.qq.QQZoneShareHandler;
import com.liuyang.share.platform.weibo.SinaShareTransitHandler;
import com.liuyang.share.platform.weixin.WxChatShareHandler;
import com.liuyang.share.platform.weixin.WxMomentShareHandler;


/**
 * 分享平台分发控制
 * Created by liuyang-ds on 2016/11/28.
 */
public class SharePlatformDistribution {

    private ShareBuilder mShareBuilder;
    private ShareHandler mShareHandler;

    public SharePlatformDistribution(ShareBuilder shareBuilder) {
        this.mShareBuilder = shareBuilder;
    }

    public void share(Activity context, SocializeMedia type, BaseShareParam content, ShareResultCallBack shareListener) {
        switch (type) {
            case WEIXIN:
                mShareHandler = new WxChatShareHandler(context,mShareBuilder);
                break;

            case WEIXIN_MONMENT:
                mShareHandler = new WxMomentShareHandler(context,mShareBuilder);
                break;

            case QQ:
                mShareHandler = new QQChatShareHandler(context,mShareBuilder);
                break;

            case QZONE:
                mShareHandler = new QQZoneShareHandler(context,mShareBuilder);
                break;

            case SINA:
                mShareHandler = new SinaShareTransitHandler(context,mShareBuilder);
                break;

            case COPY:
                mShareHandler = new CopyShareHandler(context,mShareBuilder);
                break;
            default:
                mShareHandler = new GenericShareHandler(context,mShareBuilder);
        }
        try {
            mShareHandler.share(content, shareListener);
        } catch (ShareException e) {
            if (shareListener != null) {
                shareListener.onError(type, e);
            }

        }
    }

    public ShareHandler getmShareHandler() {
        return mShareHandler;
    }
}
