package com.longshihan.mvpcomponent.base.delegate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.mvpcomponent.di.component.AppComponent;


/**
 * @author longshihan
 * @time 2017/8/18 10:59
 * @des fragment一些固有操作
 */

public interface IFragment {

    View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void initData(Bundle savedInstanceState);

    /**
     * 提供AppComponent(提供所有的单例对象)给实现类，进行Component依赖
     * @param appComponent
     */
    void setupFragmentComponent(AppComponent appComponent);
}
