package com.liuyang.share.exception;


import com.liuyang.share.ShareConstants;

/**
 * 自定义异常 缺少配置
 */
public class ImageDisposeException extends ShareException{

    public ImageDisposeException(String detailMessage) {
        super(detailMessage);
        setCode(ShareConstants.ST_CODE_SHARE_ERROR_IMAGE);
    }
}
