package com.longshihan.mvpcomponent.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.intergration.cache.Cache;


/**
 * @author Administrator
 * @time 2017/8/10 16:11
 * @des 类作用：对框架内部的数据进行一些判断，的第一层接口,新版本取消绑定persenter了
 */

public interface IActivity{

    /**
     * 提供在 {@link Activity} 生命周期内的缓存容器, 可向此 {@link Activity} 存取一些必要的数据
     * 此缓存容器和 {@link Activity} 的生命周期绑定, 如果 {@link Activity} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 <a href="https://github.com/JessYanCoding/LifecycleModel">LifecycleModel</a>
     *
     * @return like {@link com.longshihan.mvpcomponent.intergration.cache.LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 提供AppComponent(提供所有的单例对象)给实现类，进行Component依赖
     *
     * @param appComponent
     */
    void setupActivityComponent(AppComponent appComponent);


    /**
     * 如果initView返回0,框架则不会调用{@link android.app.Activity#setContentView(int)}
     *
     * @return
     * @param savedInstanceState
     */
    int initView(Bundle savedInstanceState);

    void initData();


    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于
     * {@link com.longshihan.mvpcomponent.base.BaseMVPFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();

}
