package com.longshihan.mvpcomponent.di.component;

import android.app.Application;

import com.google.gson.Gson;
import com.longshihan.mvpcomponent.base.delegate.AppDelegate;
import com.longshihan.mvpcomponent.error.core.RxErrorHandler;
import com.longshihan.mvpcomponent.intergration.AppManager;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.strategy.imageloader.ImageLoader;

import java.io.File;
import java.util.Map;


import okhttp3.OkHttpClient;

/**
 * @author Administrator
 * @time 2017/8/16 15:35
 * @des 类的作用：dagger，初始化
 */
public interface AppComponent {
    Application application();

    //OKHttp
    OkHttpClient okHttpClient();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader imageLoader();
    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();

    //gson
    Gson gson();

    //用于管理所有activity
    AppManager appManager();

    //用于管理所有仓库,网络请求层,以及数据缓存层
    IRepositoryManager repositoryManager();

    //缓存文件根目录(RxCache和Glide的的缓存都已经作为子文件夹在这个目录里),应该将所有缓存放到这个根目录里,便于管理和清理,可在GlobeConfigModule里配置
    File cacheFile();

    //用来存取一些整个App公用的数据,切勿大量存放大容量数据
    Map<String, Object> extras();
}
