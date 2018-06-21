package com.longshihan.mvpcomponent.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.mvp.RepositoryManager;

import java.util.Map;


/**
 * @author Administrator
 * @time 2017/8/16 15:37
 * @des 类的作用：与Application相关的初始化
 */

public class AppModule {
    private Application mApplication;
    private Map<String, Object> extras;

    public AppModule(Application application) {
        mApplication = application;
        extras=new ArrayMap<>();
    }

    public Application provideApplication() {
        return mApplication;
    }

    public Gson provideGson(Application application, @Nullable GsonConfiguration configuration){
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    public Map<String, Object> provideExtras() {
        return extras;
    }


    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }
}
