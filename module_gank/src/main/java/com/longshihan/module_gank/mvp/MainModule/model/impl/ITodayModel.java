package com.longshihan.module_gank.mvp.MainModule.model.impl;

import com.longshihan.module_gank.mvp.MainModule.model.entity.MeiziData;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/4 17:14
 * @des
 */

public interface ITodayModel {
    Observable<MeiziData> getCommond(String itemId);
}
