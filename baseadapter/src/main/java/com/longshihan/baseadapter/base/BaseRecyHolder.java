package com.longshihan.baseadapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/17 0017
 * @des
 * @function
 */

public class BaseRecyHolder extends RecyclerView.ViewHolder {
    public View itemView;
    public Context context;

    public BaseRecyHolder(Context context,View itemView) {
        super(itemView);
        this.itemView=itemView;
        this.context=context;
    }
}
