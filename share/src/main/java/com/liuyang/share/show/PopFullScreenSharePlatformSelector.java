package com.liuyang.share.show;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.liuyang.share.R;
import com.liuyang.share.ShareInfoParams;


/**
 * 铺满横屏实现
 */
public class PopFullScreenSharePlatformSelector extends BaseSharePlatformSelector implements View.OnClickListener {

    protected PopupWindow mShareWindow;
    protected View mAnchorView;
    protected RelativeLayout mContainerView;

    private GridView grid;
    private Animation enterAnimation;

    public PopFullScreenSharePlatformSelector(ShareInfoParams shareInfoParams, FragmentActivity context, View anchorView, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        super(shareInfoParams,context, dismissListener, itemClickListener);
        mAnchorView = anchorView;
    }

    @Override
    public void show() {
        createShareWindowIfNeed();
        if (!mShareWindow.isShowing()) {
            mShareWindow.showAtLocation(mAnchorView, Gravity.BOTTOM, 0, 0);
        }
        showEnterAnimation();
    }

    private void showEnterAnimation() {
        if (enterAnimation == null)
            enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.share_popu_bottom_in);
        grid.setAnimation(enterAnimation);
        enterAnimation.start();
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
        super.release();
        mAnchorView = null;
        mShareWindow = null;
        grid = null;
        enterAnimation = null;
    }

    private void createShareWindowIfNeed() {
        if (mShareWindow != null)
            return;
        Context context = getContext();
        grid = createShareGridView(context, getItemClickListener());
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gridParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        grid.setLayoutParams(gridParams);

        mContainerView = new RelativeLayout(getContext());
        mContainerView.setBackgroundColor(getContext().getResources().getColor(R.color.share_socialize_black_trans));
        RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mContainerView.setLayoutParams(containerParams);
        mContainerView.addView(grid);
        mContainerView.setOnClickListener(this);

        mShareWindow = new PopupWindow(mContainerView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        grid.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mShareWindow.setOutsideTouchable(true);
        mShareWindow.setAnimationStyle(-1);
        mShareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (getDismissListener() != null)
                    getDismissListener().onDismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mContainerView) {
            dismiss();
        }
    }
}
