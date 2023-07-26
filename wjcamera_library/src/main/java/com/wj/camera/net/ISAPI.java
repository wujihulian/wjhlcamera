package com.wj.camera.net;

import android.util.Log;

import androidx.annotation.IntRange;

import com.google.gson.Gson;
import com.wj.camera.WJCamera;
import com.wj.camera.callback.JsonCallback;
import com.wj.camera.callback.XmlCallback;
import com.wj.camera.config.WJDeviceSceneEnum;
import com.wj.camera.request.XML;
import com.wj.camera.response.FOCUSCTRL;
import com.wj.camera.response.FocusEntity;
import com.wj.camera.response.NetworkInterface;
import com.wj.camera.response.ResponseStatus;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.response.SceneResponse;
import com.wj.camera.response.TwoWayAudio;
import com.wj.camera.response.VideoConfig;
import com.wj.camera.response.WirelessServer;
import com.wj.camera.response.ZOOMCTRL;
import com.wj.camera.response.ZoomResponse;
import com.wj.camera.uitl.WJLogUitl;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
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
    private String devIndex="";
    private Gson mGson;

    public static ISAPI getInstance() {
        if (isapi == null) {
            isapi = new ISAPI();
        }
        return isapi;
    }

    public String getDevIndex() {
        return devIndex;
    }

    public void setDevIndex(String devIndex) {
        this.devIndex = devIndex;
    }

    /**
     * 配置设备
     *
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

    public synchronized ResponseStatus setRtmp(String deviceSerial, RtmpConfig rtmpConfig) {

        try {
            Response execute = OkHttpUtils.getInstance().put(ApiNew.RTMP).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(rtmpConfig)).execute();
            if (execute == null || execute.body() == null) {
                return null;
            }
            String string = execute.body().string();
            String xml = Objects.requireNonNull(string);
            String json = new XmlToJson.Builder(xml).build().toString();
            return new Gson().fromJson(json, ResponseStatus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void zoom(int zoom,String devIndex) {
        ZoomResponse zoomResponse = new ZoomResponse();
        zoomResponse.setZoom(new ZoomResponse.ZoomDTO());
        zoomResponse.getZoom().setRatio(zoom);
        OkHttpUtils.getInstance().put(String.format("%s?devIndex=%s", ApiNew.Zoom, devIndex)).addHeader("EZO-DeviceSerial", devIndex).jsons(entityToXml(zoomResponse)).enqueue(new XmlCallback(null));
    }


    //获取RTMP配置
    public void getRTMP(String deviceSerial, JsonCallback<RtmpConfig> callback) {
        OkHttpUtils.getInstance().get(String.format("%s?devIndex=%s", ApiNew.RTMP, devIndex)).addHeader("EZO-DeviceSerial", devIndex).enqueue(new XmlCallback(callback));
    }

    //获取RTMP配置
    public void getRTMP(JsonCallback<RtmpConfig> callback) {
        OkHttpUtils.getInstance().get(ApiNew.RTMP).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(callback));
    }

    public void getAudio(JsonCallback<TwoWayAudio> callback) {
        OkHttpUtils.getInstance().get(String.format("%s?devIndex=%s", ApiNew.Audio, devIndex)).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(callback));
    }

    public void getAudio(String deviceSerial, JsonCallback<TwoWayAudio> callback) {
//        OkHttpUtils.getInstance().get(ApiNew.Audio).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(callback));
    }


    public void setAuido(TwoWayAudio twoWayAudio, JsonCallback<ResponseStatus> callback) {
        OkHttpUtils.getInstance().put(String.format("%s?devIndex=%s", ApiNew.Audio, devIndex)).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(twoWayAudio)).enqueue(new XmlCallback(callback));
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
                } else {
                    if (callback != null) {
                        callback.onError(0, "设置失败");
                    }
                }
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                if (callback != null) {
                    callback.onError(0, "设置失败");
                }
            }
        });
    }

    //获取RTMP配置
    public synchronized RtmpConfig getRTMP(String device_serial) {
        try {
            Response execute = OkHttpUtils.getInstance().get(String.format("%s?devIndex=%s", ApiNew.RTMP, devIndex)).execute();
            if (execute == null || execute.body() == null) {
                return null;
            }
            String string = execute.body().string();
            System.out.println("execute-->"+string);
            String xml = Objects.requireNonNull(string);
            String json = new XmlToJson.Builder(xml).build().toString();
            return new Gson().fromJson(json, RtmpConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //获取当前设置 分辨率 码率相关设置
    public Call getVideoConfig(JsonCallback<VideoConfig> callback) {
        return OkHttpUtils.getInstance().get(String.format("%s?devIndex=%s", ApiNew.setting101, devIndex)).addHeader("EZO-DeviceSerial", devIndex).enqueue(new XmlCallback(callback));
    }

    //设置当前设置 分辨率 码率相关设置
    public Call setVideoConfig(VideoConfig videoConfig, JsonCallback<ResponseStatus> callback) {
        return OkHttpUtils.getInstance().put(String.format("%s?devIndex=%s", ApiNew.setting101, devIndex)).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(videoConfig)).enqueue(new XmlCallback(callback));
    }

    public Call getZoom(JsonCallback<ZoomResponse> jsonCallback) {
        return getZoom(deviceSerial, jsonCallback);
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

    //聚焦
    public void focus(String deviceSerial, boolean direction) {
        ArrayList<String> objects = new ArrayList<>();
        if (!direction) {
            objects.add("-60");
        } else {
            objects.add("60");
        }
        objects.add("0");
        Observable<String> listObservable = Observable.fromIterable(objects);
        Observable<Long> timerObservable = Observable.interval(100, TimeUnit.MILLISECONDS);
        Observable.zip(listObservable, timerObservable, new BiFunction<String, Long, String>() {
            @Override
            public String apply(String s, Long aLong) throws Exception {
                FocusEntity focusEntity = new FocusEntity();
                focusEntity.setFocusData(new FocusEntity.FocusDataDTO());
                focusEntity.getFocusData().setFocus(s);
                OkHttpUtils.getInstance().put(ApiNew.focus).jsons(entityToXml(focusEntity)).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (!s.equals("0")) {
                            FocusEntity entity = new FocusEntity();
                            focusEntity.setFocusData(new FocusEntity.FocusDataDTO());
                            focusEntity.getFocusData().setFocus("0");
                            OkHttpUtils.getInstance().put(ApiNew.focus).jsons(entityToXml(entity)).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(null);
                        }
                    }
                });
                return s;
            }
        }).subscribe();
    }

    //调焦
    public void pztData(String deviceSerial, boolean direction) {
        ArrayList<String> objects = new ArrayList<>();
        if (!direction) {
            objects.add(XML.PTZDATA_60_F);
        } else {
            objects.add(XML.PTZDATA_60_Z);
        }
        objects.add(XML.PTZDATA_0);
        Observable<String> listObservable = Observable.fromIterable(objects);
        Observable<Long> timerObservable = Observable.interval(100, TimeUnit.MILLISECONDS);
        Observable.zip(listObservable, timerObservable, new BiFunction<String, Long, String>() {
            @Override
            public String apply(String s, Long aLong) throws Exception {
                OkHttpUtils.getInstance().put(ApiNew.PZTData).jsons(s).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (!s.equals(XML.PTZDATA_0)) {
                            OkHttpUtils.getInstance().put(ApiNew.PZTData).jsons(XML.PTZDATA_0).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(null);
                        }
                    }
                });
                return s;
            }
        }).subscribe();
    }

    public Call ZOOMCRTL(String deviceSerial, String mode) {
        return ZOOMCRTL(deviceSerial, mode, 16);
    }

    //tele -- /wide ++
    public Call ZOOMCRTL(String deviceSerial, String mode, int step) {
        ZOOMCTRL zoomCTRL = new ZOOMCTRL();
        ZOOMCTRL.ZOOMMCTRLDTO zoommctrldto = new ZOOMCTRL.ZOOMMCTRLDTO();
        zoommctrldto.setMode(mode);
        zoommctrldto.setStep(step);
        zoomCTRL.setZOOMCTRL(zoommctrldto);
        return OkHttpUtils.getInstance().put(ApiNew.ZOOMCTRL).jsons(entityToXml(zoomCTRL)).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(null);
    }

    public Call FOCUSCTRL(String deviceSerial, String mode) {
        return FOCUSCTRL(deviceSerial, mode, 8);
    }

    //near聚焦+，far聚焦-
    public Call FOCUSCTRL(String deviceSerial, String mode, int step) {
        FOCUSCTRL focusctrl = new FOCUSCTRL();
        FOCUSCTRL.FOCUSCTRLDTO focusctrldto = new FOCUSCTRL.FOCUSCTRLDTO();
        focusctrldto.setMode(mode);
        focusctrldto.setStep(step);
        focusctrl.setFOCUSCTRL(focusctrldto);
        return OkHttpUtils.getInstance().put(String.format("%s?devIndex=%s", ApiNew.FOCUSCTRL, devIndex)).jsons(entityToXml(focusctrl)).addHeader("EZO-DeviceSerial", devIndex).enqueue(null);
    }

    //简单恢复
    public Call factoryResetBasic(String deviceSerial) {
        return OkHttpUtils.getInstance().put(ApiNew.factoryResetBasic).jsons(XML.PTZDATA_0).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(null);
    }

    //完全恢复
    public Call factoryResetFull(String deviceSerial) {
        return OkHttpUtils.getInstance().put(ApiNew.factoryResetFull).jsons(XML.PTZDATA_0).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(null);
    }

    //重新配网
    public void wirelessServer(String deviceSerial) {
        OkHttpUtils.getInstance().get(ApiNew.wirelessServer).addHeader("EZO-DeviceSerial", deviceSerial).enqueue(new XmlCallback(new JsonCallback<WirelessServer>() {
            @Override
            public void onSuccess(WirelessServer data) {
                if (data != null && data.getWirelessServer() != null) {
                    data.getWirelessServer().setWifiApEnabled("true");
                    OkHttpUtils.getInstance().put(ApiNew.wirelessServer).addHeader("EZO-DeviceSerial", deviceSerial).jsons(entityToXml(data)).enqueue(null);
                }
            }
        }));
    }

    //获取网络信息
    public void getNetworkInterface(String deviceSerial, JsonCallback<NetworkInterface> jsonCallback) {
        OkHttpUtils.getInstance().get(ApiNew.networkInterface).addHeader("EZO-DeviceSerial", deviceSerial)
                .enqueue(new XmlCallback(jsonCallback));
    }

    public void onepushFoucsStart() {
        OkHttpUtils.getInstance().put(String.format("%s?devIndex=%s", ApiNew.onepushfoucsStart, devIndex)).addHeader("EZO-DeviceSerial", devIndex).jsons(XML.PTZDATA_0).enqueue(null);
    }

    //获取配网错误日志
    public void getApConfigLog(String deviceSerial, JsonCallback jsonCallback) {

    }

    private String entityToXml(Object obj) {
        String jsonString = mGson.toJson(obj);
        JsonToXml xml = new JsonToXml.Builder(jsonString).build();
        return xml.toString();
    }


}
