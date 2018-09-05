package com.liuyang.share.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareConstants;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.platform.weibo.SinaShareHandler;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;


/**
 * 处理微博分享，相当于QQ的{@link //com.tencent.connect.common.AssistActivity}
 */
public class SinaAssistActivity extends Activity implements IWeiboHandler.Response {

    public static final String KEY_CONFIG = "sina_share_config";
    public static final String KEY_CODE = "sina_share_result_code";
    public static final String KEY_PARAM = "sina_share_param";

    private SinaShareHandler mShareHandler;

    private boolean mIsActivityResultCanceled;
    private boolean mHasOnNewIntentCalled;
    private boolean mHasResponseCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareBuilder shareBuilder = getIntent().getParcelableExtra(KEY_CONFIG);
        mShareHandler = new SinaShareHandler(this,shareBuilder);
        try {
            mShareHandler.init();
        } catch (Exception e) {
            e.printStackTrace();
            finishWithFailResult();
            return;
        }
        mShareHandler.onActivityCreated(this, savedInstanceState);
        try {
            if (savedInstanceState == null) {
                BaseShareParam param = getShareParam();
                if (param == null) {
                    mInnerListener.onError(SocializeMedia.SINA,new ShareException(this.getString(R.string.share_sdk_sina_params_fail)));
                    finishWithCancelResult();
                } else {
                    mShareHandler.share(getShareParam(), mInnerListener);
                }
            }
        } catch (Exception e) {
            mInnerListener.onError(SocializeMedia.SINA,new ShareException(e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasOnNewIntentCalled || mHasResponseCalled) {
            return;
        }
        if (mShareHandler.mWeiboShareAPI != null &&
                mShareHandler.mWeiboShareAPI.isWeiboAppInstalled() &&
                mIsActivityResultCanceled && !isFinishing()) {
            finishWithCancelResult();
        }
    }

    private BaseShareParam getShareParam() {
        return getIntent().getParcelableExtra(KEY_PARAM);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mHasOnNewIntentCalled = true;
        mShareHandler.onActivityNewIntent(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIsActivityResultCanceled = resultCode == Activity.RESULT_CANCELED;
        mShareHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        mHasResponseCalled = true;
        if (mShareHandler != null) {
            mShareHandler.onResponse(baseResponse);
        }
    }

    private void finishWithCancelResult() {
        finishWithResult(ShareConstants.ST_CODE_ERROR_CANCEL);
    }

    private void finishWithFailResult() {
        finishWithResult(ShareConstants.ST_CODE_ERROR);
    }

    private void finishWithSuccessResult() {
        finishWithResult(ShareConstants.ST_CODE_SUCCESSED);
    }

    ShareResultCallBack mInnerListener = new ShareResultCallBack() {


        @Override
        public void onSuccess(SocializeMedia socializeMedia) {
            finishWithSuccessResult();
        }

        @Override
        public void onError(SocializeMedia socializeMedia, ShareException exception) {
            finishWithFailResult();
        }

        @Override
        public void onCancel(SocializeMedia socializeMedia) {
            finishWithCancelResult();
        }

        @Override
        public void onDealing(SocializeMedia socializeMedia) {

        }
    };

    private void finishWithResult(int code) {
        Intent intent = new Intent();
        intent.putExtra(KEY_CODE, code);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
