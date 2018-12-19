package com.longshihan.module_dandu.main.model;

import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.module_dandu.mvp.model.entity.Result;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author longshihan
 * @time 2017/9/1 14:17
 * @des
 */

public interface IMainListModel {
        Observable<Result.Data<List<Item>>> getAD(int page, int model, String pageId, String deviceId, String createTime);
        Observable<Result.Data> getCommond(String deviceId);
}
