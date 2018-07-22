package com.longshihan.mvpart.test;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TableLayout;

/**
 * 绑定tabLayout与Rv，处理滑动动画
 */
public class TabRecyUtils {
    private TabLayout tabLayout;
    private Context mContext;
    private RecyclerView recyclerView;
    private int lenth = 0;
    private int viewWidth = 0;
    private LinearLayoutManager manager;
    private int state;

    public TabRecyUtils(Context context) {
        this.mContext = context;
        initData();
    }

    public TabRecyUtils(Context context, TabLayout tabLayout, RecyclerView recyclerView) {
        this.mContext = context;
        this.tabLayout = tabLayout;
        this.recyclerView = recyclerView;
        initData();
        initListener();
    }

    private void initData() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        viewWidth = wm.getDefaultDisplay().getWidth();
    }

    public void attachView(TabLayout tableLayout, RecyclerView recyclerView) {
        this.tabLayout = tableLayout;
        this.recyclerView = recyclerView;
        initListener();
    }

    private void initListener() {
        if (tabLayout != null && recyclerView != null) {
            manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    viewWidth = recyclerView.getLayoutManager().getChildAt(0).getWidth();
                }
            });
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    state = newState;
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (state != RecyclerView.SCROLL_STATE_IDLE) {
                        if (lenth >= viewWidth) {
                            lenth = lenth - viewWidth;
                        } else if (lenth <= -viewWidth) {
                            lenth = lenth + viewWidth;
                        }
                        lenth = lenth + dx;
                        float distance = Math.abs((float) lenth / viewWidth);

                        if (dx > 0) {
                            if (distance == 1) {
                                distance = 0;
                            }
                            tabLayout.setScrollPosition(manager.findFirstVisibleItemPosition(), distance, true);
                        } else {
                            tabLayout.setScrollPosition(manager.findFirstVisibleItemPosition(), 1 - distance, true);
                        }
                    }
                }
            });


        }
    }


}
