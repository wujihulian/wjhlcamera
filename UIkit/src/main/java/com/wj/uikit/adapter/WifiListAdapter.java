package com.wj.uikit.adapter;

import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lxj.xpopup.XPopup;
import com.wj.uikit.R;
import com.wj.uikit.pop.EditPop;

/**
 * FileName: WifiListAdapter
 * Author: xiongxiang
 * Date: 2021/1/21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WifiListAdapter extends BaseRecyclerViewAdapter<ScanResult> {

    private EditPop mEditPop;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public int inflaterLayout(int viewType) {
        return R.layout.wj_uikit_wifi_item;
    }

    private static final String TAG = "WifiListAdapter";

    @Override
    public void convert(ViewHolder holder, ScanResult data, int position) {
        if (TextUtils.isEmpty(data.SSID)) {
            holder.setText(R.id.ssid_tv, "隐藏的网络");
        } else {
            holder.setText(R.id.ssid_tv, data.SSID);
        }
        holder.getView(R.id.wj_wifi_lock).setVisibility(View.VISIBLE);
        String capabilities = data.capabilities.trim();
        if (capabilities != null && (capabilities.contains("WPA2") || capabilities.contains("WPA"))) {
            holder.getView(R.id.wj_wifi_lock).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.wj_wifi_lock).setVisibility(View.GONE);
        }
        int abs = Math.abs(data.level);
        ImageView wifiSign = holder.getView(R.id.wj_wifi_sign_iv);
        if (abs < 50) {
            wifiSign.setImageResource(R.mipmap.wj_uikit_wifi_sign3);
        } else if (abs < 70) {
            wifiSign.setImageResource(R.mipmap.wj_uikit_wifi_sign2);
        } else {
            wifiSign.setImageResource(R.mipmap.wj_uikit_wifi_sign1);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capabilities != null && (capabilities.contains("WPA2") || capabilities.contains("WPA"))) {
                    if (mEditPop == null) {
                        mEditPop = new EditPop(holder.getContext(), "设置wifi密码",8);
                        new XPopup.Builder(holder.getContext()).asCustom(mEditPop);
                    }
                    mEditPop.setOnConfirmListener(new OnItemClickListener<String>() {
                        @Override
                        public void onClick(String s, int p) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onClick(s, position);
                            }
                        }
                    });
                    mEditPop.show();
                } else {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick("", position);
                    }
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener<String> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
