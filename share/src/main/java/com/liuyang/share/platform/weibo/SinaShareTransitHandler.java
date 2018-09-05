package com.liuyang.share.platform.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareConstants;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.platform.base.AbsShareHandler;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.ui.SinaAssistActivity;


/**
 * 新浪微博分享跳转授权
 */
public class SinaShareTransitHandler extends AbsShareHandler {

    public static final int REQ_CODE = 10233;
    public SinaShareTransitHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null||getShareResultCallBack()==null) {
            return;
        }
        int statusCode = data.getIntExtra(SinaAssistActivity.KEY_CODE, -1);
        if (statusCode == ShareConstants.ST_CODE_SUCCESSED) {
            getShareResultCallBack().onSuccess(getShareMedia());
        } else if (statusCode == ShareConstants.ST_CODE_ERROR) {
            getShareResultCallBack().onError(getShareMedia(), new ShareException(getContext().getString(R.string.share_sdk_sina_share_fail)));
        } else if (statusCode == ShareConstants.ST_CODE_ERROR_CANCEL) {
            getShareResultCallBack().onCancel(getShareMedia());
        }

    }

    @Override
    public void share(final BaseShareParam params, ShareResultCallBack listener) throws ShareException {
        super.share(params, listener);
        final Context context = getContext();
        mImageHelper.saveBitmapToExternalIfNeed(params);
        mImageHelper.copyImageToCacheFileDirIfNeed(params);
        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, SinaAssistActivity.class);
                intent.putExtra(SinaAssistActivity.KEY_PARAM, params);
                intent.putExtra(SinaAssistActivity.KEY_CONFIG, getShareBuilder());
                ((Activity) context).startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.SINA;
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

}
