package com.longshihan.baseadapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/12 0012
 * @des 继承自recyclerview.ViewHolder 的viewHolder
 * @function
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public View itemView;
    public Context context;

    public BaseViewHolder(Context context,View itemView) {
        super(itemView);
        this.itemView=itemView;
        this.context=context;
    }

    public abstract void bindHolder(T t);

    public abstract void onRelease();

}
