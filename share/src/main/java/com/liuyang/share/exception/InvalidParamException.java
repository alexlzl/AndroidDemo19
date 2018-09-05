package com.liuyang.share.exception;


import com.liuyang.share.ShareConstants;

/**
 * 自定义异常 分享参数有问题
 */
public class InvalidParamException extends ShareException{

    public InvalidParamException(String detailMessage) {
        super(detailMessage);
        setCode(ShareConstants.ST_CODE_SHARE_ERROR_PARAM_INVALID);
    }
}
