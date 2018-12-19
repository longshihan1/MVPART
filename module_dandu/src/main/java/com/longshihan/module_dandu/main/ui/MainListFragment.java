package com.longshihan.module_dandu.main.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.adapter.VerticalPagerAdapter;
import com.longshihan.module_dandu.main.persenter.MainListContract;
import com.longshihan.module_dandu.main.persenter.MainListPersenter;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.module_dandu.widget.LunarDialog;
import com.longshihan.mvpcomponent.base.BaseMVPFragment;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.AppUtil;
import com.longshihan.mvpcomponent.utils.ArmsUtils;
import com.longshihan.mvpcomponent.utils.PreferenceUtils;
import com.longshihan.mvpcomponent.utils.TimeUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by LONGHE001.
 *
 * @time 2018/12/19 0019
 * @des
 * @function
 */

public class MainListFragment extends BaseMVPFragment<MainListPersenter> implements MainListContract.View {

    VerticalViewPager mVerticalViewPager;
    private VerticalPagerAdapter pagerAdapter;

    private int page = 1;
    private boolean isLoading = true;
    private String deviceId;

    public MainListFragment() {
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
        Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        isLoading = false;
        pagerAdapter.setDataList(itemList);
        page++;
    }

    @Override
    public void showOnFailure() {

    }

    @Override
    public void showLunar(String content) {
        PreferenceUtils.setValue("getLunar", TimeUtil.getDate("yyyyMMdd"));
        LunarDialog lunarDialog = new LunarDialog(mContext);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_lunar, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_iv);
        ArmsUtils.getImageLoader(this).loadImage(
                this, ImageConfigImpl.builder()
                        .url(content)
                        .imageView(imageView).build());
        lunarDialog.setContentView(view);
        lunarDialog.show();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dandu_fragment_main_list, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mVerticalViewPager = mRootview.findViewById(R.id.verticalviewpager);
        deviceId = AppUtil.getDeviceId(mActivity);
        pagerAdapter = new VerticalPagerAdapter(getChildFragmentManager());
        String getLunar = PreferenceUtils.getValue("getLunar", null);
        if (!TimeUtil.getDate("yyyyMMdd").equals(getLunar)) {
            loadRecommend();
        }
        loadData(1, 0, "0", "0");
        initListener();
    }

    private void initListener() {
        mVerticalViewPager.setAdapter(pagerAdapter);
        mVerticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

                if (pagerAdapter.getCount() <= position + 2 && !isLoading) {
                    if (isLoading) {
                        Toast.makeText(mActivity, "正在努力加载...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Logger.i("page=" + page + ",getLastItemId=" + pagerAdapter.getLastItemId());
                    loadData(page, 0, pagerAdapter.getLastItemId(), pagerAdapter
                            .getLastItemCreateTime());
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadRecommend() {
        mPresenter.getRecommend(deviceId);
    }

    private void loadData(int page, int mode, String pageId, String createTime) {
        isLoading = true;
        mPresenter.getListByPage(page, mode, pageId, deviceId, createTime);
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        mPresenter = new MainListPersenter(this, appComponent.repositoryManager());
    }
}
