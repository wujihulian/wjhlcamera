package com.wj.uikit.callback;

import android.content.Context;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.wj.camera.callback.JsonCallback;

import java.lang.ref.WeakReference;

/**
 * FileName: LoadingJsonCallback
 * Author: xiongxiang
 * Date: 2021/3/13
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public  class LoadingJsonCallback<T>  extends JsonCallback<T> {
    private WeakReference<Context> reference;   //
    private LoadingPopupView mLoadingPopupView;
    public LoadingJsonCallback (Context context){
        this(context,true);
    }

    public LoadingJsonCallback (Context context,boolean isBack){
        reference =new WeakReference<>(context);
        if (mLoadingPopupView == null) {
            if (reference.get() != null) {
                mLoadingPopupView = new XPopup.Builder(reference.get()).dismissOnBackPressed(isBack).dismissOnTouchOutside(isBack).asLoading();
            }
        }
        if (mLoadingPopupView != null) {
            mLoadingPopupView.show();
        }
    }


    @Override
    public void onSuccess(T data) {
        if (mLoadingPopupView != null) {
            mLoadingPopupView.dismiss();
            mLoadingPopupView = null;
        }
    }

    @Override
    public void onError(int code, String msg) {
        super.onError(code, msg);
        if (mLoadingPopupView != null) {
            mLoadingPopupView.dismiss();
            mLoadingPopupView = null;
        }
    }
}
