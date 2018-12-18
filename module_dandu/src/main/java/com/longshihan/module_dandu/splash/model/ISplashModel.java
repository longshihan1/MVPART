package com.longshihan.module_dandu.splash.model;

import com.longshihan.module_dandu.mvp.model.entity.SplashEntity;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/1 14:17
 * @des
 */

public interface ISplashModel {
        Observable<SplashEntity> getAD(int lastIdQueried, String deviceId);
}
