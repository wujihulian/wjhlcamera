package com.wj.uikit.pop;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.wj.camera.net.ISAPI;
import com.wj.camera.uitl.DPUtil;
import com.wj.uikit.R;
import com.wj.uikit.adapter.BaseRecyclerViewAdapter;
import com.wj.uikit.adapter.OnItemClickListener;
import com.wj.uikit.adapter.ViewHolder;
import com.wj.uikit.status.StatusBarUtil;
import com.wj.uikit.uitl.OnControlClickListener;

/**
 * FileName: FocusSelectPop
 * Author: xiongxiang
 * Date: 2021/3/11
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class FocusSelectPop extends BottomPopupView {


    private RecyclerView mRecyclerView;
    public OnItemClickListener<String> mListener;
    public int atIndex = -1;
    private FocusSelectAdapter mSelectAdapter;
    private String device_serial;

    public FocusSelectPop(@NonNull Context context) {
        super(context);
    }

    public void setListener(OnItemClickListener<String> listener) {
        mListener = listener;
    }

    public void setAtIndex(int atIndex) {
        this.atIndex = atIndex;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.wj_focus_select_pop;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ViewGroup.LayoutParams layoutParams = bottomPopupContainer.getLayoutParams();
      //  layoutParams.height= XPopupUtils.getWindowHeight(getContext())- DPUtil.dip2px(getContext(),259) - XPopupUtils.getStatusBarHeight();
        bottomPopupContainer.setLayoutParams(layoutParams);
        mRecyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mSelectAdapter = new FocusSelectAdapter();
        mRecyclerView.setAdapter(mSelectAdapter);

        findViewById(R.id.wj_tv_left).setOnClickListener(new OnControlClickListener() {
            @Override
            public void onViewClick(View view) {
                ISAPI.getInstance().ZOOMCRTL(device_serial, "tele");
            }
        });
        findViewById(R.id.wj_tv_right).setOnClickListener(new OnControlClickListener() {

            @Override
            public void onViewClick(View view) {
                ISAPI.getInstance().ZOOMCRTL(device_serial, "wide");
            }
        });


    }

    public void setDeviceSerial(String device_serial) {
        this.device_serial = device_serial;
    }

    public class FocusSelectAdapter extends BaseRecyclerViewAdapter<String> {


        @Override
        public String getData(int position) {
            return (position + 1) + "x";
        }

        @Override
        public void convert(ViewHolder holder, String data, int position) {
            TextView tv = holder.getView(R.id.tv);
            if (atIndex == position) {
                holder.getView(R.id.tv).setBackgroundResource(R.drawable.wj_circle_blue_n);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.getView(R.id.tv).setBackgroundResource(0);
                tv.setTextColor(Color.parseColor("#333333"));
            }
            holder.setText(R.id.tv, data);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    atIndex = position;
                    if (mListener != null) {
                        mListener.onClick(data, position);
                    }
                    notifyDataSetChanged();
                    dismiss();

                }
            });
        }

        @Override
        public int inflaterLayout(int viewType) {
            return R.layout.wj_focus_select_item;
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

}
