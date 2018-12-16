package com.longshihan.mvpcomponent.di.component;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.longshihan.mvpcomponent.base.delegate.AppDelegate;
import com.longshihan.mvpcomponent.di.module.GlobalConfigModule;
import com.longshihan.mvpcomponent.error.core.RxErrorHandler;
import com.longshihan.mvpcomponent.intergration.AppManager;
import com.longshihan.mvpcomponent.intergration.cache.Cache;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.strategy.imageloader.ImageLoader;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;


import okhttp3.OkHttpClient;

/**
 * @author Administrator
 * @time 2017/8/16 15:35
 * @des 类的作用：dagger，初始化
 * 可通过 {@link com.longshihan.mvpcomponent.utils.ArmsUtils#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 */
public interface AppComponent {
    Application application();

    //OKHttp
    OkHttpClient okHttpClient();

    /**
     * 图片加载管理器, 用于加载图片的管理类, 使用策略者模式, 可在运行时动态替换任何图片加载框架
     * arms-imageloader-glide 提供 Glide 的策略实现类, 也可以自行实现
     * 需要在 {@link com.longshihan.mvpcomponent.intergration.ConfigModule#applyOptions(Context, GlobalConfigModule.Builder)} 中
     * 手动注册 {@link com.longshihan.mvpcomponent.strategy.imageloader.BaseImageLoaderStrategy}, {@link ImageLoader} 才能正常使用
     *
     * @return
     */
    ImageLoader imageLoader();
    /**
     * RxJava 错误处理管理类
     *
     * @return {@link RxErrorHandler}
     */
    RxErrorHandler rxErrorHandler();

    //gson
    Gson gson();

    /**
     * 用于管理网络请求层, 以及数据缓存层
     *
     * @return {@link IRepositoryManager}
     */
    IRepositoryManager repositoryManager();

    //缓存文件根目录(RxCache和Glide的的缓存都已经作为子文件夹在这个目录里),应该将所有缓存放到这个根目录里,便于管理和清理,可在GlobeConfigModule里配置
    File cacheFile();

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return {@link Cache.Factory}
     */
    Cache.Factory cacheFactory();

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return {@link ExecutorService}
     */
    ExecutorService executorService();

    //用来存取一些整个App公用的数据,切勿大量存放大容量数据
    Map<String, Object> extras();

    void inject(AppDelegate delegate);
}
