package com.longshihan.module_gank.mvp.MainModule.contract;


import com.longshihan.module_gank.mvp.MainModule.model.entity.GankData;
import com.longshihan.mvpcomponent.mvp.IView;

/**
 * Created by longshihan on 2017/9/10.
 */

public interface AndroidContract {
    interface Presenter{
        void getDetail(String itemId);
    }
    interface View  extends IView {
        void updateListUI(GankData detailEntity);
        void showOnFailure();
    }
}
