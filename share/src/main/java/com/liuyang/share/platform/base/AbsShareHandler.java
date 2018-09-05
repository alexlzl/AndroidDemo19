package com.liuyang.share.platform.base;

import android.app.Activity;
import android.content.Context;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.exception.WorkThreadException;
import com.liuyang.share.helper.ShareImageHelper;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.utils.ThreadManager;

import java.util.concurrent.Executors;

/**
 * 暴露主线程 以及工作线程执行分享的方法，便于不同平台调用
 * 抽象的ShareHandler
 */
public abstract class AbsShareHandler extends ShareHandler {

    private Context mContext;
    protected ShareImageHelper mImageHelper;
    private ShareResultCallBack mShareResultCallBack;
    private ShareBuilder mShareBuilder;

    public AbsShareHandler(Activity context,ShareBuilder shareBuilder) {
        initContext(context);//为mContext赋值，便于后续使用
        this.mShareBuilder = shareBuilder;
        mImageHelper = new ShareImageHelper(mContext, getShareBuilder(),getShareMedia());
    }

    public ShareBuilder getShareBuilder() {
        return mShareBuilder;
    }

    public ShareResultCallBack getShareResultCallBack() {
        return mShareResultCallBack;
    }

    public Context getContext() {
        return mContext;
    }

    protected boolean isNeedActivityContext() {
        return false;
    }

    private void initContext(Activity context) {
        if (isNeedActivityContext()) {
            mContext = context;
        } else {
            mContext = context.getApplicationContext();
        }
    }

    @Override
    public void share(BaseShareParam params, ShareResultCallBack shareResultCallBack) throws ShareException {
        this.mShareResultCallBack = shareResultCallBack;//为mShareResultCallBack赋值，便于后续使用
    }

    /**
     * 子线程工作
     * @param runnable
     */
    protected void doOnWorkThread(final Runnable runnable) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();

                    if (mShareResultCallBack != null) {
                        doOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mShareResultCallBack.onError(getShareMedia(), new WorkThreadException(getContext().getString(R.string.share_sdk_thread_work__failed)));
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 主线程工作
     * @param runnable
     */
    protected void doOnMainThread(Runnable runnable) {
        ThreadManager.getMainHandler().post(runnable);
    }


}
