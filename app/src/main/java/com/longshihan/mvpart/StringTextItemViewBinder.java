package com.longshihan.mvpart;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.longshihan.baseadapter.ItemViewBinder;
import com.longshihan.baseadapter.base.BaseRecyViewHolder;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/17 0017
 * @des
 * @function
 */

public class StringTextItemViewBinder extends ItemViewBinder<String,StringTextViewHolder> {
    @NonNull
    @Override
    protected StringTextViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return (StringTextViewHolder) BaseRecyViewHolder.createViewHolder(parent.getContext(),inflater.inflate(R.layout.item_txt,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull StringTextViewHolder holder, @NonNull String item) {
        holder.bindHolder(item);
    }
}
