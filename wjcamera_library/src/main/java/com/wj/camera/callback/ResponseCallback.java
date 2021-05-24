package com.wj.camera.callback;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wj.camera.response.BaseDeviceResponse;
import com.wj.camera.uitl.TypeParameterMatcher;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * FileName: ResponseCallback
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class ResponseCallback implements Callback {
    private JsonCallback mJsonCallback;

    public ResponseCallback(JsonCallback jsonCallback) {
        mJsonCallback = jsonCallback;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Observable.just("1").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        if (mJsonCallback != null) {
                            mJsonCallback.onError(10002, e.getMessage());
                        }
                    }
                });
    }

    private static final String TAG = "ResponseCallback";

    @SuppressLint("CheckResult")
    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String string = getJson(call, response);
        Log.i(TAG, "onResponse: "+string);
        if (TextUtils.isEmpty(string) || "{}".equals(string)) {
            Observable.just("1").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object object) throws Exception {
                            if (mJsonCallback != null) {
                                mJsonCallback.onError(10001, "未知错误");
                            }
                        }
                    });
            return;
        }


        if (mJsonCallback != null) {
            Type aClass = TypeParameterMatcher.find0(mJsonCallback.getClass());
            TypeToken typeToken = TypeToken.get(aClass);
            Object o = new Gson().fromJson(string, typeToken.getType());
            Observable.just(o).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object object) throws Exception {
                            if (object instanceof BaseDeviceResponse) {
                                BaseDeviceResponse deviceResponse = (BaseDeviceResponse) object;
                                if (deviceResponse.getCode() == 200) {
                                    mJsonCallback.onSuccess(object);
                                } else {
                                    mJsonCallback.onError(deviceResponse.getCode(), deviceResponse.getMsg());
                                }
                            } else {

                                mJsonCallback.onSuccess(object);
                            }
                        }
                    });

        }
    }

    public abstract String getJson(@NotNull Call call, @NotNull Response response) throws IOException;


}
