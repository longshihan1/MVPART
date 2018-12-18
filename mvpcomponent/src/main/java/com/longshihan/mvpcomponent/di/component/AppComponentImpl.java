package com.longshihan.mvpcomponent.di.component;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.longshihan.mvpcomponent.base.delegate.AppDelegate;
import com.longshihan.mvpcomponent.di.module.AppModule;
import com.longshihan.mvpcomponent.di.module.ClientModule;
import com.longshihan.mvpcomponent.di.module.GlobalConfigModule;
import com.longshihan.mvpcomponent.error.core.RxErrorHandler;
import com.longshihan.mvpcomponent.http.RequestInterceptor;
import com.longshihan.mvpcomponent.intergration.AppManager;
import com.longshihan.mvpcomponent.intergration.cache.Cache;
import com.longshihan.mvpcomponent.intergration.cache.CacheType;
import com.longshihan.mvpcomponent.intergration.cache.IntelligentCache;
import com.longshihan.mvpcomponent.intergration.cache.LruCache;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.mvp.RepositoryManager;
import com.longshihan.mvpcomponent.strategy.imageloader.ImageLoader;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import io.rx_cache2.internal.RxCache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class AppComponentImpl implements AppComponent {
    private AppModule appModule;
    private ClientModule clientModule;
    private GlobalConfigModule globalConfigModule;
    private Application application;
    private RequestInterceptor requestInterceptor;
    private Cache.Factory factory;

    public AppComponentImpl(AppModule appModule, ClientModule clientModule, GlobalConfigModule globalConfigModule) {
        this.appModule = appModule;
        this.clientModule = clientModule;
        this.globalConfigModule = globalConfigModule;
        this.application = appModule.provideApplication();
    }

    @Override
    public Application application() {
        return application;
    }

    @Override
    public OkHttpClient okHttpClient() {
        if (requestInterceptor == null) {
            requestInterceptor = new RequestInterceptor(globalConfigModule.provideGlobalHttpHandler(), globalConfigModule.providePrintHttpLogLevel());
        }
        return clientModule.provideClient(application, globalConfigModule.provideOkhttpConfiguration(),
                clientModule.provideClientBuilder(), clientModule.provideInterceptor(requestInterceptor),
                globalConfigModule.provideInterceptors(), globalConfigModule.provideGlobalHttpHandler());
    }

    @Override
    public ImageLoader imageLoader() {
        return new ImageLoader(globalConfigModule.provideImageLoaderStrategy());
    }

    @Override
    public RxErrorHandler rxErrorHandler() {
        return clientModule.proRxErrorHandler(application, globalConfigModule.provideResponseErrorListener());
    }

    @Override
    public Gson gson() {
        return appModule.provideGson(application, globalConfigModule.provideGsonConfiguration());
    }

    @Override
    public IRepositoryManager repositoryManager() {
        Retrofit.Builder builder = new Retrofit.Builder();
        RxCache rxCache = clientModule.provideRxCache(
                application, globalConfigModule.provideRxCacheConfiguration(),
                cacheFile());
        Retrofit retrofit = clientModule.provideRetrofit(
                application, globalConfigModule.provideRetrofitConfiguration(),
                builder, okHttpClient(), globalConfigModule.provideBaseUrl(), gson());
        RepositoryManager repositoryManager = new RepositoryManager(retrofit, rxCache,application, factory);
        return appModule.provideRepositoryManager(repositoryManager);
    }

    @Override
    public File cacheFile() {
        return clientModule.provideRxCacheDirectory(globalConfigModule.provideCacheFile(application));
    }

    @Override
    public Cache.Factory cacheFactory() {
        return factory;
    }

    @Override
    public ExecutorService executorService() {
        return globalConfigModule.provideExecutorService();
    }

    @Override
    public Map<String, Object> extras() {
        return appModule.provideExtras();
    }

    @Override
    public void inject(AppDelegate delegate) {
        injectAppDelegate(delegate);
    }

    private AppDelegate injectAppDelegate(AppDelegate delegate) {
        factory=globalConfigModule.provideCacheFactory(application);
        return delegate;
    }
}
