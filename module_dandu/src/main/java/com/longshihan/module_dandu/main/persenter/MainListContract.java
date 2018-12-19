package com.longshihan.module_dandu.main.persenter;

import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.mvpcomponent.mvp.IView;

import java.util.List;

/**
 * @author longshihan
 * @time 2017/9/1 13:56
 * @des contract类
 */

public interface MainListContract {
    interface View extends IView {
        void showNoData();
        void showNoMore();
        void updateListUI(List<Item> itemList);
        void showOnFailure();
        void showLunar(String content);
    }

    interface Presenter {
        void getListByPage(int page, int model, String pageId, String deviceId, String createTime);
        void getRecommend(String deviceId);
    }
}
