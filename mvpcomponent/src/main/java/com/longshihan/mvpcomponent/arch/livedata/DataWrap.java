package com.longshihan.mvpcomponent.arch.livedata;

/**
 * Created by longshihan.
 *
 * @time 2018/7/4 0004
 * @des
 * @function
 */

public class DataWrap<T> {
    final T data;

    public DataWrap(T data) {
        this.data = data;
    }

    public boolean isNull() {
        return data == null;
    }

    public T get() {
        return data;
    }
}
