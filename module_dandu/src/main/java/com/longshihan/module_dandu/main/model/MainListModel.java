package com.longshihan.module_dandu.main.model;

import com.longshihan.module_dandu.http.ApiService;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.module_dandu.mvp.model.entity.Result;
import com.longshihan.mvpcomponent.mvp.BaseModel;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.utils.TimeUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/1 14:21
 * @des
 */

public class MainListModel extends BaseModel implements IMainListModel {
    public MainListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


    @Override
    public Observable<Result.Data<List<Item>>> getAD(int page, int model, String pageId,
                                                     String deviceId, String createTime) {
        Observable<Result.Data<List<Item>>> splashEntityObservable = mRepositoryManager
                .obtainRetrofitService(ApiService.class)
                .getList("api", "getList", page, model, pageId, createTime, "android", "1.3.0",
                        TimeUtil.getCurrentSeconds(), deviceId, 1);
        return splashEntityObservable;
    }

    @Override
    public Observable<Result.Data> getCommond(String deviceId) {
        Observable<Result.Data> recommend = mRepositoryManager
                .obtainRetrofitService(ApiService.class)
                .getRecommend("home", "Api", "getLunar", "android", deviceId, deviceId);
        return recommend;
    }
}
