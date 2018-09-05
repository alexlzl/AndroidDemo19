package com.liuyang.share.ui;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.common.AssistActivity;

/**
 * 修复分享完成后通过非正常方式返回app后界面卡死的bug。
 */
public class QQAssistAdapterActivity extends AssistActivity {
    private boolean mIsRestartFromQQSDK;
    private boolean mHasActivityResultCalled;
    private boolean mHasOnIntentCalled;

    @Override
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            if (bundle != null) {
                mIsRestartFromQQSDK = bundle.getBoolean("RESTART_FLAG");
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasOnIntentCalled || mHasActivityResultCalled) {
            return;
        }

        if (mIsRestartFromQQSDK && !isFinishing()) {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mHasOnIntentCalled = true;
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int i, int i1, Intent intent) {
        mHasActivityResultCalled = true;
        super.onActivityResult(i, i1, intent);
    }
}
