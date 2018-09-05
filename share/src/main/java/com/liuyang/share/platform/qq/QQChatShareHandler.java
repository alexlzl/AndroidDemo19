
package com.liuyang.share.platform.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.liuyang.share.ShareBuilder;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.InvalidParamException;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareImage;
import com.liuyang.share.params.ShareParamAudio;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareParamWebPage;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;


public class QQChatShareHandler extends BaseQQShareHandler {

    public QQChatShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context, shareBuilder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
            //防止收到两次回调
//            if (resultCode == Constants.ACTIVITY_OK) {
//                Tencent.handleResultData(data, mUiListener);
//            }
        }
    }

    @Override
    protected void shareText(ShareParamText params) throws ShareException {
        shareImageText(params, null);
    }

    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        final ShareImage image = params.getImage();
        if (image == null || (!image.isLocalImage() && !image.isNetImage())) {
            shareImageText(params, params.getImage());
        } else {
            shareImage(params, params.getImage());
        }
    }

    @Override
    protected void shareWebPage(ShareParamWebPage params) throws ShareException {
        shareImageText(params, params.getThumb());
    }

    @Override
    protected void shareAudio(ShareParamAudio params) throws ShareException {
        if (TextUtils.isEmpty(params.getTitle()) || TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Title or target url is empty or illegal");
        }
        if (TextUtils.isEmpty(params.getAudioUrl())) {
            throw new InvalidParamException("Audio url is empty or illegal");
        }

        final Bundle bundle = new Bundle();
        ShareImage thumb = params.getThumb();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, params.getTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, params.getContent());
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.getTargetUrl());

        if (thumb != null) {
            if (thumb.isNetImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumb.getNetImageUrl());
            } else if (thumb.isLocalImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, thumb.getLocalPath());
            }
        }

        bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, params.getAudioUrl());
        doShareToQQ((Activity)getContext(), bundle);
    }

    @Override
    protected void shareVideo(ShareParamVideo params) throws ShareException {
        shareImageText(params, params.getThumb());
    }

    /**
     * 图文模式，title、targetURL不能为空
     *
     * @param params
     * @param image
     * @throws ShareException
     */
    private void shareImageText(BaseShareParam params, ShareImage image) throws ShareException {
        if (TextUtils.isEmpty(params.getTitle()) || TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Title or target url is empty or illegal");
        }

        final Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, params.getTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, params.getContent());
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.getTargetUrl());

        if (image != null) {
            if (image.isNetImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, image.getNetImageUrl());
            } else if (image.isLocalImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, image.getLocalPath());
            }
        }

        doShareToQQ((Activity)getContext(), bundle);
    }

    /**
     * 纯图模式，localPath不能为空
     *
     * @param params
     * @param image
     */
    private void shareImage(BaseShareParam params, final ShareImage image) throws ShareException {
        mImageHelper.downloadImageIfNeed(image, new Runnable() {
            @Override
            public void run() {
                final Bundle bundle = new Bundle();
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);

                if (image.isLocalImage()) {
                    bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, image.getLocalPath());
                }

                doShareToQQ((Activity)getContext(), bundle);
            }
        });
    }

    @Override
    protected void onShare(Activity activity, Tencent tencent, Bundle params, IUiListener iUiListener) {
        tencent.shareToQQ(activity, params, iUiListener);
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.QQ;
    }
}
