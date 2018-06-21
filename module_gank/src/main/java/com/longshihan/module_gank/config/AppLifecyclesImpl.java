package com.longshihan.module_gank.config;

import android.app.Application;
import android.content.Context;

import com.longshihan.mvpcomponent.BuildConfig;
import com.longshihan.mvpcomponent.base.delegate.AppLifecycles;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * @author longshihan
 * @time 2017/9/6 17:01
 * @des
 */

public class AppLifecyclesImpl implements AppLifecycles {
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
//        CrashReport.initCrashReport(application, "6e929dcc68", true);
        if (BuildConfig.DEBUG) {//Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            //Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
            //
            //                    Timber.plant(new Timber.DebugTree() {
            //                        @Override
            //                        protected void log(int priority, String tag, String message, Throwable t) {
            //                            Logger.log(priority, tag, message, t);
            //                        }
            //                    });
            //leakCanary内存泄露检查
        }
        if (BuildConfig.TDEBUG) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            Logger.addLogAdapter(new AndroidLogAdapter());//debug下使用Logger日志模块
        }
    }

    @Override
    public void onTerminate(Application application) {

    }
}
