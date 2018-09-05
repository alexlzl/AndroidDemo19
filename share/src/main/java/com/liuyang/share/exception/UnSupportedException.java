
package com.liuyang.share.exception;


import com.liuyang.share.ShareConstants;

/**
 * 自定义异常 不支持的分享
 */
public class UnSupportedException extends ShareException {

    public UnSupportedException(String detailMessage) {
        super(detailMessage);
        setCode(ShareConstants.ST_CODE_SHARE_ERROR_PARAM_UNSUPPORTED);
    }

}
