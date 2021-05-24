package com.wj.uikit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected List<T> mDatas;

    public abstract int inflaterLayout(int viewType);

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        convert(holder, getData(position), position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(LayoutInflater.from(parent.getContext()).inflate(inflaterLayout(viewType), parent, false), viewType);
    }

    public ViewHolder createViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }


    public abstract void convert(ViewHolder holder, T data, int position);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void addData(T data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.add(data);
    }

    public void addData(List<T> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(datas);
    }

    public void setData(List<T> datas) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        if (datas != null) {
            mDatas.addAll(datas);
        }
    }

    public void setData(T data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.add(data);
    }

    public void clear() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
    }

    public void remove(int position) {
        if (mDatas != null && mDatas.size() > 0 && position < mDatas.size()) {
            mDatas.remove(position);
        }
    }

    public List<T> getAllData() {
        return mDatas == null ? new ArrayList<T>() : mDatas;
    }

    public T getData(int position) {
        return mDatas.get(position);
    }


}
