package com.longshihan.module_dandu.detail.model;

import com.longshihan.module_dandu.http.ApiService;
import com.longshihan.module_dandu.mvp.model.entity.DetailEntity;
import com.longshihan.module_dandu.mvp.model.entity.Result;
import com.longshihan.mvpcomponent.mvp.BaseModel;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/4 17:15
 * @des
 */

public class DetailModel extends BaseModel implements IDetailModel {
    public DetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<Result.Data<DetailEntity>> getCommond(String itemId) {
        Observable<Result.Data<DetailEntity>> splashEntityObservable = mRepositoryManager
                .obtainRetrofitService(ApiService.class)
                .getDetail("api", "getPost", itemId, 1);
        return splashEntityObservable;
    }
}
