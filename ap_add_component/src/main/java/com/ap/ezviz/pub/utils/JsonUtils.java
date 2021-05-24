package com.ap.ezviz.pub.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Json转换工具
 */
public class JsonUtils {

    private static Gson gson;

    private static Gson getGson() {
        synchronized (JsonUtils.class) {
            if (gson == null) {
                gson = new GsonBuilder().disableHtmlEscaping().create();
            }
        }
        return gson;
    }

    /**
     * 对象转Json字符串
     *
     * @return Json字符串
     */
    public static String toJson(Object object) {
        return getGson().toJson(object);
    }

    /**
     * Json转为对象
     *
     * @param json JSON
     * @param clazz 类型
     * @return 对象
     */
    public static <T> T formJson(String json, Class<T> clazz) {
        return getGson().fromJson(json, clazz);
    }

    /**
     * Json转对象
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }
}
