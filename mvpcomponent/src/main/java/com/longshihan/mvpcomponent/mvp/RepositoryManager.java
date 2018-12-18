package com.longshihan.mvpcomponent.mvp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.longshihan.mvpcomponent.intergration.cache.Cache;
import com.longshihan.mvpcomponent.intergration.cache.CacheType;
import com.longshihan.mvpcomponent.utils.Preconditions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * @author Administrator
 * @time 2017/8/10 14:41
 * @des 类作用：用来管理所有业务逻辑的仓库,网络请求层,以及数据缓存层,以后可以添加数据库请求层
 * 所有仓库不直接持有却通过RepositoryManager拿到需要的请求层做数据处理的好处是
 * 仓库可以直接和对应的请求层解耦,比如网路请求层,需要从Retrofit替换为其他网络请求库,这时就仓库就不会受到影响
 */

public class RepositoryManager implements IRepositoryManager {

    private Retrofit mRetrofit;
    private RxCache mRxCache;
    private Application mApplication;
    Cache.Factory mCachefactory;
    private Cache mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;

    public RepositoryManager(Retrofit retrofit, RxCache rxCache,Application application,Cache.Factory factory) {
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
        this.mApplication = application;
        this.mCachefactory=factory;
    }


    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    @NonNull
    @Override
    public synchronized <T> T obtainRetrofitService(@NonNull Class<T> serviceClass) {
        return createWrapperService(serviceClass);
    }

    /**
     * 根据 https://zhuanlan.zhihu.com/p/40097338 对 Retrofit 进行的优化
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    private <T> T createWrapperService(Class<T> serviceClass) {
        Preconditions.checkNotNull(serviceClass, "serviceClass == null");
        // 通过二次代理，对 Retrofit 代理方法的调用包进新的 Observable 里在 io 线程执行。
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, @Nullable Object[] args)
                            throws Throwable {
                        if (method.getReturnType() == Observable.class) {
                            // 如果方法返回值是 Observable 的话，则包一层再返回
                            return Observable.defer(() -> {
                                final T service = getRetrofitService(serviceClass);
                                // 执行真正的 Retrofit 动态代理的方法
                                return ((Observable) getRetrofitMethod(service, method)
                                        .invoke(service, args))
                                        .subscribeOn(Schedulers.io());
                            }).subscribeOn(Schedulers.single());
                        }
                        // 返回值不是 Observable 的话不处理
                        final T service = getRetrofitService(serviceClass);
                        return getRetrofitMethod(service, method).invoke(service, args);
                    }
                });
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    private <T> T getRetrofitService(Class<T> serviceClass) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCachefactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mRetrofitServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(serviceClass.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = mRetrofit.create(serviceClass);
            mRetrofitServiceCache.put(serviceClass.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    private <T> Method getRetrofitMethod(T service, Method method) throws NoSuchMethodException {
        return service.getClass().getMethod(method.getName(), method.getParameterTypes());
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cacheClass Cache class
     * @param <T>        Cache class
     * @return Cache
     */
    @NonNull
    @Override
    public synchronized <T> T obtainCacheService(@NonNull Class<T> cacheClass) {
        Preconditions.checkNotNull(cacheClass, "cacheClass == null");
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCachefactory.build(CacheType.CACHE_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mCacheServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) mCacheServiceCache.get(cacheClass.getCanonicalName());
        if (cacheService == null) {
            cacheService = mRxCache.using(cacheClass);
            mCacheServiceCache.put(cacheClass.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.evictAll().subscribe();
    }

    @NonNull
    @Override
    public Context getContext() {
        return mApplication;
    }

}
