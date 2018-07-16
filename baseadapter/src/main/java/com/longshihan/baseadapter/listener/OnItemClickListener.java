package com.longshihan.baseadapter.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/16 0016
 * @des
 * @function
 */

public interface OnItemClickListener<T> {
    void onItemClick(View view, RecyclerView.ViewHolder holder, T t, int position);

    boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T t, int position);
}