package com.longshihan.module_dandu.main.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.adapter.DailyViewPagerAdapter;
import com.longshihan.module_dandu.main.persenter.WordContract;
import com.longshihan.module_dandu.main.persenter.WordPersenter;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.mvpcomponent.base.BaseMVPFragment;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.utils.AppUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyFragment extends BaseMVPFragment<WordPersenter> implements WordContract.View {

    private VerticalViewPager mVerticalViewPager;

    private int page = 1;
    private static final int MODE = 4;
    private boolean isLoading = true;
    private String deviceId;
    private DailyViewPagerAdapter pagerAdapter;

    public DailyFragment() {
        // Required empty public constructor
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
    public void showLoading() {

    }



    @Override
    public void showNoData() {

    }

    @Override
    public void showNoMore() {
        Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        isLoading = false;
        pagerAdapter.setArtList(itemList);
        page++;
    }

    @Override
    public void showOnFailure() {
        if (pagerAdapter.getCount() == 0) {
            showNoData();
        } else {
            Toast.makeText(mContext, "加载数据失败，请检查您的网络", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.dandu_fragment_daily, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mVerticalViewPager = (VerticalViewPager) mRootview.findViewById(R.id.dandu_daily_view_pager);

        pagerAdapter = new DailyViewPagerAdapter(getChildFragmentManager(), mContext);
        mVerticalViewPager.setAdapter(pagerAdapter);
        mVerticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pagerAdapter.getCount() <= position + 2 && !isLoading) {
                    Logger.e("page=" + page + ",getLastItemId=" + pagerAdapter.getLastItemId());
                    mPresenter.getListByPage(page, 0, pagerAdapter.getLastItemId(), deviceId, pagerAdapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        deviceId = AppUtil.getDeviceId(mActivity);
        mPresenter.getListByPage(page, MODE, "0", deviceId, "0");
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
     mPresenter=new WordPersenter(this,appComponent.repositoryManager());
    }
}
