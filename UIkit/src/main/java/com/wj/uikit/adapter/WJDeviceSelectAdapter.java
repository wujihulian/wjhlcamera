package com.wj.uikit.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.net.DeviceApi;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.uikit.R;
import com.wj.uikit.db.DeviceInfo;
import com.wj.uikit.pop.EditPop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FileName: DeviceSelectAdapter
 * Author: xiongxiang
 * Date: 2021/1/21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJDeviceSelectAdapter extends BaseRecyclerViewAdapter<DeviceInfo> {
    private int selectMax = 1;
    private HashMap<String, Boolean> mHashMap = new HashMap<>();

    public OnItemClickListener<DeviceInfo> mOnRemoveItemListener;

    public void setOnRemoveItemListener(OnItemClickListener<DeviceInfo> onRemoveItemListener) {
        mOnRemoveItemListener = onRemoveItemListener;
    }

    public OnItemClickListener<DeviceInfo> getOnRemoveItemListener() {
        return mOnRemoveItemListener;
    }

    public WJDeviceSelectAdapter() {

    }

    public void setSelectHashMap(HashMap<String, Boolean> hashMap) {
        mHashMap = hashMap;
    }

    public WJDeviceSelectAdapter(int selectMax) {
        this.selectMax = selectMax;
    }

    @Override
    public int inflaterLayout(int viewType) {
        return R.layout.wj_uikit_deviceselect_item;
    }

    @Override
    public void convert(ViewHolder holder, DeviceInfo data, int position) {


        if (data.deviceCamera == null) {
            holder.getView(R.id.device_location).setVisibility(View.GONE);
            holder.setText(R.id.device_name, data.device_serial+"");
        } else {
            holder.setText(R.id.device_name, data.deviceCamera.getDeviceName());
            String location;
            TextView textView = holder.getView(R.id.device_location);
            if (data.deviceCamera.getDeviceName().equals(data.deviceCamera.getChannelName())) {
                location = "未填写地点";
                textView.setTextColor(Color.parseColor("#999999"));

            } else {
                textView.setTextColor(Color.parseColor("#FF333333"));
                location = data.deviceCamera.getChannelName();
            }
            holder.setText(R.id.device_location, location + "");
            holder.getView(R.id.device_location).setVisibility(View.VISIBLE);
        }

        ImageView selcet_iv = holder.getView(R.id.selcet_iv);
        Boolean isSelcet = mHashMap.get(data.device_serial);
        if (isSelcet == null) {
            isSelcet = false;
            mHashMap.put(data.device_serial, isSelcet);
        }
        if (isSelcet) {
            //选中
            selcet_iv.setImageResource(R.mipmap.wj_uikit_select0);
        } else {
            selcet_iv.setImageResource(R.mipmap.wj_uikit_select1);
        }
        selcet_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHashMap.get(data.device_serial) == false) {
                    if (getAllSelect().size() >= selectMax) {
                        Toast.makeText(holder.getContext(), String.format("最多选择%s台设备", selectMax), Toast.LENGTH_LONG).show();
                        return;
                    }

                    //选中
                    selcet_iv.setImageResource(R.mipmap.wj_uikit_select0);
                    mHashMap.put(data.device_serial, true);
                } else {
                    mHashMap.put(data.device_serial, false);
                    selcet_iv.setImageResource(R.mipmap.wj_uikit_select1);
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
                        if (TextUtils.isEmpty(s)) {
                            Toast.makeText(holder.getContext(), "内容不能为空", Toast.LENGTH_LONG).show();
                            return;
                        }
                        DeviceApi.getInstance().cameraNameUpdate(data.device_serial, s, new JsonCallback<BaseDeviceResponse>() {
                            @Override
                            public void onSuccess(BaseDeviceResponse baseDeviceResponse) {
                                if (data.deviceCamera != null) {
                                    data.deviceCamera.setChannelName(s);
                                }
                                holder.setText(R.id.device_location, s);
                            }

                            @Override
                            public void onError(int code, String msg) {
                                Toast.makeText(holder.getContext(), msg + "", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.getContext(), WJDeviceDebugActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(WJDeviceConfig.DEVICE_INFO, data);
                intent.putExtras(extras);
                holder.getContext().startActivity(intent);
            }
        });*/
        holder.getView(R.id.iv_remove_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnRemoveItemListener() != null) {
                    getOnRemoveItemListener().onClick(data, position);
                }
            }
        });


    }


    public List<DeviceInfo> getAllSelect() {
        List<DeviceInfo> selectDatas = new ArrayList<>();
        List<DeviceInfo> allData = getAllData();
        for (int i = 0; i < allData.size(); i++) {
            DeviceInfo deviceInfo = allData.get(i);
            Boolean aBoolean = mHashMap.get(deviceInfo.device_serial);
            if (aBoolean) {
                selectDatas.add(deviceInfo);
            }
        }
        return selectDatas;
    }
}
