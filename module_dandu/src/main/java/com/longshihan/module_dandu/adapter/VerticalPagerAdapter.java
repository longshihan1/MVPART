package com.longshihan.module_dandu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.longshihan.module_dandu.main.ui.MainDetailFragment;
import com.longshihan.module_dandu.mvp.model.entity.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longshihan on 2017/9/3.
 */

public class VerticalPagerAdapter extends FragmentStatePagerAdapter {
    private List<Item> dataList = new ArrayList<>();

    public VerticalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MainDetailFragment.instance(dataList.get(position));
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void setDataList(List<Item> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        if (dataList.size() == 0) {
            return "0";
        }
        Item item = dataList.get(dataList.size() - 1);
        return item.getId();
    }

    public String getLastItemCreateTime() {
        if (dataList.size() == 0) {
            return "0";
        }
        Item item = dataList.get(dataList.size() - 1);
        return item.getCreate_time();
    }
}