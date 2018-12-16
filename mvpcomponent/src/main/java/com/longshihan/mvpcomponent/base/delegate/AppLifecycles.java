package com.longshihan.mvpcomponent.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * @author Administrator
 * @time 2017/8/10 16:23
 * @des 类作用：绑定application的若干状态
 * 用于代理 {@link Application} 的生命周期
 */

public interface AppLifecycles {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
