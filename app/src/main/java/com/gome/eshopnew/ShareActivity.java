package com.gome.eshopnew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.liuzhouliang.demo11.R;
import com.liuyang.share.ShareConstants;
import com.liuyang.share.ShareHelper;
import com.liuyang.share.ShareInfoParams;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.ShareUtil;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.ShareException;

/**
 * Created by liuyang-ds on 2016/12/28.
 */
public class ShareActivity extends FragmentActivity implements View.OnClickListener{
    private Button btnShare;
    private ShareHelper mPlatform;
//    public static final String QQ_APPID = "100825615";
//    public static final String QQ_SCOPE = "all";
//    public static final String WECHAT_APPID = "wx4cc1d66050b7aba1";
//    public static final String WECHAT_APPSECRET = "abf27897df4c5c9ad61cdf80c956495a";
//    public static final String SINA_APPKEY = "686166542";
//    public static final String DEFAULT_REDIRECT_URL = "http://www.gome.com.cn";
//    public static final String DEFAULT_SCOPE = "email,direct_messages_read,direct_messages_write,"
//            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
//            + "follow_app_official_microblog," + "invitation_write";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShare:
//                ShareBuilder shareBuilder = new ShareBuilder.Builder(ShareActivity.this)
//                        .setDefaultShareImage(R.drawable.ic_launcher)
//                        .setQqAppId(QQ_APPID)
//                        .setQqScope(QQ_SCOPE)
//                        .setWxAppId(WECHAT_APPID)
//                        .setSinaAppKey(SINA_APPKEY)
//                        .setSinaRedirectUrl(DEFAULT_REDIRECT_URL)
//                        .setSinaScope(DEFAULT_SCOPE)
//                        .build();
//                mPlatform = new ShareHelper(getShareData(),ShareActivity.this,shareBuilder,back);
//                qqshare();
                ShareInfoParams shareInfo=getShareData();
                if (shareInfo != null && shareInfo.getPlatform().length > 0) {
                    ShareUtil shareUtil = new ShareUtil();
                    shareUtil.share(shareInfo,this, back);
                }else{
                    Toast.makeText(this,"分享异常",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private ShareInfoParams getShareData(){
        ShareInfoParams shareInfoParams=new ShareInfoParams();
        shareInfoParams.setTitle("测试分享标题");
        shareInfoParams.setShareDesc("测试分享内容");
        shareInfoParams.setShareType("share_image");
        shareInfoParams.setShareImageUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2131205662,1725422972&fm=200&gp=0.jpg");
        shareInfoParams.setShareUrl("https://www.baidu.com/");
        String[] platform=new String[]{ShareConstants.QZONE,ShareConstants.WEIBO,ShareConstants.WEI_CHAT,ShareConstants.QQ,ShareConstants.WE_CHAT_MOMENTS};
        shareInfoParams.setPlatform(platform);
        return shareInfoParams;

    }

//    public void qqshare(){
//        BaseShareParam param = null;
//        param = new ShareParamImage("飞利浦SPS2620X/93插线板", "飞利浦SPS2620X/93插线板", "http://item.gome.com.cn/9133860280-1122860067.html");
//        ShareParamImage paramImage = (ShareParamImage) param;
//        paramImage.setImage(generateImage());
//        mPlatform.doShare(param);
//    }

//    private ShareImage generateImage() {
//        //ShareImage image = new ShareImage(file);
//        BitmapFactory.Options options2 = new BitmapFactory.Options();
//        options2.inPreferredConfig = Bitmap.Config.RGB_565;
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.aaaaa,options2);
//        ShareImage image = new ShareImage(bitmap);
//        //ShareImage image = new ShareImage(resId);
////        ShareImage image = new ShareImage("http://gfs.gomein.net.cn/T13VWTBXCv1RCvBVdK_800.jpg");
////        ShareImage image = new ShareImage(R.drawable.ic_launcher);
//        return image;
//    }
    public ShareResultCallBack back = new ShareResultCallBack(){

        @Override
        public void onSuccess(SocializeMedia socializeMedia) {
            Toast.makeText(ShareActivity.this,socializeMedia.name()+"成功了",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocializeMedia socializeMedia, ShareException exception) {
            Toast.makeText(ShareActivity.this,socializeMedia.name()+"失败了",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SocializeMedia socializeMedia) {
            Toast.makeText(ShareActivity.this,socializeMedia.name()+"取消了",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDealing(SocializeMedia socializeMedia) {
            Toast.makeText(ShareActivity.this,socializeMedia.name()+"处理中了",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPlatform!=null){
            mPlatform.onActivityResult(requestCode,resultCode,data);
        }
    }
}
