package com.longshihan.mvpart;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.longshihan.baseadapter.base.BaseRecyViewHolder;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/17 0017
 * @des
 * @function
 */

public class StringTextViewHolder extends BaseRecyViewHolder<String>{
    public TextView textView;
    public StringTextViewHolder(Context context, View itemView) {
        super(context, itemView);
        textView=itemView.findViewById(R.id.main_txt3);
    }

    @Override
    public void bindHolder(String o) {
        if (o instanceof String){
            textView.setText(o.toString());
        }
    }

    @Override
    public void onRelease() {

    }
}
