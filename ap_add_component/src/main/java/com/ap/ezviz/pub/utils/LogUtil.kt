package com.ap.ezviz.pub.utils

import android.text.TextUtils
import android.util.Log
import ap.apconnect.add.component.BuildConfig
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object LogUtil {

    val DEBUG = BuildConfig.DEBUG

    @JvmStatic
    fun d(tag: String?, content: String?) {
        debugLog(tag, content)
    }

    @JvmStatic
    fun debugLog(tag: String?, content: String?) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.d(tag, content)
        }
    }

    @JvmStatic
    fun d(tag: String?, callback: (() -> String?)) {
        if (DEBUG) {
            val content = callback.invoke()
            d(tag, content)
        }
    }

    @JvmStatic
    fun e(tag: String?, content: String?) {
        errorLog(tag, content)
    }

    @JvmStatic
    fun errorLog(tag: String?, content: String?) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.e(tag, content)
        }
    }

    @JvmStatic
    fun e(tag: String?, callback: (() -> String?)) {
        if (DEBUG) {
            val content = callback.invoke()
            e(tag, content)
        }
    }

    @JvmStatic
    fun i(tag: String?, content: String?) {
        infoLog(tag, content)
    }

    @JvmStatic
    fun infoLog(tag: String?, content: String?) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.i(tag, content)
        }
    }

    @JvmStatic
    fun i(tag: String?, callback: (() -> String?)) {
        if (DEBUG) {
            val content = callback.invoke()
            i(tag, content)
        }
    }

    @JvmStatic
    fun v(tag: String?, content: String?) {
        verboseLog(tag, content)
    }

    @JvmStatic
    fun verboseLog(tag: String?, content: String?) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.v(tag, content)
        }
    }

    @JvmStatic
    fun v(tag: String?, callback: (() -> String?)) {
        if (DEBUG) {
            val content = callback.invoke()
            v(tag, content)
        }
    }

    @JvmStatic
    fun w(tag: String?, content: String?) {
        warnLog(tag, content)
    }

    @JvmStatic
    fun warnLog(tag: String?, content: String?) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.w(tag, content)
        }
    }

    @JvmStatic
    fun w(tag: String?, callback: (() -> String?)) {
        if (DEBUG) {
            val content = callback.invoke()
            w(tag, content)
        }
    }

    @JvmStatic
    fun d(tag: String?, content: String?, t: Throwable) {
        debugLog(tag, content, t)
    }

    @JvmStatic
    fun debugLog(tag: String?, content: String?, t: Throwable) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.d(tag, content, t)
        }
    }

    @JvmStatic
    fun e(tag: String?, content: String?, t: Throwable) {
        errorLog(tag, content, t)
    }

    @JvmStatic
    fun errorLog(tag: String?, content: String?, t: Throwable) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.e(tag, content, t)
        }
    }

    @JvmStatic
    fun w(tag: String?, t: Throwable) {
        warnLog(tag, t)
    }

    @JvmStatic
    fun warnLog(tag: String?, content: String?, t: Throwable) {
        if (DEBUG && !TextUtils.isEmpty(content)) {
            Log.w(tag, content, t)
        }
    }

    @JvmStatic
    fun warnLog(tag: String?, t: Throwable?) {
        if (DEBUG && t != null) {
            Log.w(tag, t)
        }
    }

    @JvmStatic
    fun makeLogTag(cls: Class<*>): String {
        return "Androidpn_" + cls.simpleName
    }

    @JvmStatic
    fun printJson(tag : String? , content : String){
        if(DEBUG && tag != null){
            var message : String
            message = try {
                when {
                    content.startsWith("{") -> {
                        val jsonObject = JSONObject(content)
                        jsonObject.toString(4) //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                    }
                    content.startsWith("[") -> {
                        val jsonArray = JSONArray(content)
                        jsonArray.toString(4)
                    }
                    else -> content
                }
            } catch (e : JSONException) {
                content
            }
            message = System.getProperty("line.separator") + message
            val lines = message.split(System.getProperty("line.separator"))
            for (line :String in lines) {
                debugLog(tag, "║ $line")
            }
        }
    }

}
