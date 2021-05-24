package com.wj.camera.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wj.camera.WJCamera;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.callback.StringCallbck;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.response.CheckDevcieUpdate;
import com.wj.camera.response.DeviceCameraData;
import com.wj.camera.response.DeviceData;
import com.wj.camera.response.DeviceUpdateStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * FileName: DeviceApi
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class DeviceApi {
    public static DeviceApi mDeviceApi;

    private final Gson mGson;

    public static DeviceApi getInstance() {
        if (mDeviceApi == null) {
            mDeviceApi = new DeviceApi();
        }
        return mDeviceApi;
    }

    private DeviceApi() {
        mGson = new Gson();
    }

    public OkHttpClient getClient(){
        return WJCamera.getInstance().getClient();
    }

    /**
     * 获取设备信息
     * @param deviceSerial 设备序列号
     * @param callback
     */
    public void deviceInfo(String deviceSerial, JsonCallback<BaseDeviceResponse<DeviceData>> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceInfo)
                .post(formBody)
                .build();
        getClient().newCall(request).enqueue(new StringCallbck(callback));
    }

    private static final String TAG = "DeviceApi";

    /**
     * 获取设备信息
     */
    public BaseDeviceResponse<DeviceData> deviceInfo(String deviceSerial) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceInfo)
                .post(formBody)
                .build();
        try {
            Response execute = getClient().newCall(request).execute();
            String string = execute.body().string();

            Type jsonType = new TypeToken<BaseDeviceResponse<DeviceData>>() {
            }.getType();
            BaseDeviceResponse<DeviceData> baseDeviceResponse = mGson.fromJson(string, jsonType);
            return baseDeviceResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultResponse();
    }

    /**
     * 获取设备通道信息
     * @param deviceSerial
     * @return
     */
    public BaseDeviceResponse<List<DeviceCameraData>> deviceCameraInfo(String deviceSerial) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceCameraInfo)
                .post(formBody)
                .build();
        try {
            Response execute = getClient().newCall(request).execute();
            String string = execute.body().string();
            Type jsonType = new TypeToken<BaseDeviceResponse<List<DeviceCameraData>>>() {
            }.getType();
            BaseDeviceResponse<List<DeviceCameraData>> baseDeviceResponse = mGson.fromJson(string, jsonType);
            return baseDeviceResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultResponse();
    }

    /**
     * 修改通道名称
     * @param deviceSerial
     * @param jsonCallback
     */
    public void cameraNameUpdate(String deviceSerial, String cameraName, JsonCallback<BaseDeviceResponse> jsonCallback) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        builder.addEncoded("name", cameraName);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.CameraNameUpdate)
                .post(formBody)
                .build();
        getClient().newCall(request).enqueue(new StringCallbck(jsonCallback));
    }


    /**
     * 设置设备名称
     * @param deviceSerial
     * @param deviceName
     * @param callback
     */
    public void setDeviceName(String deviceSerial, String deviceName, JsonCallback<BaseDeviceResponse> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        builder.addEncoded("deviceName", deviceName);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceNameUpdate)
                .post(formBody)
                .build();
        getClient().newCall(request).enqueue(new StringCallbck(callback));
    }

    /**
     * 添加设备
     * @param deviceSerial 设备序列号
     * @param validateCode 设备验证码
     * @param callback
     */
    public void addDevie(String deviceSerial, String validateCode, JsonCallback<BaseDeviceResponse> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        builder.addEncoded("validateCode", validateCode);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceAdd)
                .post(formBody)
                .build();
        getClient().newCall(request).enqueue(new StringCallbck(callback));
    }

    /**
     * 删除设备
     * @param deviceSerial
     * @param callback
     */
    public void deleteDevice(String deviceSerial, JsonCallback<BaseDeviceResponse> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceDelete)
                .post(formBody)
                .build();
        getClient().newCall(request).enqueue(new StringCallbck(callback));
    }

    //检查设备
    public BaseDeviceResponse<CheckDevcieUpdate> checkDeviceUpdate(String deviceSerial) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.CheckDeviceUpdate)
                .post(formBody)
                .build();
        try {
            Response execute = getClient().newCall(request).execute();
            String string = execute.body().string();
            Type jsonType = new TypeToken<BaseDeviceResponse<CheckDevcieUpdate>>() {
            }.getType();
            BaseDeviceResponse<CheckDevcieUpdate> baseDeviceResponse = mGson.fromJson(string, jsonType);
            return baseDeviceResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultResponse();
    }

    //设备升级
    public BaseDeviceResponse deviceUpdate(String deviceSerial) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceUpdate)
                .post(formBody)
                .build();
        try {
            Response execute = getClient().newCall(request).execute();
            String string = execute.body().string();
            BaseDeviceResponse baseDeviceResponse = mGson.fromJson(string, BaseDeviceResponse.class);
            return baseDeviceResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultResponse();
    }

    public BaseDeviceResponse<DeviceUpdateStatus> deviceUpdateStatus(String deviceSerial) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("accessToken", WJCamera.getInstance().getAccessToken());
        builder.addEncoded("deviceSerial", deviceSerial);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.DeviceUpdateStatus)
                .post(formBody)
                .build();
        try {
            Response execute = getClient().newCall(request).execute();
            String string = execute.body().string();
            Type jsonType = new TypeToken<BaseDeviceResponse<DeviceUpdateStatus>>() {
            }.getType();
            BaseDeviceResponse<DeviceUpdateStatus> baseDeviceResponse = mGson.fromJson(string, jsonType);
            return baseDeviceResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return defaultResponse();
    }

    public BaseDeviceResponse defaultResponse() {
        BaseDeviceResponse<Object> defaultResponse = new BaseDeviceResponse<>();
        defaultResponse.setMsg("解析失败");
        defaultResponse.setCode(0);
        return defaultResponse;
    }


}
