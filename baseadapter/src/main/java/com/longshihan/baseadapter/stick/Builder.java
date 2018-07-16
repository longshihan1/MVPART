package com.longshihan.baseadapter.stick;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/16 0016
 * @des
 * @function
 */

public class Builder {
    private RecyclerView recyclerView;
    private int gravity = Gravity.LEFT;
    public StickHeaderListener wrapper;

    public static Builder getInstance(){
        return new Builder();
    }

    public Builder(){
    }
    public StickHeaderListener getWrapper() {
        return wrapper;
    }

    public Builder setWrapper(StickHeaderListener wrapper) {
        this.wrapper = wrapper;
        return this;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public Builder setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    public Builder setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }
}
