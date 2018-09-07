
package com.gome.eshopnew.wxapi;


import com.liuyang.share.ShareConstants;
import com.liuyang.share.ui.BaseWXEntryActivity;

public class WXEntryActivity extends BaseWXEntryActivity {

    @Override
    protected String getAppId() {
        return ShareConstants.WECHAT_APPID;
    }

}