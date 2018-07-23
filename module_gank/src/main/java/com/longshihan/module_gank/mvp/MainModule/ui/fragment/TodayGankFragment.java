package com.longshihan.module_gank.mvp.MainModule.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.module_gank.R;
import com.longshihan.module_gank.adapter.MeiZiAdapter;
import com.longshihan.module_gank.mvp.MainModule.contract.TodayGankContract;
import com.longshihan.module_gank.mvp.MainModule.model.entity.Meizi;
import com.longshihan.module_gank.mvp.MainModule.model.entity.MeiziData;
import com.longshihan.module_gank.mvp.MainModule.persenter.TodayPersenter;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.base.BaseMVPFragment;
import com.longshihan.mvpcomponent.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class TodayGankFragment extends BaseMVPFragment<TodayPersenter> implements TodayGankContract.View {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;
    private List<Meizi> meizis;
    private MeiZiAdapter adapter;
    private int page = 1;
    private boolean isRefresh = true;
    private boolean canLoading = true;


    public TodayGankFragment() {
        // Required empty public constructor
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(int type, String message) {

    }

    @Override
    public void closeActivity() {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_gank, container, false);
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        mRecyclerView = getView(R.id.gank_todayrecycleView);
        mSmartRefreshLayout = getView(R.id.gank_todayrefreshLayout);

        adapter = new MeiZiAdapter(mContext, new ArrayList<Meizi>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        loadData(page);
        initListener();
    }


    private void initListener() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                loadData(page);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                loadData(page);
            }
        });
    }

    private void loadData(int page) {
        mPresenter.getDetail(page + "");
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        mPresenter=new TodayPersenter(this, ArmsUtils.obtainAppComponentFromContext(this).repositoryManager());
    }

    @Override
    public void updateListUI(MeiziData meiziData) {
        if (page == 1) {
            mSmartRefreshLayout.finishRefresh();
            adapter.replaceList(meiziData.results);
        } else {
            mSmartRefreshLayout.finishLoadmore();
            adapter.appendList(meiziData.results);
        }
    }

    @Override
    public void showOnFailure() {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadmore();
    }
}
