package com.liuyang.share.show;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.liuyang.share.R;
import com.liuyang.share.ShareInfoParams;


/**
 * 包裹内容实现
 */
public class PopWrapSharePlatformSelector extends BaseSharePlatformSelector {
    protected PopupWindow mShareWindow;
    protected View mAnchorView;

    public PopWrapSharePlatformSelector(ShareInfoParams shareInfoParams, FragmentActivity context, View anchorView, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        super(shareInfoParams,context, dismissListener, itemClickListener);
        mAnchorView = anchorView;
    }

    @Override
    public void show() {
        createShareWindowIfNeed();
        if (!mShareWindow.isShowing()) {
            mShareWindow.showAtLocation(mAnchorView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void dismiss() {
        if (mShareWindow != null) {
            mShareWindow.dismiss();
        }
    }

    @Override
    public void release() {
        dismiss();
        mShareWindow = null;
        super.release();
        mAnchorView = null;
    }

    protected void createShareWindowIfNeed() {
        if (mShareWindow != null)
            return;

        Context context = getContext();
        GridView grid = createShareGridView(context, getItemClickListener());
        mShareWindow = new PopupWindow(grid, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mShareWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mShareWindow.setOutsideTouchable(true);
        mShareWindow.setAnimationStyle(R.style.share_socialize_shareboard_animation);
        mShareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (getDismissListener() != null)
                    getDismissListener().onDismiss();
            }
        });
    }

}
