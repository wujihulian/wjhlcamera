package com.wj.uikit.pop;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;
import com.wj.uikit.R;
import com.wj.uikit.adapter.BaseRecyclerViewAdapter;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.adapter.ViewHolder;

/**
 * FileName: SelectPop
 * Author: xiongxiang
 * Date: 2021/3/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class SelectPop extends BottomPopupView {
    private RecyclerView mRecyclerView;
    public OnItemClickListener<String> mListener;
    public String[] mStrings = {"自动", "手动"};

    public SelectPop(@NonNull Context context) {
        super(context);
    }
    public SelectPop(@NonNull Context context, String[] strings) {
        super(context);
        this.mStrings = strings;
    }

    public void setListener(OnItemClickListener<String> listener) {
        mListener = listener;
    }

    public OnItemClickListener<String> getListener() {
        return mListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.wj_uikit_select_pop;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SelectAdapter adapter = new SelectAdapter();
        mRecyclerView.setAdapter(adapter);
    }


    public class SelectAdapter extends BaseRecyclerViewAdapter<String> {


        @Override
        public void convert(ViewHolder holder, String data, int position) {
            holder.setText(R.id.tv, data);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getListener() != null) {
                        getListener().onClick(data, position);
                    }
                    dismiss();
                }
            });
        }

        @Override
        public int inflaterLayout(int viewType) {
            return R.layout.wj_select_item;
        }

        @Override
        public int getItemCount() {
            return mStrings == null ? 0 : mStrings.length;
        }

        @Override
        public String getData(int position) {
            return mStrings[position];
        }
    }
}
