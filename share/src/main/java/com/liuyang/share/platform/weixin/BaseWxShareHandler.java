package com.liuyang.share.platform.weixin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.widget.Toast;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareConstants;
import com.liuyang.share.exception.InvalidParamException;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.exception.UninstalledAPPException;
import com.liuyang.share.params.ShareImage;
import com.liuyang.share.params.ShareParamAudio;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareParamWebPage;
import com.liuyang.share.params.ShareVideo;
import com.liuyang.share.platform.base.BaseShareHandler;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 基础微信分享
 */
public abstract class BaseWxShareHandler extends BaseShareHandler {

    public static final String ACTION_RESULT = "com.liuyang.share.wx.result";
    public static final String ACTION_LOGIN_RESULT = "com.liuyang.login.wx.result";
    public static final String BUNDLE_STATUS_CODE = "respErrorCode";
    public static final String BUNDLE_SENDAUTHCODE = "sendAuthCode";

    protected static final int IMAGE_MAX = 32 * 1024;
    protected static final int IMAGE_WIDTH = 600;
    protected static final int IMAGE_HEIGHT = 800;

    public IWXAPI mIWXAPI;

    public BaseWxShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
        try {
            IntentFilter filter = new IntentFilter(ACTION_RESULT);
            context.registerReceiver(mResultReceiver, filter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init() throws ShareException {
        mIWXAPI = WXAPIFactory.createWXAPI(getContext(),getShareBuilder().getWxAppId(), true);
        if (mIWXAPI.isWXAppInstalled()) {
            mIWXAPI.registerApp(getShareBuilder().getWxAppId());
        }
        if (!mIWXAPI.isWXAppInstalled()) {
            String msg = getContext().getString(R.string.share_sdk_not_install_wechat);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            throw new UninstalledAPPException(msg);
        }
    }

    @Override
    protected void shareText(final ShareParamText params) throws ShareException {
        String text = params.getContent();
        if (TextUtils.isEmpty(text)) {
            throw new InvalidParamException("Content is empty or illegal");
        }

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("textshare");
        req.message = msg;
        req.scene = getShareType();
        shareOnMainThread(req);
    }

    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                WXImageObject imgObj = buildWXImageObject(params.getImage());

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;
                msg.thumbData = mImageHelper.buildThumbData(params.getImage());

                final SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("imgshareappdata");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    protected WXImageObject buildWXImageObject(final ShareImage image) {
        WXImageObject imgObj = new WXImageObject();

        if (image == null) {
            return imgObj;
        }

        if (image.isLocalImage()) {
            imgObj.setImagePath(image.getLocalPath());
        } else if (!image.isUnknowImage()) {
            imgObj.imageData = mImageHelper.buildThumbData(image, IMAGE_MAX, IMAGE_WIDTH, IMAGE_HEIGHT, false);
        }

        return imgObj;
    }

    @Override
    protected void shareWebPage(final ShareParamWebPage params) throws ShareException {
        if (TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Target url is empty or illegal");
        }

        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {

                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = params.getTargetUrl();

                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = params.getTitle();
                msg.description = params.getContent();
                msg.thumbData = mImageHelper.buildThumbData(params.getThumb());

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    @Override
    protected void shareAudio(final ShareParamAudio params) throws ShareException {
        if (TextUtils.isEmpty(params.getTargetUrl()) && TextUtils.isEmpty(params.getAudioUrl())) {
            throw new InvalidParamException("Target url or audio url is empty or illegal");
        }

        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                WXMusicObject audio = new WXMusicObject();

                if (!TextUtils.isEmpty(params.getAudioUrl())) {
                    audio.musicUrl = params.getAudioUrl();
                } else {
                    audio.musicUrl = params.getTargetUrl();
                }

                WXMediaMessage msg = new WXMediaMessage(audio);
                msg.title = params.getTitle();
                msg.description = params.getContent();
                msg.thumbData = mImageHelper.buildThumbData(params.getThumb());

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("music");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    @Override
    protected void shareVideo(final ShareParamVideo params) throws ShareException {
        if (TextUtils.isEmpty(params.getTargetUrl()) && (params.getVideo() == null || TextUtils.isEmpty(params.getVideo().getVideoH5Url()))) {
            throw new InvalidParamException("Target url or video url is empty or illegal");
        }

        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                WXVideoObject video = new WXVideoObject();
                ShareVideo sv = params.getVideo();
                if (!TextUtils.isEmpty(sv.getVideoH5Url())) {
                    video.videoUrl = sv.getVideoH5Url();
                } else {
                    video.videoUrl = params.getTargetUrl();
                }

                WXMediaMessage msg = new WXMediaMessage(video);
                msg.title = params.getTitle();
                msg.description = params.getContent();
                msg.thumbData = mImageHelper.buildThumbData(params.getThumb());

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("video");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    protected String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void shareOnMainThread(final SendMessageToWX.Req req) {
        doOnMainThread(new Runnable() {
            @Override
            public void run() {
                boolean result = mIWXAPI.sendReq(req);
                if (!result && getShareResultCallBack() != null) {
                    getShareResultCallBack().onError(getShareMedia(),new ShareException(getContext().getString(R.string.share_sdk_weixin_result_fail)));
                }
            }
        });
    }

    public void release() {
        try {
            if (getContext()!= null) {
                getContext().unregisterReceiver(mResultReceiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            int code = intent.getIntExtra(BUNDLE_STATUS_CODE, -1);
            if (code == ShareConstants.ST_CODE_SUCCESSED) {
                if(getShareResultCallBack()!=null){
                    getShareResultCallBack().onSuccess(getShareMedia());
                }
            } else if (code == ShareConstants.ST_CODE_ERROR) {
                if(getShareResultCallBack()!=null){
                    getShareResultCallBack().onError(getShareMedia(), new ShareException(getContext().getString(R.string.share_sdk_weixin_share_fail)));
                }
            } else if (code == ShareConstants.ST_CODE_ERROR_CANCEL) {
                if(getShareResultCallBack()!=null){
                    getShareResultCallBack().onCancel(getShareMedia());
                }
            }
            release();//释放结果监听广播，防止重复监听广播
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //nothing to do
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    abstract int getShareType();

}
