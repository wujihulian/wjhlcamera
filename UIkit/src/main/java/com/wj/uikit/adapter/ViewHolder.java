package com.wj.uikit.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 设置TextView文本
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            if (TextUtils.isEmpty(text)) {
                tv.setText("");
            } else {
                tv.setText(text);
            }
        }
        return this;
    }

    public ViewHolder setText(int viewId, int id) {
        String string = itemView.getContext().getResources().getString(id);
        setText(viewId, string);
        return this;
    }
    /**
     * 通过id获取view
     */
    public <T extends View> T getView(int viewId) {
        // 先从缓存中找
        View view = mViews.get(viewId);
        if (view == null) {
            // 直接从ItemView中找
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


}
