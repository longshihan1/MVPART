package com.longshihan.module_dandu.main.ui;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.adapter.ArtRecycleViewAdapter;
import com.longshihan.module_dandu.main.persenter.WordContract;
import com.longshihan.module_dandu.main.persenter.WordPersenter;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.mvpcomponent.base.BaseMVPFragment;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.utils.AppUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class WordTvFragment extends BaseMVPFragment<WordPersenter> implements WordContract.View {
    private int mMessageCount = 0;
    private RecyclerView mRecycler;
    private String deviceId;
    private int page = 1;
    private int mode = 1;
    private ArtRecycleViewAdapter recycleViewAdapter;
    private SmartRefreshLayout mSmartRefreshLayout;

    public WordTvFragment() {
    }


    public void setMessageCount(int messageCount) {
        if (mMessageCount == messageCount) {
            return;
        }
        if (mPresenter == null) {
            return;
        }
        mMessageCount = messageCount;
        mode = messageCount;
        page = 1;
        loadData(page, mMessageCount, "0", deviceId, "0");
    }

    @Override
    public void hideLoading() {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadmore();
    }

    @Override
    public void showError(int type, String message) {
        hideLoading();

    }

    @Override
    public void closeActivity() {

    }

    @Override
    public void showLoading() {

    }


    @Override
    public void showNoData() {

    }

    @Override
    public void showNoMore() {

    }

    @Override
    public void updateListUI(List<Item> itemList) {
        if (page == 1) {
            recycleViewAdapter.replaceAllData(itemList);
        } else {
            page++;
            recycleViewAdapter.setArtList(itemList);
        }
    }

    @Override
    public void showOnFailure() {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dandu_fragment_word_tv, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        deviceId = AppUtil.getDeviceId(mActivity);
        mRecycler = (RecyclerView) mRootview.findViewById(R.id.dandu_artrecycleView);
        mSmartRefreshLayout = getView(R.id.dandu_artrefreshLayout);
        recycleViewAdapter = new ArtRecycleViewAdapter(mContext);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecycler.setAdapter(recycleViewAdapter);
        loadData(page, mMessageCount, "0", deviceId, "0");
        initListener();
    }

    private void initListener() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                loadData(page, mMessageCount, "0", deviceId, "0");
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                loadData(page, mMessageCount, "0", deviceId, "0");
            }
        });
    }

    private void loadData(int page, int mode, String pageId, String deviceId, String createTime) {
        mPresenter.getListByPage(page, mode, pageId, deviceId, createTime);
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
       mPresenter=new WordPersenter(this,appComponent.repositoryManager());
    }
}
