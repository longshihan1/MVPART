package com.longshihan.module_gank.mvp.MainModule.model.impl;


import com.longshihan.module_gank.http.GankService;
import com.longshihan.module_gank.mvp.MainModule.model.entity.MeiziData;
import com.longshihan.mvpcomponent.mvp.BaseModel;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/4 17:15
 * @des
 */

public class TodayModel extends BaseModel implements ITodayModel {
    public TodayModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<MeiziData> getCommond(String page) {
        Observable<MeiziData> splashEntityObservable = mRepositoryManager
                .obtainRetrofitService(GankService.class)
                .getMeiziData(page);
        return splashEntityObservable;
    }
}
