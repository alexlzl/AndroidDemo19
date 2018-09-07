package com.gome.eshopnew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareConstants;
import com.liuyang.share.ShareData;
import com.liuyang.share.ShareHelper;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.ShareUtil;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.exception.ShareException;

/**
 * 测试分享页面
 */
public class ShareActivity extends FragmentActivity implements View.OnClickListener {
    private Button btnShare;
    private ShareHelper mShareHelper;
    private String video = "https://v1-tt.ixigua.com/2ce0de4ab005d865410ee0f140e0e8e9/5b90a9d3/video/m/220e5969680e3ef4e1aaa376786975d9384115b1fd50000380c95d4543f/";

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
                ShareData shareInfo = getShareImageData();
                if (shareInfo != null && shareInfo.getPlatform().length > 0) {
                    mShareHelper = new ShareHelper(shareInfo, this, getShareBuilder(), back);
                    ShareUtil.getShareUtil().share(mShareHelper, shareInfo, this);
                } else {
                    Toast.makeText(this, "分享异常", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private ShareBuilder getShareBuilder(){

        return  new ShareBuilder.Builder(ShareActivity.this)
                .setDefaultShareImage(R.drawable.ic_launcher)
                .setQqAppId(ShareConstants.QQ_APPID)
                .setQqScope(ShareConstants.QQ_SCOPE)
                .setWxAppId(ShareConstants.WECHAT_APPID)
                .setSinaAppKey(ShareConstants.SINA_APPKEY)
                .setSinaRedirectUrl(ShareConstants.DEFAULT_REDIRECT_URL)
                .setSinaScope(ShareConstants.DEFAULT_SCOPE)
                .build();
    }

//    private ShareBuilder getShareBuilder(ShareData shareInfoParams) {
//        int size = shareInfoParams.getPlatform().length;
//
//        ShareBuilder.Builder shareBuilder = new ShareBuilder.Builder(this)
//                .setDefaultShareImage(com.liuyang.share.R.drawable.app_icon);
//        for (int i = 0; i < size; i++) {
//            if (ShareConstants.QQ.equals(shareInfoParams.getPlatform()[i]) || ShareConstants.QZONE.equals(shareInfoParams.getPlatform()[i])) {
//                shareBuilder.setQqAppId(ShareConstants.QQ_APPID).setQqScope(ShareConstants.QQ_SCOPE);
//            } else if (ShareConstants.WEI_CHAT.equals(shareInfoParams.getPlatform()[i]) || ShareConstants.WE_CHAT_MOMENTS.equals(shareInfoParams.getPlatform()[i])) {
//                shareBuilder.setWxAppId(ShareConstants.WECHAT_APPID);
//            } else if (ShareConstants.WEIBO.equals(shareInfoParams.getPlatform()[i])) {
//                shareBuilder.setSinaAppKey(ShareConstants.SINA_APPKEY)
//                        .setSinaRedirectUrl(ShareConstants.DEFAULT_REDIRECT_URL)
//                        .setSinaScope(ShareConstants.DEFAULT_SCOPE);
//            }
//        }
//
//        return shareBuilder.build();
//
//
//    }

    /**
     * 测试数据
     *
     * @return
     */
    public void shareText(View view) {
        ShareData shareInfo = getShareTextData();
        if (shareInfo != null && shareInfo.getPlatform().length > 0) {
            mShareHelper = new ShareHelper(shareInfo, this, getShareBuilder(), back);
            ShareUtil.getShareUtil().share(mShareHelper, shareInfo, this);
        } else {
            Toast.makeText(this, "分享异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 测试数据
     *
     * @return
     */
    public void shareVideo(View view) {
        ShareData shareInfo = getShareVideoData();
        if (shareInfo != null && shareInfo.getPlatform().length > 0) {
            mShareHelper = new ShareHelper(shareInfo, this, getShareBuilder(), back);
            ShareUtil.getShareUtil().share(mShareHelper, shareInfo, this);
        } else {
            Toast.makeText(this, "分享异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 测试数据
     *
     * @return
     */
    private ShareData getShareVideoData() {
        ShareData shareInfoParams = new ShareData();
        shareInfoParams.setTitle("这个名字不错");
        shareInfoParams.setShareDesc("这个内容很有意思");
        shareInfoParams.setShareType(ShareConstants.SHARE_VIDEO);
        shareInfoParams.setShareImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536208706724&di=db42f6e28c6738485b4ab11352a7244f&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201305%2F26%2F20130526140022_5fMJe.jpeg");
        shareInfoParams.setShareUrl(video);
        String[] platform = new String[]{ShareConstants.WEI_CHAT, ShareConstants.QZONE, ShareConstants.WEIBO, ShareConstants.QQ, ShareConstants.WE_CHAT_MOMENTS};
        shareInfoParams.setPlatform(platform);
        return shareInfoParams;

    }

    /**
     * 测试数据
     *
     * @return
     */
    private ShareData getShareTextData() {
        ShareData shareInfoParams = new ShareData();
        shareInfoParams.setTitle("这个名字不错");
        shareInfoParams.setShareDesc("这个内容很有意思");
        shareInfoParams.setShareType(ShareConstants.SHARE_TEXT);
        shareInfoParams.setShareImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536208706724&di=db42f6e28c6738485b4ab11352a7244f&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201305%2F26%2F20130526140022_5fMJe.jpeg");
        shareInfoParams.setShareUrl("http://item.gome.com.cn/9133860280-1122860067.html");
        String[] platform = new String[]{ShareConstants.WEIBO, ShareConstants.QZONE, ShareConstants.WEI_CHAT, ShareConstants.QQ, ShareConstants.WE_CHAT_MOMENTS};
        shareInfoParams.setPlatform(platform);
        return shareInfoParams;

    }

    /**
     * 测试数据
     *
     * @return
     */
    private ShareData getShareImageData() {
        ShareData shareInfoParams = new ShareData();
        shareInfoParams.setTitle("这个名字不错");
        shareInfoParams.setShareDesc("这个内容很有意思");
        shareInfoParams.setShareType(ShareConstants.SHARE_IMAGE);
        shareInfoParams.setShareImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536237347811&di=d0d45fe3548e21f0818911547c1c5237&imgtype=0&src=http%3A%2F%2Fwww.xz7.com%2Fup%2F2017-12%2F2017122310640.jpg");
        shareInfoParams.setShareUrl("http://item.gome.com.cn/9133860280-1122860067.html");
//        String[] platform = new String[]{ShareConstants.QZONE,  ShareConstants.WEI_CHAT, ShareConstants.QQ, ShareConstants.WE_CHAT_MOMENTS};
//        String[] platform = new String[]{ShareConstants.QZONE};
        String[] platform = new String[]{ShareConstants.QZONE,  ShareConstants.WEI_CHAT};
//        String[] platform = new String[]{ShareConstants.QZONE,  ShareConstants.WEI_CHAT, ShareConstants.QQ, ShareConstants.WE_CHAT_MOMENTS};
        shareInfoParams.setPlatform(platform);
        return shareInfoParams;

    }


    public ShareResultCallBack back = new ShareResultCallBack() {

        @Override
        public void onSuccess(SocializeMedia socializeMedia) {
            Log.e("TAGG", "正常回调============" + "onSuccess");
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "成功了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocializeMedia socializeMedia, ShareException exception) {
            Log.e("TAGG", "正常回调============" + "onError");
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "失败了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SocializeMedia socializeMedia) {
            Log.e("TAGG", "正常回调============" + "onCancel");
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "取消了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDealing(SocializeMedia socializeMedia) {
            Log.e("TAGG", "正常回调============" + "onDealing");
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "处理中了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mShareHelper != null) {
            mShareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
