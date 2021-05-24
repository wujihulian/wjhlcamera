package com.wj.uikit.subscribe;

import android.content.Context;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * FileName: LoadingObserver
 * Author: xiongxiang
 * Date: 2021/3/10
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class LoadingObserver<T> implements Observer<T> {
    private WeakReference<Context> reference;   //
    private LoadingPopupView mLoadingPopupView;
    private Disposable mDisposable;
    private static final String TAG = "LoadingObserver";

    public LoadingObserver(Context context) {
        reference = new WeakReference<>(context);
        if (mLoadingPopupView == null) {
            if (reference.get() != null) {
                mLoadingPopupView = new XPopup.Builder(reference.get())
                        .dismissOnTouchOutside(false)
                        .setPopupCallback(new SimpleCallback() {
                                              @Override
                                              public void onDismiss(BasePopupView popupView) {
                                                  super.onDismiss(popupView);
                                                  dispose();
                                              }
                                          }
                        ).asLoading();
            }
        }
        if (mLoadingPopupView != null) {
            mLoadingPopupView.show();

        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {


    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (mLoadingPopupView != null) {
            try {
                mLoadingPopupView.dismiss();
                mLoadingPopupView = null;
            }catch (Exception exception){

            }
        }
    }

    @Override
    public void onComplete() {
        if (mLoadingPopupView != null) {
            mLoadingPopupView.dismiss();
            mLoadingPopupView = null;
        }
    }

    public void dispose() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mDisposable = null;
        }
    }
}
