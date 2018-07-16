package com.longshihan.baseadapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/12 0012
 * @des
 * @function
 */

public abstract class BaseRecyViewHolder<T> extends RecyclerView.ViewHolder {
    public View itemView;
    public Context context;
    public BaseRecyViewHolder(Context context, View itemView) {
        super(itemView);
        this.itemView=itemView;
        this.context=context;
    }

    public abstract void bindHolder(T t);

    public abstract void onRelease();

}
