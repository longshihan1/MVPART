package com.longshihan.module_dandu.main.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.mvp.model.entity.LeftMenuEvent;
import com.longshihan.module_dandu.utils.DanDuConstacts;
import com.longshihan.mvpcomponent.base.BaseMVPActivity;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DanduActivitymainActivity2 extends BaseMVPActivity {
    DrawerLayout mDrawerLayout;
    private MainListFragment mMainListFragment;
    private LeftFragment blankFragment;
    private WordTvFragment mWordTvFragment;
    private DailyFragment mDailyFragment;
    private FragmentTransaction mTransition;
    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;
    private TextView mTextView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.dandu_activitymain;
    }

    @Override
    public void initData() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mToolbar = (Toolbar) findViewById(R.id.dandu_maintoolbar);
        mToolbar.setTitle("");
        mTextView = (TextView) findViewById(R.id.dandu_maintoolbartext);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        EventBus.getDefault().register(this);
        blankFragment = new LeftFragment();
        mMainListFragment = new MainListFragment();
        mWordTvFragment = new WordTvFragment();
        mDailyFragment = new DailyFragment();
        mFragmentManager = getSupportFragmentManager();
        mTransition = mFragmentManager.beginTransaction();
        mTransition.replace(R.id.dandu_mainfragment, mMainListFragment);
        mTransition.commitAllowingStateLoss();
        mFragmentManager.findFragmentById(R.id.dandu_mainlefft);
    }

    /**
     * 隐藏Fragment
     *
     * @param ft
     */
    public void hideFragment(FragmentTransaction ft) {
        //如果不为空，就先隐藏起来
        if (mMainListFragment != null) {
            ft.hide(mMainListFragment);
        }
        if (mWordTvFragment != null) {
            ft.hide(mWordTvFragment);
        }
        if (mDailyFragment != null) {
            ft.hide(mDailyFragment);
        }
    }

    /**
     * 展示fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        mTransition = mFragmentManager.beginTransaction();
        hideFragment(mTransition);
        if (!fragment.isAdded()) {
            mTransition.add(R.id.dandu_mainfragment, fragment);
            mTransition.show(fragment);
        } else {
            mTransition.show(fragment);
        }
        mTransition.commitAllowingStateLoss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawers();
            } else
                super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(LeftMenuEvent messageEvent) {
        mDrawerLayout.closeDrawers();
        String msg = messageEvent.msg;
        Logger.d(messageEvent.toString());
        if (DanDuConstacts.HomePageTV.equals(msg)) {
            showFragment(mMainListFragment);
            mTextView.setText("单读");
        } else if (DanDuConstacts.WordsTV.equals(msg)) {
            mTextView.setText("文字");
            showFragment(mWordTvFragment);
            mWordTvFragment.setMessageCount(1);
        } else if (DanDuConstacts.VoiceTV.equals(msg)) {
            mTextView.setText("声音");
            showFragment(mWordTvFragment);
            mWordTvFragment.setMessageCount(3);
        } else if (DanDuConstacts.VodeoTV.equals(msg)) {
            mTextView.setText("影像");
            showFragment(mWordTvFragment);
            mWordTvFragment.setMessageCount(2);
        } else if (DanDuConstacts.CalendarTv.equals(msg)) {
            mTextView.setText("单向历");
            showFragment(mDailyFragment);
        } else if (DanDuConstacts.MessageLinear.equals(msg)) {
            mTextView.setText("消息");
        } else if (DanDuConstacts.CollLinear.equals(msg)) {
            mTextView.setText("收藏");
        } else if (DanDuConstacts.NolineLinear.equals(msg)) {
            mTextView.setText("离线");
        } else if (DanDuConstacts.NoteLinear.equals(msg)) {
            mTextView.setText("笔记");
        } else if (DanDuConstacts.TitleBar.equals(msg)) {

        } else if (DanDuConstacts.Login.equals(msg)) {

        }
    }

    @Override
    public boolean useFragment() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
