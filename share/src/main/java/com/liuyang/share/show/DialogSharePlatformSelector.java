package com.liuyang.share.show;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.liuyang.share.R;

/**
 * 对话框默认实现
 */
public class DialogSharePlatformSelector extends BaseSharePlatformSelector {

    private Dialog mShareDialog;

    public DialogSharePlatformSelector(FragmentActivity context, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        super(context, dismissListener, itemClickListener);

    }

    @Override
    public void show() {
        if (mShareDialog == null) {
            mShareDialog = createDialog();
            mShareDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (getDismissListener() != null)
                        getDismissListener().onDismiss();
                }
            });
        }
        mShareDialog.show();
    }

    public Dialog createDialog(){
        GridView grid = createShareGridView(getContext(), mItemClickListener);
        LinearLayout linearLayout = (LinearLayout)View.inflate(getContext(),R.layout.share_socialize_shareboard_main,null);
        linearLayout.addView(grid,0);
        Dialog dialog = new Dialog(getContext(), R.style.Share_Dialog);
        dialog.setContentView(linearLayout);
        Window window = dialog.getWindow();
        DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WindowManager.LayoutParams lp = window.getAttributes();
        int screenwidth = metrics.widthPixels;
        lp.width = screenwidth;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.ShareDialogAnimationSlidBottom);
        window.setGravity(Gravity.BOTTOM);
        Button btCancel = (Button) linearLayout.findViewById(R.id.bt_share_socialize_shareboard_main);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void dismiss() {
        if (mShareDialog != null)
            mShareDialog.dismiss();
    }

    @Override
    public void release() {
        dismiss();
        mShareDialog = null;
        super.release();
    }
}
