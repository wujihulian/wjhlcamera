package com.wj.camera.net;

import androidx.annotation.IntRange;

import com.google.gson.Gson;
import com.wj.camera.WJCamera;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.callback.XmlCallback;
import com.wj.camera.config.WJDeviceConfig;
import com.wj.camera.response.ResponseStatus;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.response.TwoWayAudio;
import com.wj.camera.response.VideoConfig;
import com.wj.camera.response.ZoomResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * FileName: ISAPI
 * Author: xiongxiang
 * Date: 2021/1/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class ISAPI {

    private static final String TAG = "ISAPI";
    public static ISAPI isapi;
    private OkHttpClient mClient;
    private String deviceSerial;
    private Gson mGson;

    public static ISAPI getInstance() {
        if (isapi == null) {
            isapi = new ISAPI();
        }
        return isapi;
    }

    /**
     * 配置设备
     * @param deviceSerial 设备 序列号
     */
    public void config(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    private ISAPI() {
        mGson = new Gson();
        mClient = WJCamera.getInstance().getClient();
    }

    public void setRtmp(RtmpConfig rtmpConfig, JsonCallback<ResponseStatus> callback) {
        setRtmp(deviceSerial,rtmpConfig,callback);
    }
    public void setRtmp(String deviceSerial,RtmpConfig rtmpConfig, JsonCallback<ResponseStatus> callback) {
        RequestBody xmlRequestBody = createXmlRequestBody(rtmpConfig);
        put(xmlRequestBody,deviceSerial, Api.RTMP, callback);
    }

    public ResponseStatus setRtmp(String device_serial, RtmpConfig rtmpConfig) {
        RequestBody xmlRequestBody = createXmlRequestBody(rtmpConfig);
        Request request = new Request.Builder()
                .url(Api.RTMP)
                .put(xmlRequestBody)
                .addHeader("EZO-AccessToken", WJCamera.getInstance().getAccessToken())
                .addHeader("EZO-DeviceSerial", device_serial)
                .build();
        try {
            Response execute = mClient.newCall(request).execute();
            String string = execute.body().string();
            String xml = Objects.requireNonNull(string);
            String json = new XmlToJson.Builder(xml).build().toString();
            return mGson.fromJson(json, ResponseStatus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void zoom(int zoom) {
        ZoomResponse zoomResponse = new ZoomResponse();
        zoomResponse.setZoom(new ZoomResponse.ZoomDTO());
        zoomResponse.getZoom().setRatio(zoom);
        RequestBody xmlRequestBody = createXmlRequestBody(zoomResponse);
        put(xmlRequestBody, Api.Zoom, null);
    }

    public void setPTData(String ptzData, JsonCallback callback) {
        RequestBody xmlRequestBody = createXmlRequestBody(ptzData);

        put(xmlRequestBody, Api.PZTData, callback);
    }

    //获取RTMP配置
    public void getRTMP(String deviceSerial,JsonCallback<RtmpConfig> callback) {
        get(Api.RTMP,deviceSerial, callback);
    }

    //获取RTMP配置
    public void getRTMP(JsonCallback<RtmpConfig> callback) {
        get(Api.RTMP, callback);
    }

    public void getAudio(JsonCallback<TwoWayAudio> callback) {
        get(Api.Audio, callback);
    }

    public void setAuido(TwoWayAudio twoWayAudio, JsonCallback<ResponseStatus> callback) {
        RequestBody xmlRequestBody = createXmlRequestBody(twoWayAudio);
        put(xmlRequestBody, Api.Audio, callback);
    }
    public void setVolume(String deviceSerial,@IntRange(from = 0,to = 100) int volume){
        setVolume(deviceSerial,volume,null);

    }
    public void setVolume(String deviceSerial,@IntRange(from = 0,to = 100)int volume, JsonCallback<ResponseStatus> callback){

        get(Api.Audio, deviceSerial, new JsonCallback<TwoWayAudio>() {
            @Override
            public void onSuccess(TwoWayAudio data) {
                if (data!=null){
                    if (data.getTwoWayAudioChannel()!=null) {
                        data.getTwoWayAudioChannel().setSpeakerVolume(String.valueOf(volume));
                        setAuido(data,callback);
                    }
                }
            }
        });

    }

    //获取RTMP配置
    public RtmpConfig getRTMP(String device_serial) {
        Request request = new Request.Builder()
                .url(Api.RTMP)
                .addHeader("EZO-AccessToken", WJCamera.getInstance().getAccessToken())
                .addHeader("EZO-DeviceSerial", device_serial == null ? deviceSerial : device_serial)
                .build();
        try {
            Response execute = mClient.newCall(request).execute();
            String string = execute.body().string();
            String xml = Objects.requireNonNull(string);
            String json = new XmlToJson.Builder(xml).build().toString();
            return mGson.fromJson(json, RtmpConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //获取RTMP配置
    public RtmpConfig getRTMP(String device_serial, OkHttpClient client) {
        Request request = new Request.Builder()
                .url(Api.RTMP)
                .addHeader("EZO-AccessToken", WJCamera.getInstance().getAccessToken())
                .addHeader("EZO-DeviceSerial", device_serial == null ? deviceSerial : device_serial)
                .build();
        try {
            Response execute = client.newCall(request).execute();
            String string = execute.body().string();
            String xml = Objects.requireNonNull(string);
            String json = new XmlToJson.Builder(xml).build().toString();
            return mGson.fromJson(json, RtmpConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //获取当前设置 分辨率 码率相关设置
    public void getVideoConfig(JsonCallback<VideoConfig> callback) {
        get(Api.setting101, callback);
    }

    //设置当前设置 分辨率 码率相关设置
    public void setVideoConfig(VideoConfig videoConfig, JsonCallback<ResponseStatus> callback) {
        RequestBody xmlRequestBody = createXmlRequestBody(videoConfig);
        put(xmlRequestBody, Api.setting101, callback);
    }

    private RequestBody createXmlRequestBody(String xml) {
        MediaType JSON = MediaType.parse("application/xml;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, xml);
        return requestBody;
    }

    private RequestBody createXmlRequestBody(Object object) {
        MediaType JSON = MediaType.parse("application/xml;charset=utf-8");
        String jsonString = mGson.toJson(object);
        JsonToXml xml = new JsonToXml.Builder(jsonString)
                .build();
        RequestBody requestBody = RequestBody.create(JSON, xml.toString());
        return requestBody;
    }


    private void get(String url, JsonCallback callback) {
        get(url, deviceSerial, callback);
    }

    private void get(String url, String deviceSerial, JsonCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("EZO-AccessToken", WJCamera.getInstance().getAccessToken())
                .addHeader("EZO-DeviceSerial", deviceSerial)
                .build();
        mClient.newCall(request).enqueue(new XmlCallback(callback));
    }

    public <T> T get(String url, Type type, String device_serial) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("EZO-AccessToken", WJCamera.getInstance().getAccessToken())
                .addHeader("EZO-DeviceSerial", device_serial == null ? deviceSerial : device_serial)
                .build();
        try {
            Response execute = mClient.newCall(request).execute();
            String string = execute.body().string();
            String xml = Objects.requireNonNull(string);
            return mGson.fromJson(xml, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void put(RequestBody requestBody, String url, JsonCallback callback) {
        put(requestBody, url, deviceSerial, callback);
    }

    private void put(RequestBody requestBody, String url, String deviceSerial, JsonCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("EZO-AccessToken", WJCamera.getInstance().getAccessToken())
                .addHeader("EZO-DeviceSerial", deviceSerial)
                .build();
        mClient.newCall(request).enqueue(new XmlCallback(callback));
    }


    public void getZoom(JsonCallback<ZoomResponse> jsonCallback) {
        get(Api.Zoom, jsonCallback);
    }

    public void getZoom(String deviceSerial, JsonCallback<ZoomResponse> jsonCallback) {
        get(Api.Zoom, deviceSerial, jsonCallback);
    }

    //设置码率
    public void setBitrate(String deviceSerial ,String bitrate,JsonCallback<ResponseStatus> jsonCallback){
        get(Api.setting101,deviceSerial, new JsonCallback<VideoConfig>() {
            @Override
            public void onSuccess(VideoConfig data) {
                if (data!=null && data.getStreamingChannel()!=null && data.getStreamingChannel().getVideo()!=null){
                            data.getStreamingChannel().getVideo().setVbrUpperCap(bitrate);
                            RequestBody xmlRequestBody = createXmlRequestBody(data);
                            put(xmlRequestBody, Api.setting101,deviceSerial, jsonCallback);
                }else {
                    if (jsonCallback!=null) {
                        jsonCallback.onError(1001,"设置码率失败");
                    }
                }
            }
        });
    }

    //设置分辨率
    public void setResolution(String deviceSerial ,String[] ratio,JsonCallback<ResponseStatus> jsonCallback){
        get(Api.setting101,deviceSerial, new JsonCallback<VideoConfig>() {
            @Override
            public void onSuccess(VideoConfig data) {
                if (data!=null && data.getStreamingChannel()!=null && data.getStreamingChannel().getVideo()!=null){
                    data.getStreamingChannel().getVideo().setVideoResolutionHeight(ratio[0]);
                    data.getStreamingChannel().getVideo().setVideoResolutionWidth(ratio[1]);
                    RequestBody xmlRequestBody = createXmlRequestBody(data);
                    put(xmlRequestBody, Api.setting101,deviceSerial, jsonCallback);
                }else {
                    if (jsonCallback!=null) {
                        jsonCallback.onError(1001,"设置分辨率失败");
                    }
                }
            }
        });
    }

    //获取配置信息
    public void getDeviceConfig(String deviceSerial, JsonCallback<VideoConfig> jsonCallback){
        get(Api.setting101,deviceSerial, jsonCallback);
    }
}
