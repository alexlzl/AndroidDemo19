package com.liuyang.share.platform.base;

import android.content.Intent;

import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;

/**
 * 定义分享平台必须要实现的方法
 * Created by liuyang-ds on 2016/12/30.
 */
public abstract class ShareHandler {
    /**
     * 处理一些平台需要在onActivityResult里的操作
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * 各个平台统一的分享调用
     * @param params
     * @param shareResultCallBack
     * @throws Exception
     */
    public abstract void share(BaseShareParam params, ShareResultCallBack shareResultCallBack) throws ShareException;

    /**
     * 平台类型，便于获取结果后后续处理
     * @return
     */
    public abstract SocializeMedia getShareMedia();
}
