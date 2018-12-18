package com.longshihan.module_dandu.splash.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.splash.persenter.SplashContract;
import com.longshihan.module_dandu.splash.persenter.SplashPersenter;
import com.longshihan.module_dandu.widget.FixedImageView;
import com.longshihan.mvpcomponent.base.BaseMVPActivity;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.GlideArms;
import com.longshihan.mvpcomponent.utils.AppUtil;
import com.longshihan.mvpcomponent.utils.ArmsUtils;
import com.longshihan.mvpcomponent.utils.FileUtil;
import com.longshihan.mvpcomponent.utils.PreferenceUtils;
import com.longshihan.permissionlibrary.CheckPermissions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class SplashActivity extends BaseMVPActivity<SplashPersenter> implements SplashContract.View {

    FixedImageView splashImg;

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
    public void setupActivityComponent(AppComponent appComponent) {
        mPresenter = new SplashPersenter(this, appComponent.repositoryManager());
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.dandu_activity_splash;
    }

    @Override
    public void initData() {
        splashImg = findViewById(R.id.splash_img);
        initStatus();
        doReadFilePermisssion();
    }

    /**
     * 获取权限
     */
    private void doReadFilePermisssion() {
        CheckPermissions checkPermissions = new CheckPermissions(this);
        checkPermissions.setLisener(permission -> {
            if (permission.granted) {
                Log.i("permissions", Manifest.permission.READ_CALENDAR + "：获取成功");
                delaySplash();
            } else {
                Log.i("permissions", Manifest.permission.READ_CALENDAR + "：获取失败");
            }
        });
        checkPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE
        );
        delaySplash();
    }

    private void delaySplash() {
        List<String> picList = FileUtil.getAllAD();
        if (picList.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(picList.size());
            int imgIndex = PreferenceUtils.getValue("splash_img_index", 0);
            if (index == imgIndex) {
                if (index >= picList.size()) {
                    index--;
                } else if (imgIndex == 0) {
                    if (index + 1 < picList.size()) {
                        index++;
                    }
                }
            }
            PreferenceUtils.setValue("splash_img_index", index);
            File file = new File(picList.get(index));
            ArmsUtils.ConfigGlideRequest(this, GlideArms.with(this).load(file), splashImg);
        } else {
            try {
                AssetManager assetManager = this.getAssets();
                InputStream in = assetManager.open("welcome_default.jpg");
                ArmsUtils.ConfigGlideRequest(this, GlideArms.with(this).load(in), splashImg);
                animWelcomeImage();
                in.close();
                String deviceId = AppUtil.getDeviceId(SplashActivity.this);
                mPresenter.StartTask(deviceId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public Drawable InputStream2Drawable(InputStream is) {
        Drawable drawable = BitmapDrawable.createFromStream(is, "splashImg");
        return drawable;
    }

    private void animWelcomeImage() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(splashImg, "translationX", -100F);
        animator.setDuration(1500L).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                Intent intent = new Intent(SplashActivity.this, DanduActivitymainActivity2.class);
//                startActivity(intent);
//                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public boolean useFragment() {
        return false;
    }

}
