package com.longshihan.mvpart.arch.model;

import android.support.annotation.ColorRes;

/**
 * Created by longshihan.
 *
 * @time 2018/7/18 0018
 * @des
 * @function
 */

public class StringItem {
    @ColorRes
    private int color;
    private String name;

    public StringItem(String name,@ColorRes int color) {
        this.color = color;
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public @ColorRes int getColor() {
        return color;
    }
}
