package com.longshihan.module_dandu.detail.model;

import com.longshihan.module_dandu.mvp.model.entity.DetailEntity;
import com.longshihan.module_dandu.mvp.model.entity.Result;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/4 17:14
 * @des
 */

public interface IDetailModel {
    Observable<Result.Data<DetailEntity>> getCommond(String itemId);
}
