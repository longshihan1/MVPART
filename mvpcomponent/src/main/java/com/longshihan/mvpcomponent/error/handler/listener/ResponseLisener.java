package com.longshihan.mvpcomponent.error.handler.listener;

/**
 * @author longshihan
 * @time 2017/9/7 16:26
 * @des
 */

public interface ResponseLisener<T> {
    void accept(T t);
    void error(Throwable throwable);
}
