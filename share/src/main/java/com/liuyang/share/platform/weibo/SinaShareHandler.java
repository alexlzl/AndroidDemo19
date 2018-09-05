package com.liuyang.share.platform.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.platform.base.BaseShareHandler;
import com.liuyang.share.exception.InvalidParamException;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.exception.UnSupportedException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareAudio;
import com.liuyang.share.params.ShareImage;
import com.liuyang.share.params.ShareParamAudio;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareParamWebPage;
import com.liuyang.share.params.ShareVideo;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

import java.io.File;

/**
 * 新浪微博分享
 */
public class SinaShareHandler extends BaseShareHandler {

    public IWeiboShareAPI mWeiboShareAPI;
    public SsoHandler mSsoHandler;
    public WeiboMultiMessage mWeiboMessage;

    public SinaShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    /**
     * 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
     * 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
     * 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
     * 失败返回 false，不调用上述回调
     *
     * @param activity
     * @param savedInstanceState
     */
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (savedInstanceState != null && mWeiboShareAPI != null) {
            mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), (IWeiboHandler.Response) activity);
        }
    }

    /**
     * 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
     * 来接收微博客户端返回的数据；执行成功，返回 true，并调用
     * {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
     *
     * @param intent
     */
    public void onActivityNewIntent(Activity activity, Intent intent) {
        if (mWeiboShareAPI != null)
            try {
                mWeiboShareAPI.handleWeiboResponse(intent, (IWeiboHandler.Response) activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null && TextUtils.isEmpty(getToken())) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void init() throws ShareException {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getContext(), getShareBuilder().getSinaAppKey());
        mWeiboShareAPI.registerApp();
    }

    @Override
    protected void shareText(final ShareParamText params) throws ShareException {
        checkContent(params);
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj(params);
        allInOneShare(weiboMessage);
    }

    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        checkContent(params);
        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.textObject = getTextObj(params);
                weiboMessage.imageObject = getImageObj(params.getImage());
                allInOneShare(weiboMessage);
            }
        });
    }

    @Override
    protected void shareWebPage(final ShareParamWebPage params) throws ShareException {
        checkContent(params);
        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                if (!isSinaClientInstalled()) {
                    weiboMessage.textObject = getTextObj(params);
                }
                try {
                    checkImage(params.getThumb());
                    weiboMessage.imageObject = getImageObj(params.getThumb());
                } catch (Exception e) {
                    weiboMessage.textObject = getTextObj(params);
                }
                weiboMessage.mediaObject = getWebPageObj(params);
                allInOneShare(weiboMessage);
            }
        });
    }

    @Override
    protected void shareAudio(final ShareParamAudio params) throws ShareException {
        checkContent(params);
        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                if (!isSinaClientInstalled()) {
                    weiboMessage.textObject = getTextObj(params);
                }
                try {
                    checkImage(params.getThumb());
                    weiboMessage.imageObject = getImageObj(params.getThumb());
                } catch (Exception e) {
                    weiboMessage.textObject = getTextObj(params);
                }
                weiboMessage.mediaObject = getAudioObj(params);
                allInOneShare(weiboMessage);
            }
        });
    }

    @Override
    protected void shareVideo(final ShareParamVideo params) throws ShareException {
        checkContent(params);
        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                if (!isSinaClientInstalled()) {
                    weiboMessage.textObject = getTextObj(params);
                }
                try {
                    checkImage(params.getThumb());
                    weiboMessage.imageObject = getImageObj(params.getThumb());
                } catch (Exception e) {
                    weiboMessage.textObject = getTextObj(params);
                }
                weiboMessage.mediaObject = getVideoObj(params);
                allInOneShare(weiboMessage);
            }
        });
    }

    private TextObject getTextObj(BaseShareParam params) {
        TextObject textObject = new TextObject();

        if (params != null) {
            textObject.text = params.getContent();
        }

        return textObject;
    }

    private ImageObject getImageObj(ShareImage image) {
        ImageObject imageObject = new ImageObject();
        if (image == null) {
            return imageObject;
        }
        if (image.isLocalImage()) {
            imageObject.imagePath = image.getLocalPath();
        } else {
            imageObject.imageData = mImageHelper.buildThumbData(image);
        }
        return imageObject;
    }

    private WebpageObject getWebPageObj(ShareParamWebPage params) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = params.getContent();
        mediaObject.description = params.getTitle();
        byte[] thumbData = mImageHelper.buildThumbData(params.getThumb());
        if (thumbData == null || thumbData.length == 0) {
            mediaObject.thumbData = mImageHelper.buildThumbData(new ShareImage(getShareBuilder().getDefaultShareImage()));
        } else {
            mediaObject.thumbData = thumbData;
        }
        mediaObject.actionUrl = params.getTargetUrl();
        mediaObject.defaultText = "分享测试";
        return mediaObject;
    }


    private MusicObject getAudioObj(ShareParamAudio params) {
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = params.getContent();
        musicObject.description = params.getTitle();
        byte[] thumbData = mImageHelper.buildThumbData(params.getThumb());
        if (thumbData == null || thumbData.length == 0) {
            musicObject.thumbData = mImageHelper.buildThumbData(new ShareImage(getShareBuilder().getDefaultShareImage()));
        } else {
            musicObject.thumbData = thumbData;
        }
        musicObject.actionUrl = params.getTargetUrl();
        ShareAudio audio = params.getAudio();
        if (audio != null) {
            musicObject.dataUrl = audio.getAudioSrcUrl();
            musicObject.dataHdUrl = audio.getAudioSrcUrl();
            musicObject.h5Url = audio.getAudioH5Url();
            musicObject.duration = 10;
            musicObject.defaultText = audio.getDescription();
        }
        return musicObject;
    }

    private VideoObject getVideoObj(ShareParamVideo param) {
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = param.getContent();
        videoObject.description = param.getTitle();
        byte[] thumbData = mImageHelper.buildThumbData(param.getThumb());
        if (thumbData == null || thumbData.length == 0) {
            videoObject.thumbData = mImageHelper.buildThumbData(new ShareImage(getShareBuilder().getDefaultShareImage()));
        } else {
            videoObject.thumbData = thumbData;
        }
        videoObject.actionUrl = param.getTargetUrl();
        ShareVideo video = param.getVideo();
        if (video != null) {
            videoObject.dataUrl = video.getVideoSrcUrl();
            videoObject.dataHdUrl = video.getVideoSrcUrl();
            videoObject.h5Url = video.getVideoH5Url();
            videoObject.duration = 10;
            videoObject.defaultText = video.getDescription();
        }
        return videoObject;
    }

    private void allInOneShare(WeiboMultiMessage weiboMessage) {
        final SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        final String token = getToken();
        final AuthInfo mAuthInfo = new AuthInfo(getContext(), getShareBuilder().getSinaAppKey(), getShareBuilder().getSinaRedirectUrl(), getShareBuilder().getSinaScope());
        if (TextUtils.isEmpty(token)) {
            mSsoHandler = new SsoHandler((Activity) getContext(), mAuthInfo);
            mSsoHandler.authorize(mAuthListener);
            mWeiboMessage = weiboMessage;
        } else {
            mWeiboMessage = null;
            mSsoHandler = null;
            doOnMainThread(new Runnable() {
                @Override
                public void run() {
                    boolean result = mWeiboShareAPI.sendRequest((Activity) getContext(), request, mAuthInfo, token, mAuthListener);
                    if (!result) {
                        if (getShareResultCallBack() != null) {
                            getShareResultCallBack().onError(getShareMedia(), new ShareException(getContext().getString(R.string.share_sdk_sina_result_fail)));
                        }
                    }
                }
            });
        }

    }

    private WeiboAuthListener mAuthListener = new WeiboAuthListener() {

        @Override
        public void onWeiboException(WeiboException arg0) {
            if (getShareResultCallBack() != null) {
                getShareResultCallBack().onError(getShareMedia(),  new ShareException(arg0.getMessage()));
            }
        }

        @Override
        public void onComplete(Bundle bundle) {
            Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (newToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(getContext(), newToken);
                if (mWeiboMessage != null) {
                    allInOneShare(mWeiboMessage);
                }
                return;
            }
            if (getShareResultCallBack() == null) {
                return;
            }
            getShareResultCallBack().onError(getShareMedia(), new ShareException(getContext().getString(R.string.share_sdk_sina_token_invalid)));
        }

        @Override
        public void onCancel() {
            if (getShareResultCallBack() != null) {
                getShareResultCallBack().onCancel(getShareMedia());
            }
        }
    };

    public void onResponse(BaseResponse baseResp) {
        if (getShareResultCallBack() == null) {
            return;
        }
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                getShareResultCallBack().onSuccess(getShareMedia());
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                getShareResultCallBack().onCancel(getShareMedia());
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                getShareResultCallBack().onError(getShareMedia(), new ShareException(baseResp.errMsg));
                break;
        }
    }

    private String getToken() {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
        String token = null;
        if (mAccessToken != null) {
            token = mAccessToken.getToken();
        }
        return token;
    }

    private boolean isSinaClientInstalled() {
        return mWeiboShareAPI != null && mWeiboShareAPI.isWeiboAppInstalled();
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.SINA;
    }

    private void checkContent(BaseShareParam params) throws ShareException {
        if(params==null){
            throw new InvalidParamException("share params is null");
        }
        if(params instanceof ShareParamText){
            if (TextUtils.isEmpty(params.getContent())) {
                throw new InvalidParamException("Content is empty or illegal");
            }
        }else if(params instanceof ShareParamImage){
            if (TextUtils.isEmpty(params.getContent())) {
                throw new InvalidParamException("Content is empty or illegal");
            }
            ShareImage image = ((ShareParamImage)params).getImage();
            checkImage(image);
        }else if(params instanceof ShareParamWebPage){
            if (TextUtils.isEmpty(params.getContent())) {
                throw new InvalidParamException("Content is empty or illegal");
            }
            if (TextUtils.isEmpty(params.getTargetUrl())) {
                throw new InvalidParamException("Target url is empty or illegal");
            }
        }else if(params instanceof ShareParamAudio){
            if (TextUtils.isEmpty(params.getContent())) {
                throw new InvalidParamException("Content is empty or illegal");
            }
            if (TextUtils.isEmpty(params.getTargetUrl())) {
                throw new InvalidParamException("Target url is empty or illegal");
            }
            if (((ShareParamAudio)params).getAudio() == null) {
                throw new InvalidParamException("Audio is empty or illegal");
            }

        }else if(params instanceof ShareParamVideo){
            if (TextUtils.isEmpty(params.getContent())) {
                throw new InvalidParamException("Content is empty or illegal");
            }
            if (TextUtils.isEmpty(params.getTargetUrl())) {
                throw new InvalidParamException("Target url is empty or illegal");
            }
            if (((ShareParamVideo)params).getVideo() == null) {
                throw new InvalidParamException("Video is empty or illegal");
            }
        }

    }

    private void checkImage(ShareImage image) throws ShareException {
        if (image == null) {
            throw new InvalidParamException("Image cannot be null");
        }
        if (image.isLocalImage()) {
            if (TextUtils.isEmpty(image.getLocalPath()) || !new File(image.getLocalPath()).exists()) {
                throw new InvalidParamException("Image path is empty or illegal");
            }
        } else if (image.isNetImage()) {
            if (TextUtils.isEmpty(image.getNetImageUrl())) {
                throw new InvalidParamException("Image url is empty or illegal");
            }
        } else if (image.isResImage())
            throw new UnSupportedException("Unsupport image type");
        else if (image.isBitmapImage()) {
            if (image.getBitmap().isRecycled()) {
                throw new InvalidParamException("Cannot share recycled bitmap.");
            }
        } else
            throw new UnSupportedException("Invaild image");
    }

}
