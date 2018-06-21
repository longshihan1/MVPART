package com.longshihan.module_gank.mvp.MainModule.contract;

import com.longshihan.module_gank.mvp.MainModule.model.entity.MeiziData;
import com.longshihan.mvpcomponent.mvp.IView;

/**
 * Created by longshihan on 2017/9/10.
 */

public interface TodayGankContract {
    interface Presenter{
        void getDetail(String page);
    }
    interface View  extends IView {
        void updateListUI(MeiziData meiziData);
        void showOnFailure();
    }
}
