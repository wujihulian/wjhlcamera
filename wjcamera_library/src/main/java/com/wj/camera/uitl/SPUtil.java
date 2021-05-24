package com.wj.camera.uitl;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/8.
 */

public class SPUtil {

    private static String fileName = "wj_camera";

    public static String saveData(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        switch (type) {
            case "String":
                edit.putString(key, (String) object);
                break;
            case "Boolean":
                edit.putBoolean(key, (Boolean) object);
                break;
            case "Integer":
                edit.putInt(key, (Integer) object);
                break;
            case "Float":
                edit.putFloat(key, (Float) object);
                break;
        }
        edit.commit();
        return type;
    }

    public static Object getData(Context context, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) object);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) object);
        } else {
            return object;
        }
    }

    public static void clearData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
