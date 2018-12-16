package com.longshihan.mvpcomponent.base;


import android.support.annotation.NonNull;

import com.longshihan.mvpcomponent.di.component.AppComponent;

/**
 * @author Administrator
 * @time 2017/8/16 15:32
 * @des application
 */

public interface App {
    @NonNull
    AppComponent getAppComponent();
}
