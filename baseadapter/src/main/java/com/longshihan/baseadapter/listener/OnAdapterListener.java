package com.longshihan.baseadapter.listener;

import android.support.v7.widget.RecyclerView;

/**
 * 绑定一些数据
 */
public interface OnAdapterListener {
    void onBindAnimation(RecyclerView.ViewHolder holder);
}
