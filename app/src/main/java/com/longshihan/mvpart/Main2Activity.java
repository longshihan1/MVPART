package com.longshihan.mvpart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.longshihan.baseadapter.MultiTypeAdapter;
import com.longshihan.mvpart.arch.model.StringItem;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class Main2Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MultiTypeAdapter adapter;
    private LinearLayoutManager manager;
    int scrolldistance = 0;
    int maxscrolldistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.main_rv2);
        adapter = new MultiTypeAdapter();
        adapter.register(StringItem.class, new StringTextItemViewBinder());
        List<StringItem> strings = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //dx > 0 时为手指向左滚动,列表滚动显示右面的内容
            //dx < 0 时为手指向右滚动,列表滚动显示左面的内容
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //获取最后一个完全显示的ItemPosition
                int lastVisiblePositions = manager.findLastVisibleItemPosition();
                int firstVisiblePositions = manager.findFirstVisibleItemPosition();
                if (firstVisiblePositions == lastVisiblePositions) {//不考虑相等的情况
                    return;
                }
                scrolldistance += Math.abs(dx);
                if (dx > 0) {
                    getleftScollYDistance();
                    Log.d("左滑", getleftScollYDistance() + ":" + manager.findFirstVisibleItemPosition() + ":" + manager.findLastVisibleItemPosition());
                } else {
                    getRightScollYDistance();
                    Log.d("右滑", getRightScollYDistance() + ":" + manager.findFirstVisibleItemPosition() + ":" + manager.findLastVisibleItemPosition());

                }
            }
        });
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
