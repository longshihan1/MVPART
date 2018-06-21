package com.longshihan.module_gank.mvp.MainModule.ui.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.longshihan.module_gank.R;
import com.longshihan.module_gank.mvp.MainModule.ui.fragment.AndroidFragment;
import com.longshihan.module_gank.mvp.MainModule.ui.fragment.FrontEndFragment;
import com.longshihan.module_gank.mvp.MainModule.ui.fragment.IOSFragment;
import com.longshihan.module_gank.mvp.MainModule.ui.fragment.TodayGankFragment;
import com.longshihan.module_gank.mvp.MainModule.ui.fragment.VideoFragment;
import com.longshihan.module_gank.mvp.MainModule.ui.fragment.WelfareFragment;

@Route(path = "/gank/MainActivity")
public class GankMainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener {
    private FragmentTransaction mTransition;
    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;
    DrawerLayout drawer;
    private TodayGankFragment mTodayGankFragment;
    private WelfareFragment mWelfareFragment;
    private AndroidFragment mAndroidFragment;
    private FrontEndFragment mFrontEndFragment;
    private IOSFragment mIOSFragment;
    private VideoFragment mVideoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toutiao_activity_main);

        initdata();
    }

    private void initdata() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initFragment();

    }

    private void initFragment() {
        mTodayGankFragment = new TodayGankFragment();
        mWelfareFragment = new WelfareFragment();
        mAndroidFragment = new AndroidFragment();
        mFrontEndFragment = new FrontEndFragment();
        mIOSFragment = new IOSFragment();

        mFragmentManager = getSupportFragmentManager();
        mTransition = mFragmentManager.beginTransaction();
        mTransition.replace(R.id.fragment_container, mTodayGankFragment);
        mTransition.commitAllowingStateLoss();
    }


    /**
     * 隐藏Fragment
     *
     * @param ft
     * @param fragmentManager
     */
    public void hideFragment(FragmentTransaction ft, FragmentManager fragmentManager) {
        //如果不为空，就先隐藏起来
        for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
            if (fragmentManager.getFragments().get(i).isAdded()) {
                ft.hide(fragmentManager.getFragments().get(i));
            }
        }
    }

    /**
     * 展示fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        mTransition = mFragmentManager.beginTransaction();
        hideFragment(mTransition, mFragmentManager);
        if (!fragment.isAdded()) {
            mTransition.add(R.id.fragment_container, fragment);
            mTransition.show(fragment);
        } else {
            mTransition.show(fragment);
        }
        mTransition.commitAllowingStateLoss();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //startActivity(SearchActivity.newIntent(this));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_today:
                showFragment(mTodayGankFragment);
                break;
            case R.id.nav_welfare:
                showFragment(mWelfareFragment);
                break;
            case R.id.nav_android:
                showFragment(mAndroidFragment);
                break;
            case R.id.nav_ios:
                showFragment(mIOSFragment);
                break;
            case R.id.nav_front_end:
                showFragment(mFrontEndFragment);
                break;
            case R.id.nav_video:
                showFragment(mVideoFragment);
                break;
            case R.id.nav_about:
                //startActivity(AboutActivity.newIntent(this));
                break;
            case R.id.nav_feedback:
                //startFeedbackActivity();
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}