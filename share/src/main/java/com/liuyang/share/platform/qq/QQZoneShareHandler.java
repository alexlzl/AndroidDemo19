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
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * 只支持图文模式
 * 官方提示:QZone接口暂不支持发送多张图片的能力，若传入多张图片，则会自动选入第一张图片作为预览图。多图的能力将会在以后支持。
 */
public class QQZoneShareHandler extends BaseQQShareHandler {

    public QQZoneShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, mUiListener);
            }
        }
    }

    @Override
    protected void shareText(ShareParamText params) throws ShareException {
        shareImageText(params, null);
    }

    @Override
    protected void shareImage(ShareParamImage params) throws ShareException {
        shareImageText(params, params.getImage());
    }

    @Override
    protected void shareWebPage(ShareParamWebPage params) throws ShareException {
        shareImageText(params, params.getThumb());
    }

    @Override
    protected void shareAudio(ShareParamAudio params) throws ShareException {
        shareImageText(params, params.getThumb());
    }

    @Override
    protected void shareVideo(ShareParamVideo params) throws ShareException {
        shareImageText(params, params.getThumb());
    }

    private void shareImageText(BaseShareParam params, ShareImage image) throws ShareException {
        if (TextUtils.isEmpty(params.getTitle()) || TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Title or target url is empty or illegal");
        }

        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, params.getTitle());
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, params.getContent());
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, params.getTargetUrl());

        ArrayList<String> imageUrls = new ArrayList<>();
        if (image != null) {
            if (image.isNetImage()) {
                imageUrls.add(image.getNetImageUrl());
            } else if (image.isLocalImage()) {
                imageUrls.add(image.getLocalPath());
            }
        }
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

        doShareToQQ((Activity) getContext(), bundle);
    }

    @Override
    protected void onShare(Activity activity, Tencent tencent, Bundle params, IUiListener iUiListener) {
        tencent.shareToQzone(activity, params, iUiListener);
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.QZONE;
    }
}
