package com.wj.camera.net;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class RxConsumer implements Consumer<Disposable> {
    private Lifecycle mLifecycle;
    private Disposable mDisposable;
    private View mView;

    public RxConsumer(FragmentActivity fragmentActivity) {
        this.mLifecycle = fragmentActivity.getLifecycle();
    }

    public RxConsumer(Fragment fragment) {
        this.mLifecycle = fragment.getViewLifecycleOwner().getLifecycle();
    }

    public RxConsumer(View view) {
        this.mView = view;
    }

    @Override
    public void accept(final Disposable disposable) throws Exception {
        mDisposable = disposable;
        if (mLifecycle != null) {
            addObserver(mLifecycle);
            return;
        }
        if (mView != null) {
            addObserver(mView);
            return;
        }
    }

    private void addObserver(final View view) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                dispose();
                view.removeOnAttachStateChangeListener(this);
            }
        });
    }

    private void addObserver(Lifecycle lifecycle) {
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                switch (event) {
                    case ON_DESTROY:
                        dispose();
                        break;
                }
            }
        });
    }

    private void dispose() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mDisposable = null;
        }
    }
}
