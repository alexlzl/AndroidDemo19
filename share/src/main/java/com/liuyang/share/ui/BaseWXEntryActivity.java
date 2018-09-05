package com.liuyang.share.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.liuyang.share.ShareConstants;
import com.liuyang.share.platform.weixin.BaseWxShareHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信回调的实现
 */
public abstract class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWXApi();
    }

    private void initWXApi() {
        mIWXAPI = WXAPIFactory.createWXAPI(this, getAppId(), true);
        if (mIWXAPI.isWXAppInstalled()) {
            mIWXAPI.registerApp(getAppId());
        }
        mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if (isAutoFinishAfterOnReq()) {
            finish();
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        parseResult(resp);
        if (isAutoFinishAfterOnResp()) {
            finish();
        }
    }

    private void parseResult(BaseResp resp) {
        if((resp instanceof SendAuth.Resp)){
            SendAuth.Resp r = (SendAuth.Resp) resp;
            String sendAuthCode = r.code;
            sendResult(resp.errCode,sendAuthCode);
        }else{
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    sendResult(ShareConstants.ST_CODE_SUCCESSED);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    sendResult(ShareConstants.ST_CODE_ERROR_CANCEL);
                    break;
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    sendResult(ShareConstants.ST_CODE_ERROR);
                    break;
            }
        }

    }
    //分享
    private void sendResult(int statusCode) {
        Intent intent = new Intent(BaseWxShareHandler.ACTION_RESULT);
        intent.putExtra(BaseWxShareHandler.BUNDLE_STATUS_CODE, statusCode);
        sendBroadcast(intent);
    }
    //登陆
    private void sendResult(int statusCode,String sendAuthCode) {
        Intent intent = new Intent(BaseWxShareHandler.ACTION_LOGIN_RESULT);
        intent.putExtra(BaseWxShareHandler.BUNDLE_STATUS_CODE, statusCode);
        intent.putExtra(BaseWxShareHandler.BUNDLE_SENDAUTHCODE, sendAuthCode);
        sendBroadcast(intent);
    }


    protected boolean isAutoFinishAfterOnReq() {
        return true;
    }

    protected boolean isAutoFinishAfterOnResp() {
        return true;
    }

    protected boolean isAutoCreateWXAPI() {
        return true;
    }

    protected abstract String getAppId();

}
