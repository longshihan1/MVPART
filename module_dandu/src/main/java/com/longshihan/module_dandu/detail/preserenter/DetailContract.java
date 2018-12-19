package com.longshihan.module_dandu.detail.preserenter;

import com.longshihan.module_dandu.mvp.model.entity.DetailEntity;
import com.longshihan.mvpcomponent.mvp.IView;

/**
 * @author longshihan
 * @time 2017/9/4 17:05
 * @des
 */

public interface DetailContract {
    interface Presenter{
        void getDetail(String itemId);
    }
    interface View  extends IView {
        void updateListUI(DetailEntity detailEntity);
        void showOnFailure();
    }
}
