package com.longshihan.mvpart;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.longshihan.baseadapter.MultiTypeAdapter;
import com.longshihan.mvpart.test.TabRecyUtils;
import com.longshihan.mvpart.arch.model.StringItem;
import com.longshihan.mvpart.test.MaxHeightView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MultiTypeAdapter adapter;
    private LinearLayoutManager manager;
    int scrolldistance = 0;
    int maxscrolldistance = 0;
    private MaxHeightView heightView;int lenth=0;
    TabLayout mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.main_rv2);
        heightView=findViewById(R.id.main_maxview);
        mTitles=findViewById(R.id.tlTabTitle);
        heightView.setMaxHeight(300);
        adapter = new MultiTypeAdapter();
        adapter.register(StringItem.class, new StringTextItemViewBinder());
        List<StringItem> strings = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mTitles.addTab(mTitles.newTab().setText("测试" + i));
            if (i % 2 == 0) {
                strings.add(new StringItem("测试" + i, R.color.colorAccent));
            } else {
                strings.add(new StringItem("测试" + i, R.color.colorPrimary));
            }
        }

        adapter.setItems(strings);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        TabRecyUtils tabRecyUtils=new TabRecyUtils(this);
        tabRecyUtils.attachView(mTitles,recyclerView);
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState==RecyclerView.SCROLL_STATE_IDLE){
//                    lenth=0;
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lenth=lenth+dx;
//                mTitles.setScrollPosition((manager.findFirstVisibleItemPosition()+manager.findLastVisibleItemPosition())/2,0.5f,true);
//                Log.d("左滑", dx + ":" +lenth+":::"+ manager.findFirstVisibleItemPosition() + ":" + manager.findLastVisibleItemPosition());
//
//                if (manager.findFirstVisibleItemPosition() == 0) {
//
//                    // 位置为0的时候传递偏移量给mainactivity的scroollTabLayout处理tablayout的滚动
////                    element.setAppBarOffset(dy);
////                    EventBus.getDefault().post(element, "scroollTabLayout");
//                } else {
//                    // 位置不为0的情况是快速滑动的时候，传递12345作为快速向下滚动的标志,mainactivity的scroollTabLayout会针对此情况做特殊处理
////                    element.setAppBarOffset(12345);
////                    EventBus.getDefault().post(element, "scroollTabLayout");
//                }
//            }
//        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState==RecyclerView.SCROLL_STATE_IDLE){
//                    lenth=0;
//                }
//
//            }
//
//            //dx > 0 时为手指向左滚动,列表滚动显示右面的内容
//            //dx < 0 时为手指向右滚动,列表滚动显示左面的内容
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lenth=lenth+dx;
//                Log.d("左滑", dx + ":" +lenth+":::"+ manager.findFirstVisibleItemPosition() + ":" + manager.findLastVisibleItemPosition());
//
//                //获取最后一个完全显示的ItemPosition
////                int lastVisiblePositions = manager.findLastVisibleItemPosition();
////                int firstVisiblePositions = manager.findFirstVisibleItemPosition();
////                if (firstVisiblePositions == lastVisiblePositions) {//不考虑相等的情况
////                    return;
////                }
////                scrolldistance += Math.abs(dx);
////                if (dx > 0) {
////                    getleftScollYDistance();
////                    Log.d("左滑", getleftScollYDistance() + ":" + manager.findFirstVisibleItemPosition() + ":" + manager.findLastVisibleItemPosition());
////                } else {
////                    getRightScollYDistance();
////                    Log.d("右滑", getRightScollYDistance() + ":" + manager.findFirstVisibleItemPosition() + ":" + manager.findLastVisibleItemPosition());
////
////                }
//            }
//        });
    }





    public int getRightScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisiableChildView.getWidth();
//        Log.d("测试右滑", itemWidth + ":" + firstVisiableChildView.getLeft() + "");
        return itemWidth - Math.abs(firstVisiableChildView.getLeft());
    }

    public int getleftScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisiableChildView.getWidth();
//        Log.d("测试左滑", itemWidth + ":" + firstVisiableChildView.getLeft() + "");
        return itemWidth - Math.abs(firstVisiableChildView.getLeft());
    }

}
