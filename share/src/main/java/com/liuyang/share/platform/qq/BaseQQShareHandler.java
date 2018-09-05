package com.liuyang.share.platform.qq;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.exception.UninstalledAPPException;
import com.liuyang.share.platform.base.BaseShareHandler;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


/**
 * 基础的QQ分享
 */
public abstract class BaseQQShareHandler extends BaseShareHandler {
    protected Tencent mTencent;

    public BaseQQShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    @Override
    protected void init() throws ShareException {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(getShareBuilder().getQqAppId(), getContext().getApplicationContext());
        }
    }
    /**
     * 必须在主线程分享
     *
     * @param activity
     * @param params
     */
    protected void doShareToQQ(final Activity activity, final Bundle params) {
        doOnMainThread(new Runnable() {
            @Override
            public void run() {
                onShare(activity, mTencent, params, mUiListener);
                if (activity != null && !Util.isMobileQQSupportShare(activity.getApplicationContext())) {
                    String msg = getContext().getString(R.string.share_sdk_not_install_qq);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    if (getShareResultCallBack()!= null) {
                        getShareResultCallBack().onError(getShareMedia(), new UninstalledAPPException(msg));
                    }
                }
            }
        });
    }

    protected abstract void onShare(Activity activity, Tencent tencent, Bundle params, IUiListener iUiListener);

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    protected final IUiListener mUiListener = new IUiListener() {
        @Override
        public void onCancel() {
            if (getShareResultCallBack() != null) {
                getShareResultCallBack().onCancel(getShareMedia());
            }
        }

        @Override
        public void onComplete(Object response) {
            if (getShareResultCallBack()!= null) {
                getShareResultCallBack().onSuccess(getShareMedia());
            }
        }

        @Override
        public void onError(UiError e) {
            if (getShareResultCallBack()!= null) {
                getShareResultCallBack().onError(getShareMedia(), new ShareException(e.errorMessage));
            }
        }
    };
}
