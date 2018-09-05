package com.liuyang.share;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.liuyang.share.model.ShareTarget;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.show.BaseSharePlatformSelector;
import com.liuyang.share.show.DialogSharePlatformSelector;

/**
 * 分享帮助，便于调用
 * Created by liuyang-ds on 2016/11/28.
 */
public class ShareHelper {

    public SharePlatformDistribution mPlatformDistribution;
    private FragmentActivity mContext;
    private BaseSharePlatformSelector mPlatformSelector;
    private ShareResultCallBack mCallback;
    private BaseShareParam mShareParam;

    public ShareHelper(FragmentActivity context, ShareBuilder shareBuilder,ShareResultCallBack callback) {
        if (context == null) {
            throw new NullPointerException("context must be not null");
        }
        if(shareBuilder==null){
            throw new NullPointerException("shareBuilder must be not null");
        }
        mContext = context;
        mCallback = callback;
        mPlatformDistribution = new SharePlatformDistribution(shareBuilder);
    }

    public void toShare(BaseShareParam content) {
        this.mShareParam = content;
        if(mPlatformSelector==null){
            mPlatformSelector = new DialogSharePlatformSelector(mContext, new BaseSharePlatformSelector.OnShareSelectorDismissListener() {
                @Override
                public void onDismiss() {
                    onShareSelectorDismiss();
                }
            }, mShareItemClick);
        }
        mPlatformSelector.show();
    }

    private AdapterView.OnItemClickListener mShareItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            shareTo((ShareTarget) parent.getItemAtPosition(position));
            onShareSelectorDismiss();
        }
    };

   private void onShareSelectorDismiss() {
        mPlatformSelector.dismiss();
    }

    private void shareTo(ShareTarget item) {
        if (mShareParam == null) {
            return;
        }
        mPlatformDistribution.share(mContext, item.media, mShareParam, mCallback);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mPlatformDistribution.getmShareHandler()!=null){
            mPlatformDistribution.getmShareHandler().onActivityResult(requestCode,resultCode,data);
        }
    }

}
