package com.gome.eshopnew;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.liuzhouliang.demo11.R;
import com.liuyang.share.ShareConstants;
import com.liuyang.share.ShareHelper;
import com.liuyang.share.ShareInfoParams;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.ShareUtil;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareImage;
import com.liuyang.share.params.ShareParamWebPage;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;


/**
 * @author lzl
 * @ describe  定义第三方分享插件
 * @ time 2018/9/4 18:26
 */
public class SharePlugin extends CordovaPlugin {

    private ShareHelper mPlatform;
    private static final String ACTION_NAME = "showNativeShareComponent";

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if (ACTION_NAME.equals(action)) {
//            ShareInfoParams shareInfo = JSON.parseObject(args.optString(0), new TypeReference<ShareInfoParams>() {
//            });
            ShareInfoParams shareInfo=getShareData();
//            ShareBuilder shareBuilder = new ShareBuilder.Builder(cordova.getActivity())
//                    .setDefaultShareImage(R.drawable.app_icon)
//                    .setQqAppId(ShareConstants.QQ_APPID)
//                    .setQqScope(ShareConstants.QQ_SCOPE)
//                    .setWxAppId(ShareConstants.WECHAT_APPID)
//                    .setSinaAppKey(ShareConstants.SINA_APPKEY)
//                    .setSinaRedirectUrl(ShareConstants.DEFAULT_REDIRECT_URL)
//                    .setSinaScope(ShareConstants.DEFAULT_SCOPE)
//                    .build();
//            mPlatform = new ShareHelper((FragmentActivity) cordova.getActivity(), shareBuilder, back);
//            qqshare(shareInfo);
            if (shareInfo != null && shareInfo.getPlatform().length > 0) {
                ShareUtil shareUtil = new ShareUtil();
                shareUtil.share(shareInfo, cordova.getActivity(), back);
            }else{
                Toast.makeText(cordova.getActivity(),"分享异常",Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;
    }

    private ShareInfoParams getShareData(){
        ShareInfoParams shareInfoParams=new ShareInfoParams();
        shareInfoParams.setTitle("测试分享标题");
        shareInfoParams.setShareDesc("测试分享内容");
        shareInfoParams.setShareType("share_image");
        shareInfoParams.setShareImageUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2131205662,1725422972&fm=200&gp=0.jpg");
        shareInfoParams.setShareUrl("https://www.baidu.com/");
        String[] platform=new String[]{ShareConstants.WEIBO,ShareConstants.WEI_CHAT,ShareConstants.QQ,ShareConstants.WE_CHAT_MOMENTS,ShareConstants.QZONE};
        shareInfoParams.setPlatform(platform);
        return shareInfoParams;

    }


    public void qqshare(ShareInfoParams shareInfo) {
        BaseShareParam param = null;
        param = new ShareParamWebPage(shareInfo.getTitle(), shareInfo.getShareDesc(), shareInfo.getShareUrl());
//        ShareParamImage paramImage = (ShareParamImage) param;
///        paramImage.setImage(new ShareImage(shareInfo.shareImageUrl));
        mPlatform.doShare(param);
    }


    private ShareImage generateImage() {
        //ShareImage image = new ShareImage(file);
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeResource(cordova.getActivity().getResources(), R.drawable.aaaaa, options2);
        ShareImage image = new ShareImage(bitmap);
        //ShareImage image = new ShareImage(resId);
//        ShareImage image = new ShareImage("http://gfs.gomein.net.cn/T13VWTBXCv1RCvBVdK_800.jpg");
//        ShareImage image = new ShareImage(R.drawable.ic_launcher);
        return image;
    }

    public ShareResultCallBack back = new ShareResultCallBack() {

        @Override
        public void onSuccess(SocializeMedia socializeMedia) {
            Toast.makeText(cordova.getActivity(), socializeMedia.name() + "成功了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocializeMedia socializeMedia, ShareException exception) {
            Toast.makeText(cordova.getActivity(), socializeMedia.name() + "失败了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SocializeMedia socializeMedia) {
            Toast.makeText(cordova.getActivity(), socializeMedia.name() + "取消了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDealing(SocializeMedia socializeMedia) {
            Toast.makeText(cordova.getActivity(), socializeMedia.name() + "处理中了", Toast.LENGTH_SHORT).show();
        }
    };
}
