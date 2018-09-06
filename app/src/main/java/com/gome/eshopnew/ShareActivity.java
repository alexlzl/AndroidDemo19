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
public class ShareActivity extends FragmentActivity implements View.OnClickListener {
    private Button btnShare;
    private ShareHelper mPlatform;

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
                ShareInfoParams shareInfo = getShareData();
                if (shareInfo != null && shareInfo.getPlatform().length > 0) {
//                    ShareUtil shareUtil = new ShareUtil();
                    ShareUtil.getShareUtil().share(shareInfo, this, back);
                } else {
                    Toast.makeText(this, "分享异常", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 测试数据
     *
     * @return
     */
    private ShareInfoParams getShareData() {
        ShareInfoParams shareInfoParams = new ShareInfoParams();
        shareInfoParams.setTitle("这个名字不错");
        shareInfoParams.setShareDesc("这个内容很有意思");
        shareInfoParams.setShareType(ShareConstants.SHARE_IMAGE);
        shareInfoParams.setShareImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536208706724&di=db42f6e28c6738485b4ab11352a7244f&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201305%2F26%2F20130526140022_5fMJe.jpeg");
        shareInfoParams.setShareUrl("http://item.gome.com.cn/9133860280-1122860067.html");
        String[] platform = new String[]{ShareConstants.QZONE, ShareConstants.WEIBO, ShareConstants.WEI_CHAT, ShareConstants.QQ, ShareConstants.WE_CHAT_MOMENTS};
        shareInfoParams.setPlatform(platform);
        return shareInfoParams;

    }


    public ShareResultCallBack back = new ShareResultCallBack() {

        @Override
        public void onSuccess(SocializeMedia socializeMedia) {
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "成功了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocializeMedia socializeMedia, ShareException exception) {
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "失败了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SocializeMedia socializeMedia) {
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "取消了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDealing(SocializeMedia socializeMedia) {
            Toast.makeText(ShareActivity.this, socializeMedia.name() + "处理中了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPlatform != null) {
            mPlatform.onActivityResult(requestCode, resultCode, data);
        }
    }
}
