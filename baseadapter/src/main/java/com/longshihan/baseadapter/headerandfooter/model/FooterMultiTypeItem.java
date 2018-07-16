package com.longshihan.baseadapter.headerandfooter.model;

import android.view.View;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/13 0013
 * @des 一般场景下的foot的item
 * @function
 */

public class FooterMultiTypeItem {
    private View view;

    public FooterMultiTypeItem(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public FooterMultiTypeItem setView(View view) {
        this.view = view;
        return this;
    }
}
