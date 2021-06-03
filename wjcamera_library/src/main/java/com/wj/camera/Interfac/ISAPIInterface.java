package com.wj.camera.Interfac;

import androidx.annotation.IntRange;

import com.wj.camera.callback.JsonCallback;
import com.wj.camera.config.WJDeviceSceneEnum;
import com.wj.camera.response.ResponseStatus;
import com.wj.camera.response.RtmpConfig;
import com.wj.camera.response.SceneResponse;
import com.wj.camera.response.TwoWayAudio;
import com.wj.camera.response.VideoConfig;
import com.wj.camera.response.ZoomResponse;

import okhttp3.OkHttpClient;

/**
 * FileName: ISAPIInterface
 * Author: xiongxiang
 * Date: 2021/6/1
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface ISAPIInterface {
    void config(String deviceSerial);
    void setRtmp(RtmpConfig rtmpConfig, JsonCallback<ResponseStatus> callback);

    void setRtmp(String deviceSerial, RtmpConfig rtmpConfig, JsonCallback<ResponseStatus> callback);

    ResponseStatus setRtmp(String device_serial, RtmpConfig rtmpConfig);

    void zoom(int zoom);

    void setPTData(String ptzData, JsonCallback callback);

    void getRTMP(String deviceSerial, JsonCallback<RtmpConfig> callback);

    void getRTMP(JsonCallback<RtmpConfig> callback);

    void getAudio(JsonCallback<TwoWayAudio> callback);

    void setAuido(TwoWayAudio twoWayAudio, JsonCallback<ResponseStatus> callback);

    void setVolume(String deviceSerial, @IntRange(from = 0, to = 100) int volume);

    void setVolume(String deviceSerial, @IntRange(from = 0, to = 100) int volume, JsonCallback<ResponseStatus> callback);

    RtmpConfig getRTMP(String device_serial);

    RtmpConfig getRTMP(String device_serial, OkHttpClient client);

    void getVideoConfig(JsonCallback<VideoConfig> callback);

    void setVideoConfig(VideoConfig videoConfig, JsonCallback<ResponseStatus> callback);

    void getZoom(JsonCallback<ZoomResponse> jsonCallback);

    void setBitrate(String deviceSerial, String bitrate, JsonCallback<ResponseStatus> jsonCallback);

    void setResolution(String deviceSerial, String[] ratio, JsonCallback<ResponseStatus> jsonCallback);

    void getDeviceConfig(String deviceSerial, JsonCallback<VideoConfig> jsonCallback);

    void getScene(String deviceSerial, JsonCallback<SceneResponse> jsonCallback);

    void setScene(String deviceSerial, WJDeviceSceneEnum indoor, JsonCallback<ResponseStatus> jsonCallback);
}
