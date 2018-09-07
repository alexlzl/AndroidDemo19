package com.liuyang.share.show;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.liuyang.share.R;
import com.liuyang.share.ShareData;
import com.liuyang.share.helper.ConfigHelper;
import com.liuyang.share.model.ShareTarget;


/**
 * 默认实现
 */
public abstract class BaseSharePlatformSelector {

    private FragmentActivity mContext;
    private OnShareSelectorDismissListener mDismissListener;
    public AdapterView.OnItemClickListener mItemClickListener;
    private static ShareData mShareInfoParams;

    public BaseSharePlatformSelector(ShareData shareInfoParams, FragmentActivity context, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        mContext = context;
        mDismissListener = dismissListener;
        mItemClickListener = itemClickListener;
        mShareInfoParams = shareInfoParams;
    }

    public abstract void show();

    public abstract void dismiss();

    public void release() {
        mContext = null;
        mDismissListener = null;
        mItemClickListener = null;
    }

    protected static GridView createShareGridView(final Context context, AdapterView.OnItemClickListener onItemClickListener) {
        GridView grid = new GridView(context);
        ListAdapter adapter = new ArrayAdapter<ShareTarget>(context, 0, ConfigHelper.getShareTargets(mShareInfoParams)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_socialize_shareboard_item, parent, false);
                view.setBackgroundDrawable(null);
                ImageView image = (ImageView) view.findViewById(R.id.bili_socialize_shareboard_image);
                TextView platform = (TextView) view.findViewById(R.id.bili_socialize_shareboard_pltform_name);

                ShareTarget target = getItem(position);
                image.setImageResource(target.iconId);
                platform.setText(target.titleId);
                return view;
            }
        };
        grid.setNumColumns(mShareInfoParams.getPlatform().length);
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid.setColumnWidth(context.getResources().getDimensionPixelSize(R.dimen.share_socialize_shareboard_size));
        grid.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        grid.setSelector(R.drawable.share_socialize_selector_item_background);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(onItemClickListener);
        return grid;
    }



    public FragmentActivity getContext() {
        return mContext;
    }

    public AdapterView.OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public OnShareSelectorDismissListener getDismissListener() {
        return mDismissListener;
    }

    public interface OnShareSelectorDismissListener {
        void onDismiss();
    }

}
