package com.longshihan.mvpcomponent.mvp;

/**
 * Created by longshihan on 2017/8/16.
 */

public interface IView {
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showError(int type, String message);

    /**
     * 杀死自己
     */
    void closeActivity();
}
