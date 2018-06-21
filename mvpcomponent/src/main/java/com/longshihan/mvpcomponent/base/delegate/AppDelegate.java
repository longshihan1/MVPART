package com.longshihan.mvpcomponent.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;


import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.base.App;
import com.longshihan.mvpcomponent.di.component.AppComponentImpl;
import com.longshihan.mvpcomponent.di.module.AppModule;
import com.longshihan.mvpcomponent.di.module.ClientModule;
import com.longshihan.mvpcomponent.di.module.GlobalConfigModule;
import com.longshihan.mvpcomponent.intergration.ActivityLifecycle;
import com.longshihan.mvpcomponent.intergration.ConfigModule;
import com.longshihan.mvpcomponent.intergration.ManifestParser;
import com.longshihan.mvpcomponent.intergration.lifecycle.ActivityLifecycleForRxLifecycle;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 * @time 2017/8/16 16:45
 * @des 类的作用：AppDelegate可以代理Application的生命周期,在对应的生命周期,执行对应的逻辑,因为Java只能单继承
 * 所以当遇到某些三方库需要继承于它的Application的时候,就只有自定义Application并继承于三方库的Application,这时就不用再继承BaseApplication
 * 只用在自定义Application中对应的生命周期调用AppDelegate对应的方法(Application一定要实现APP接口),框架就能照常运行
 * <p>
 * 简而言之，这是一个仿的application的实现类，我们需要自定义的application的方法在这里面定义
 */

public class AppDelegate implements App, AppLifecycles {
    private Application mApplication;
    private AppComponent mAppComponent;
    protected ActivityLifecycle mActivityLifecycle;
    protected ActivityLifecycleForRxLifecycle mActivityLifecycleForRxLifecycle;
    private List<ConfigModule> mModules;
    private List<AppLifecycles> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallback;

    public AppDelegate(Context context) {//使用lsit特别适合组件化的工程，每个工程都会注册一个application上的自适应的组件
        this.mModules = new ManifestParser(context).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(context, mAppLifecycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(Context base) {
        for (AppLifecycles lifecycle : mAppLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(Application application) {
        this.mApplication = application;
        AppModule appModule=new AppModule(application);
        ClientModule clientModule=new ClientModule();
        mAppComponent=new AppComponentImpl(appModule,clientModule,getGlobalConfigModule(mApplication, mModules));
        mAppComponent.extras().put(ConfigModule.class.getName(), mModules);
        this.mModules = null;
        mActivityLifecycle=new ActivityLifecycle(mAppComponent.appManager(),application,mAppComponent.extras());
        mActivityLifecycleForRxLifecycle=new ActivityLifecycleForRxLifecycle();
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);

        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        mComponentCallback = new AppComponentCallbacks(mApplication, mAppComponent);

        mApplication.registerComponentCallbacks(mComponentCallback);

        for (AppLifecycles lifecycle : mAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }
    }

    @Override
    public void onTerminate(Application application) {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mActivityLifecycleForRxLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        }
        if (mComponentCallback != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallback);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppLifecycles lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycles = null;
        this.mComponentCallback = null;
        this.mAppLifecycles = null;
        this.mApplication = null;
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule
                .builder();
        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }
        return builder.build();
    }


    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            //内存不足时清理图片请求框架的内存缓存
            mAppComponent.imageLoader().clear(mApplication, ImageConfigImpl
                    .builder()
                    .isClearMemory(true)
                    .build());
        }
    }
}
