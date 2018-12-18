package com.longshihan.module_dandu.splash.persenter;


import com.longshihan.mvpcomponent.mvp.IView;

/**
 * @author longshihan
 * @time 2017/9/1 13:56
 * @des splsh的contract类
 */

public interface SplashContract {
    interface View extends IView {
        void startLoadMore();
        void endLoadMore();
    }

    interface Presenter {
        void StartTask(String deviceId);
    }
}
