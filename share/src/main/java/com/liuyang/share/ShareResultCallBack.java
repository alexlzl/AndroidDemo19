package com.liuyang.share;

import com.liuyang.share.exception.ShareException;

/**
 * 分享结果回调监听
 * Created by liuyang-ds on 2016/12/30.
 */
public interface ShareResultCallBack {

    void onSuccess(SocializeMedia socializeMedia);

    void onError(SocializeMedia socializeMedia ,ShareException exception);

    void onCancel(SocializeMedia socializeMedia);

    void onDealing(SocializeMedia socializeMedia);
}
