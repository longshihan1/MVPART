package com.longshihan.baseadapter.base;

import android.content.Context;
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

public class BaseViewHolder extends BaseRecyViewHolder {
    public BaseViewHolder(Context context, View itemView) {
        super(context,itemView);
    }

    @Override
    public void bindHolder( Object o) {
    }

    @Override
    public void onRelease() {
    }


    public static BaseViewHolder createViewHolder(Context context, View itemView) {
        BaseViewHolder holder = new BaseViewHolder(context, itemView);
        return holder;
    }

    public static BaseViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return createViewHolder(context,itemView);
    }
}
