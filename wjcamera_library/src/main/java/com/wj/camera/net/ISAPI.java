package com.wj.camera.net;

import android.util.Log;

import androidx.annotation.IntRange;

import com.google.gson.Gson;
import com.wj.camera.WJCamera;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.callback.XmlCallback;
import com.wj.camera.config.WJDeviceSceneEnum;
import com.wj.camera.response.ResponseStatus;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.response.SceneResponse;
import com.wj.camera.response.TwoWayAudio;
import com.wj.camera.response.VideoConfig;
import com.wj.camera.response.ZoomResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.Call;
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
        setRtmp(deviceSerial, rtmpConfig, callback);
    }
    public void setRtmp(String deviceSerial, RtmpConfig rtmpConfig, JsonCallback<ResponseStatus> callback) {
        OkHttpUtils.getInstance().put(ApiNew.RTMP).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(rtmpConfig)).enqueue(new XmlCallback(callback));
    }

    public ResponseStatus setRtmp(String deviceSerial, RtmpConfig rtmpConfig) {

        try {
            Response execute = OkHttpUtils.getInstance().put(ApiNew.RTMP).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(rtmpConfig)).execute();
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
        OkHttpUtils.getInstance().put(ApiNew.Zoom).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(zoomResponse)).enqueue(new XmlCallback(null));
    }



    //获取RTMP配置
    public void getRTMP(String deviceSerial, JsonCallback<RtmpConfig> callback) {
        OkHttpUtils.getInstance().get(ApiNew.RTMP).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(callback));
    }

    //获取RTMP配置
    public void getRTMP(JsonCallback<RtmpConfig> callback) {
        OkHttpUtils.getInstance().get(ApiNew.RTMP).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(callback));

    }

    public void getAudio(JsonCallback<TwoWayAudio> callback) {
        OkHttpUtils.getInstance().get(ApiNew.Audio).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(callback));
    }

    public void setAuido(TwoWayAudio twoWayAudio, JsonCallback<ResponseStatus> callback) {
        OkHttpUtils.getInstance().put(ApiNew.Audio).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(twoWayAudio)).enqueue(new XmlCallback(callback));
    }

    public void setVolume(String deviceSerial, @IntRange(from = 0, to = 100) int volume) {
        setVolume(deviceSerial, volume, null);

    }

    public void setVolume(String deviceSerial, @IntRange(from = 0, to = 100) int volume, JsonCallback<ResponseStatus> callback) {
            getAudio(new JsonCallback<TwoWayAudio>() {
                @Override
                public void onSuccess(TwoWayAudio data) {
                    if (data != null) {
                        if (data.getTwoWayAudioChannel() != null) {
                            data.getTwoWayAudioChannel().setSpeakerVolume(String.valueOf(volume));
                            setAuido(data, callback);
                        }
                    }
                }
            });
    }

    //获取RTMP配置
    public RtmpConfig getRTMP(String device_serial) {

        try {
            Response execute=OkHttpUtils.getInstance().get(ApiNew.RTMP).addHeader("EZO-DeviceSerial", device_serial).execute();
            String string = execute.body().string();
            Log.i(TAG, "getRTMP: "+string);
            String xml = Objects.requireNonNull(string);
            String json = new XmlToJson.Builder(xml).build().toString();
            return mGson.fromJson(json, RtmpConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //获取当前设置 分辨率 码率相关设置
    public Call getVideoConfig(JsonCallback<VideoConfig> callback) {
       return OkHttpUtils.getInstance().get(ApiNew.setting101).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(callback));
    }

    //设置当前设置 分辨率 码率相关设置
    public Call setVideoConfig(VideoConfig videoConfig, JsonCallback<ResponseStatus> callback) {
       return OkHttpUtils.getInstance().put(ApiNew.setting101).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(videoConfig)).enqueue(new XmlCallback(callback));
    }
    public Call getZoom(JsonCallback<ZoomResponse> jsonCallback) {
       return getZoom(deviceSerial,jsonCallback);
    }

    public Call getZoom(String deviceSerial, JsonCallback<ZoomResponse> jsonCallback) {
        return OkHttpUtils.getInstance().get(ApiNew.Zoom).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(jsonCallback));
    }
    //设置码率
    public void setBitrate(String deviceSerial, String bitrate, JsonCallback<ResponseStatus> jsonCallback) {
        OkHttpUtils.getInstance().get(ApiNew.setting101).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(new JsonCallback<VideoConfig>() {
            @Override
            public void onSuccess(VideoConfig data) {
                if (data != null && data.getStreamingChannel() != null && data.getStreamingChannel().getVideo() != null) {
                    data.getStreamingChannel().getVideo().setVbrUpperCap(bitrate);
                    OkHttpUtils.getInstance().put(ApiNew.setting101).jsons(entityToXml(data)).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(jsonCallback));
                } else {
                    if (jsonCallback != null) {
                        jsonCallback.onError(1001, "设置码率失败");
                    }
                }
            }
        }));
    }
    //设置分辨率
    public void setResolution(String deviceSerial, String[] ratio, JsonCallback<ResponseStatus> jsonCallback) {
        OkHttpUtils.getInstance().get(ApiNew.setting101)
                .addHeader("EZO-DeviceSerial", deviceSerial)
                .enqueue(new XmlCallback(new JsonCallback<VideoConfig>() {
                    @Override
                    public void onSuccess(VideoConfig data) {
                        if (data != null && data.getStreamingChannel() != null && data.getStreamingChannel().getVideo() != null) {
                            data.getStreamingChannel().getVideo().setVideoResolutionHeight(ratio[1]);
                            data.getStreamingChannel().getVideo().setVideoResolutionWidth(ratio[0]);
                            JsonToXml xml = new JsonToXml.Builder(mGson.toJson(data))
                                    .build();
                            OkHttpUtils.getInstance().put(ApiNew.setting101).jsons(xml.toString())
                                    .addHeader("EZO-DeviceSerial", deviceSerial)
                                    .enqueue(new XmlCallback(jsonCallback));
                        } else {
                            if (jsonCallback != null) {
                                jsonCallback.onError(1001, "设置分辨率失败");
                            }
                        }
                    }
                }));
    }
    //获取配置信息
    public Call getDeviceConfig(String deviceSerial, JsonCallback<VideoConfig> jsonCallback) {
        return OkHttpUtils.getInstance().get(ApiNew.setting101)
                .addHeader("EZO-DeviceSerial", deviceSerial)
                .enqueue(new XmlCallback(jsonCallback));
    }

    public Call getScene(String deviceSerial, JsonCallback<SceneResponse> jsonCallback) {
       return OkHttpUtils.getInstance()
                .get(ApiNew.Scene)
                .addHeader("EZO-DeviceSerial", deviceSerial)
                .enqueue(new XmlCallback(jsonCallback));
    }

    //切换场景
    public Call setScene(String deviceSerial, WJDeviceSceneEnum indoor, JsonCallback<ResponseStatus> jsonCallback) {
        SceneResponse sceneResponse = new SceneResponse();
        sceneResponse.setMountingScenario(new SceneResponse.MountingScenarioDTO());
        sceneResponse.getMountingScenario().setMode(indoor.getScene());
        return OkHttpUtils.getInstance().put(ApiNew.Scene)
                .jsons(entityToXml(sceneResponse))
                .addHeader("EZO-DeviceSerial", deviceSerial)
                .enqueue(new XmlCallback(jsonCallback));
    }

    private String entityToXml(Object obj) {
        String jsonString = mGson.toJson(obj);
        JsonToXml xml = new JsonToXml.Builder(jsonString).build();
        return xml.toString();
    }


}
