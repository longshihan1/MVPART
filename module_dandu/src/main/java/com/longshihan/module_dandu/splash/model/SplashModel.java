package com.longshihan.module_dandu.splash.model;

import com.longshihan.module_dandu.http.ApiService;
import com.longshihan.module_dandu.mvp.model.entity.SplashEntity;
import com.longshihan.mvpcomponent.mvp.BaseModel;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.utils.TimeUtil;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/1 14:21
 * @des
 */

public class SplashModel extends BaseModel implements ISplashModel {
    public SplashModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<SplashEntity> getAD(int lastIdQueried, String deviceId) {
        final String client = "android";
        String version = "1.3.0";
        Long time = TimeUtil.getCurrentSeconds();
        Observable<SplashEntity> splashEntityObservable = mRepositoryManager.obtainRetrofitService(ApiService.class)
                .getSplash(client, version, time, deviceId);
        return splashEntityObservable;
    }
}
