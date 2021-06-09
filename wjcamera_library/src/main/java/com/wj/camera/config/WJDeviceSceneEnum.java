package com.wj.camera.config;

/**
 * FileName: StringScene
 * Author: xiongxiang
 * Date: 2021/6/1
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public enum WJDeviceSceneEnum {
    INDOOR("indoor", "电子白板"),
    OUTDOOR("outdoor", "广角模式"),
    DAY("day", "摄像模式");
    //NIGHT("night", "夜晚"),
    //MORNING("morning", "黎明"),
    //NIGHTFALL("nightfall", "黄昏");




    private String scene;
    private String title;

    WJDeviceSceneEnum(String scene, String title) {
        this.scene = scene;
        this.title = title;
    }

    public String getScene() {
        return scene;
    }

    public String getTitle() {
        return title;
    }

    public static String[] toArrayString() {
        WJDeviceSceneEnum[] values = WJDeviceSceneEnum.values();
        int length = values.length;
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            strings[i] = values[i].getTitle();
        }
        return strings;
    }

    public static String getSceneTitle(String scene) {
        WJDeviceSceneEnum[] values = WJDeviceSceneEnum.values();
        int length = values.length;
        for (int i = 0; i < length; i++) {
            if (scene.equals(values[i].scene)) {
                return values[i].title;
            }
        }
        return "";
    }

}
