package com.longshihan.module_gank.adapter;

import android.support.annotation.Nullable;

import com.longshihan.baseadapter.BaseQuickAdapter;
import com.longshihan.module_gank.adapter.viewholder.MeiZiTestHolder;
import com.longshihan.module_gank.mvp.MainModule.model.entity.Meizi;

import java.util.ArrayList;
import java.util.List;

public class MeiziTestAdapter extends BaseQuickAdapter<Meizi, MeiZiTestHolder> {


    public MeiziTestAdapter(int layoutResId, @Nullable List<Meizi> data) {
        super(layoutResId, data);
    }

    public void replaceList(List<Meizi> datas) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData = datas;
        notifyDataSetChanged();
    }

    public void appendList(List<Meizi> datas) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(MeiZiTestHolder helper, Meizi item) {
        helper.bindHolder(item,mContext);
    }
}
