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
 * @des
 * @function
 */

public abstract class BaseRecyViewHolder<T> extends BaseRecyHolder {

    public BaseRecyViewHolder(Context context, View itemView) {
        super(context,itemView);
    }


    public static RecyclerView.ViewHolder createViewHolder(Context context, View itemView) {
        RecyclerView.ViewHolder holder = new BaseRecyHolder(context, itemView);
        return holder;
    }

    public static RecyclerView.ViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return createViewHolder(context,itemView);
    }

    public abstract void bindHolder(T t);

    public abstract void onRelease();

}
