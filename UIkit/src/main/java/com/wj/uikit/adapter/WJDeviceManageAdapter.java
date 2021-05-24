package com.wj.uikit.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.view.WJDeviceConfig;
import com.wj.uikit.R;
import com.wj.uikit.WJDeviceDebugActivity;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.pop.EditPop;

/**
 * FileName: DeviceSelectAdapter
 * Author: xiongxiang
 * Date: 2021/1/21
 * Description: 设备 管理
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDeviceManageAdapter extends BaseRecyclerViewAdapter<DeviceInfo> {
    public OnItemClickListener<DeviceInfo> mOnRemoveItemListener;

    public void setOnRemoveItemListener(OnItemClickListener<DeviceInfo> onRemoveItemListener) {
        mOnRemoveItemListener = onRemoveItemListener;
    }

    public OnItemClickListener<DeviceInfo> getOnRemoveItemListener() {
        return mOnRemoveItemListener;
    }

    @Override
    public int inflaterLayout(int viewType) {
        return R.layout.wj_uikit_device_manage_item;
    }


    @Override
    public void convert(ViewHolder holder, DeviceInfo data, int position) {

        if (data.deviceCamera == null) {
            holder.getView(R.id.device_location).setVisibility(View.GONE);
            holder.setText(R.id.device_name, data.device_serial+"");
        } else {
            holder.setText(R.id.device_name, data.deviceCamera.getDeviceName());
            String  location;
            TextView textView = holder.getView(R.id.device_location);
            if (data.deviceCamera.getDeviceName().equals(data.deviceCamera.getChannelName())){
                location="未填写地点";
                textView.setTextColor(Color.parseColor("#999999"));

            }else {
                textView.setTextColor(Color.parseColor("#FF333333"));
                location=data.deviceCamera.getChannelName();
            }
            holder.setText(R.id.device_location, location+"");
            holder.getView(R.id.device_location).setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.getContext(), WJDeviceDebugActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(WJDeviceConfig.DEVICE_INFO, data);
                intent.putExtras(extras);
                holder.getContext().startActivity(intent);
            }
        });

        holder.getView(R.id.iv_remove_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnRemoveItemListener()!=null){
                    getOnRemoveItemListener().onClick(data,position);
                }
            }
        });

        holder.getView(R.id.edit_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPop popupView = new EditPop(holder.getContext(), "输入地点");
                new XPopup.Builder(holder.getContext()).asCustom(popupView).show();
                popupView.setOnConfirmListener(new OnItemClickListener<String>() {
                    @Override
                    public void onClick(String s, int position) {
                        DeviceApi.getInstance().cameraNameUpdate(data.device_serial, s, new JsonCallback<BaseDeviceResponse>() {
                            @Override
                            public void onSuccess(BaseDeviceResponse baseDeviceResponse) {
                                if (data.deviceCamera!=null){
                                    data.deviceCamera.setChannelName(s);
                                }
                                holder.setText(R.id.device_location, s);
                            }

                            @Override
                            public void onError(int code, String msg) {
                                Toast.makeText(holder.getContext(),msg+"",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }
}
