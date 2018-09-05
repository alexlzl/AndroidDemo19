package com.liuyang.share.platform.generic;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.liuyang.share.helper.BuildHelper;
import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.platform.base.BaseShareHandler;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareParamAudio;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareParamWebPage;


/**
 * ShareParam，只读取content的内容。
 */
public class CopyShareHandler extends BaseShareHandler {

    public CopyShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // nothing to do
    }

    @Override
    protected void init() throws ShareException {

    }

    @Override
    protected void shareText(ShareParamText params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareImage(ShareParamImage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareWebPage(ShareParamWebPage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareAudio(ShareParamAudio params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareVideo(ShareParamVideo params) throws ShareException {
        share(params);
    }

    private void share(BaseShareParam param) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        String content = param.getContent();
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (BuildHelper.isApi11_HoneyCombOrLater()) {
            clip.setPrimaryClip(ClipData.newPlainText(null, content));
        } else {
            ((android.text.ClipboardManager) clip).setText(content);
        }
        Toast.makeText(context, R.string.share_sdk_share_copy, Toast.LENGTH_SHORT).show();
    }


    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.COPY;
    }

}
