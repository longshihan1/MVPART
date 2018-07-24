package com.longshihan.mvpart;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.longshihan.mvpart.test.MyViewPagerAdapter;
import com.longshihan.mvpart.test.TabFragment1;
import com.longshihan.mvpart.utils.ScreenShotListenManager;
import com.longshihan.mvpart.utils.ShotDialogUtils;
import com.orhanobut.logger.Logger;

public class Main4Activity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        int s = 0;

        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        final ShotDialogUtils shotDialogUtils=new ShotDialogUtils(this);
        shotDialogUtils.startScreenShot();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new TabFragment1().newInstance("010203040506070809101112131415161718192021222324252627282930313233"), "Tab 1");
        adapter.addFragment(new TabFragment1().newInstance("Page2"), "Tab 2");
        adapter.addFragment(new TabFragment1().newInstance("Page3"), "Tab 3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("viewpage滚动","位置："+position+",偏移："+positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
