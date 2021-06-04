package com.wj.camera.net;

/**
 * FileName: Api
 * Author: xiongxiang
 * Date: 2021/1/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface ApiNew {
    String baseUrl = "https://open.ys7.com";

    //RTMP
    String RTMP = "/api/hikvision/ISAPI/Custom/RTMP";
    //倍数变焦
    String Zoom = "/api/hikvision/ISAPI/Custom/ZOOM";
    //调焦
    String PZTData =  "/api/hikvision/ISAPI/PTZCtrl/channels/1/continuous";
    //子流
    String setting102 =  "/api/hikvision/ISAPI/Streaming/channels/102";
    //主流
    String setting101 =  "/api/hikvision/ISAPI/Streaming/channels/101";
    //音量
    String Audio =  "/api/hikvision/ISAPI/System/TwoWayAudio/channels/1";
    //场景
    String Scene ="/api/hikvision/ISAPI/Image/channels/1/mountingScenario";


    //设备信息
    String DeviceInfo =  "/api/lapp/device/info";
    //设备通道信息
    String DeviceCameraInfo =  "/api/lapp/device/camera/list";
    //修改通道名称
    String CameraNameUpdate =  "/api/lapp/camera/name/update";
    //修改设备名称
    String DeviceNameUpdate =  "/api/lapp/device/name/update";
    //添加设备
    String DeviceAdd =  "/api/lapp/device/add";
    //删除设备
    String DeviceDelete =  "/api/lapp/device/delete";
    //获取token
    String Token =  "/api/lapp/token/get";
    //检查设备升级
    String CheckDeviceUpdate =  "/api/lapp/device/version/info";
    //设备升级
    String DeviceUpdate ="/api/lapp/device/upgrade";
    //设备升级状态
    String DeviceUpdateStatus="/api/lapp/device/upgrade/status";




}
