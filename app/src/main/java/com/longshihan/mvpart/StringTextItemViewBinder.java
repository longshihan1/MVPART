package com.longshihan.mvpart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.baseadapter.ItemViewBinder;
import com.longshihan.baseadapter.base.BaseViewHolder;
import com.longshihan.mvpart.arch.model.StringItem;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/17 0017
 * @des
 * @function
 */

public class StringTextItemViewBinder extends ItemViewBinder<StringItem,StringTextViewHolder> {
    @NonNull
    @Override
    protected StringTextViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view=inflater.inflate(R.layout.item_txt,parent,false);
        StringTextViewHolder viewHolder= new StringTextViewHolder(parent.getContext(),view);
        return  viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull StringTextViewHolder holder, @NonNull StringItem item) {
        holder.bindHolder(item);
    }
}
