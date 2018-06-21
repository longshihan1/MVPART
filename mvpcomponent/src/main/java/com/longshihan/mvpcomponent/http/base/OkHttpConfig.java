package com.longshihan.mvpcomponent.http.base;

import android.content.Context;


import com.longshihan.mvpcomponent.di.module.ClientModule;

import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;

/**
 * @author longshihan
 * @time 2017/9/6 17:56
 * @des
 */

public class OkHttpConfig implements ClientModule.OkhttpConfiguration {
    @Override
    public void configOkhttp(Context context, OkHttpClient.Builder okhttpBuilder) {
        //开启使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看
        okhttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
        //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
        ProgressManager.getInstance().with(okhttpBuilder);
        //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
        RetrofitUrlManager.getInstance().with(okhttpBuilder);
    }
}
