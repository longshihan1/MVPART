package com.longshihan.mvpcomponent.base.delegate;

import android.os.Bundle;

import com.longshihan.mvpcomponent.di.component.AppComponent;


/**
 * @author Administrator
 * @time 2017/8/10 16:11
 * @des 类作用：对框架内部的数据进行一些判断，的第一层接口,新版本取消绑定persenter了
 */

public interface IActivity{
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
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link com.longshihan.mvpcomponent.base.BaseMVPFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();

}
