package com.liuyang.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;

import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareImage;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;

import java.io.File;

/**
 * @author lzl
 * @ describe
 * @ time 2018/9/4 18:09
 */
public class ShareUtil {
    private Context mContext;

    public void share(ShareInfoParams shareInfoParams, Context context, ShareResultCallBack callBack) {
        mContext = context;
        ShareHelper mShareHelper = new ShareHelper(shareInfoParams,(FragmentActivity) context, getShareBuilder(shareInfoParams), callBack);
        mShareHelper.doShare(getShareDataParam(shareInfoParams));
    }

    /**
     * @ describe  初始化分享渠道
     * @author lzl
     * @ time 2018/9/5 10:26
     * @ param
     * @ return
     */
    private ShareBuilder getShareBuilder(ShareInfoParams shareInfoParams) {
        int size = shareInfoParams.getPlatform().length;

        ShareBuilder.Builder shareBuilder = new ShareBuilder.Builder(mContext)
                .setDefaultShareImage(R.drawable.app_icon);
        for (int i = 0; i < size; i++) {
            if (ShareConstants.QQ.equals(shareInfoParams.getPlatform()[i]) || ShareConstants.QZONE.equals(shareInfoParams.getPlatform()[i])) {
                shareBuilder.setQqAppId(ShareConstants.QQ_APPID).setQqScope(ShareConstants.QQ_SCOPE);
            } else if (ShareConstants.WEI_CHAT.equals(shareInfoParams.getPlatform()[i]) || ShareConstants.WE_CHAT_MOMENTS.equals(shareInfoParams.getPlatform()[i])) {
                shareBuilder.setWxAppId(ShareConstants.WECHAT_APPID);
            } else if (ShareConstants.WEIBO.equals(shareInfoParams.getPlatform()[i])) {
                shareBuilder.setSinaAppKey(ShareConstants.SINA_APPKEY)
                        .setSinaRedirectUrl(ShareConstants.DEFAULT_REDIRECT_URL)
                        .setSinaScope(ShareConstants.DEFAULT_SCOPE);
            } else {
                shareBuilder.setWxAppId(ShareConstants.WECHAT_APPID);
            }
        }

        return shareBuilder.build();
//        ShareBuilder shareBuilder = new ShareBuilder.Builder(mContext)
//                        .setDefaultShareImage(R.drawable.app_icon)
//                        .setQqAppId(ShareConstants.QQ_APPID)
//                        .setQqScope(ShareConstants.QQ_SCOPE)
//                        .setWxAppId(ShareConstants.WECHAT_APPID)
//                        .setSinaAppKey(ShareConstants.SINA_APPKEY)
//                        .setSinaRedirectUrl(ShareConstants.DEFAULT_REDIRECT_URL)
//                        .setSinaScope(ShareConstants.DEFAULT_SCOPE)
//                        .build();
//        return shareBuilder;

    }


    /**
     * @ describe  初始化分享数据类型
     * @author lzl
     * @ time 2018/9/5 11:09
     * @ param
     * @ return
     */
    private BaseShareParam getShareDataParam(ShareInfoParams shareInfo) {
        BaseShareParam param = null;

        if (ShareConstants.SHARE_IMAGE.equals(shareInfo.getShareType())) {
            /**
             * 图片类型分享
             */
            param = new ShareParamImage(shareInfo.getTitle(), shareInfo.getShareDesc(), shareInfo.getShareUrl());
            ((ShareParamImage) param).setImage(generateUrlImage(shareInfo.getShareImageUrl()));
        } else if (ShareConstants.SHARE_TEXT.equals(shareInfo.getShareType())) {
            /**
             * 文本类型分享
             */
            param = new ShareParamText(shareInfo.getTitle(), shareInfo.getShareDesc(), shareInfo.getShareUrl());
        }
        return param;

    }

    /**
     * @ describe 通过资源ID获取ShareImage
     * @author lzl
     * @ time 2018/9/5 11:26
     * @ param
     * @ return
     */
    private ShareImage generateResImage(int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resourceId, options);
        return new ShareImage(bitmap);
    }

    /**
     * @ describe 通过URL获取ShareImage
     * @author lzl
     * @ time 2018/9/5 11:26
     * @ param
     * @ return
     */
    private ShareImage generateUrlImage(String url) {
        return new ShareImage(url);
    }

    /**
     * @ describe 通过文件获取ShareImage
     * @author lzl
     * @ time 2018/9/5 11:26
     * @ param
     * @ return
     */
    private ShareImage generateFileImage(File file) {
        return new ShareImage(file);
    }


}
