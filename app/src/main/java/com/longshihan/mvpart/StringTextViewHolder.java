package com.longshihan.mvpart;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longshihan.baseadapter.base.BaseViewHolder;
import com.longshihan.mvpart.arch.model.StringItem;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/17 0017
 * @des
 * @function
 */

public class StringTextViewHolder extends BaseViewHolder<StringItem> {
    public TextView textView;
    public LinearLayout linearLayout;
    public StringTextViewHolder(Context context, View itemView) {
        super(context, itemView);
        textView=itemView.findViewById(R.id.main_txt3);
        linearLayout=itemView.findViewById(R.id.main_lin);
    }

    @Override
    public void bindHolder(StringItem o) {
        textView.setText(o.getName());
        linearLayout.setBackgroundResource(o.getColor());
    }

    @Override
    public void onRelease() {

    }
}
