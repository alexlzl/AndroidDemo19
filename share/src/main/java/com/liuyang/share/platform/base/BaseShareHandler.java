package com.liuyang.share.platform.base;

import android.app.Activity;

import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareParamAudio;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareParamWebPage;

/**
 * 暴露需要分享的不同形式，便于不同平台实现
 * Created by liuyang-ds on 2016/12/30.
 */
public abstract class BaseShareHandler extends AbsShareHandler {


    public BaseShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    /**
     * 统一处理各种分享
     * @param params
     * @param shareResultCallBack
     * @throws Exception
     */
    @Override
    public void share(BaseShareParam params, ShareResultCallBack shareResultCallBack) throws ShareException {
        super.share(params, shareResultCallBack);
        init();
        mImageHelper.setShareResultCallBack(shareResultCallBack);
        mImageHelper.saveBitmapToExternalIfNeed(params);
        mImageHelper.copyImageToCacheFileDirIfNeed(params);
        if (params instanceof ShareParamText) {
            shareText((ShareParamText) params);
        } else if (params instanceof ShareParamImage) {
            shareImage((ShareParamImage) params);
        } else if (params instanceof ShareParamWebPage) {
            shareWebPage((ShareParamWebPage) params);
        } else if (params instanceof ShareParamAudio) {
            shareAudio((ShareParamAudio) params);
        } else if (params instanceof ShareParamVideo) {
            shareVideo((ShareParamVideo) params);
        }
    }

    /**
     * 初始化不同平台分享需要的参数
     *
     * @throws Exception
     */
    protected abstract void init() throws ShareException;

    /**
     * 文字分享
     *
     * @param params
     * @throws ShareException
     */
    protected abstract void shareText(ShareParamText params) throws ShareException;

    /**
     * 图片分享
     *
     * @param params
     * @throws ShareException
     */
    protected abstract void shareImage(ShareParamImage params) throws ShareException;

    /**
     * url分享
     *
     * @param params
     * @throws ShareException
     */
    protected abstract void shareWebPage(ShareParamWebPage params) throws ShareException;

    /**
     * Audio分享
     *
     * @param params
     * @throws ShareException
     */
    protected abstract void shareAudio(ShareParamAudio params) throws ShareException;

    /**
     * Video分享
     *
     * @param params
     * @throws ShareException
     */
    protected abstract void shareVideo(ShareParamVideo params) throws ShareException;
}
