package com.longshihan.module_gank.mvp.MeiZiModule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.longshihan.module_gank.R;
import com.longshihan.module_gank.http.GankApi;
import com.longshihan.module_gank.mvp.MainModule.model.entity.Meizi;
import com.longshihan.module_gank.utils.DateUtil;
import com.longshihan.module_gank.utils.ShareElement;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.ArmsUtils;
import com.longshihan.mvpcomponent.utils.FileUtil;
import com.longshihan.mvpcomponent.utils.LogUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MeiZhiActivity extends AppCompatActivity {
    Meizi meizi;
    private Toolbar toolbar;
    private AppBarLayout app_bar;
    private ImageView iv_meizhi;
    PhotoViewAttacher attacher;
    Bitmap girl;
    protected boolean isToolBarHiding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mei_zhi);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        iv_meizhi = (ImageView) findViewById(R.id.iv_meizhi);
        setSupportActionBar(toolbar);
        getIntentData();
        initMeizhiView();

    }

    private void initMeizhiView() {
        setTitle(DateUtil.toDateTimeStr(meizi.publishedAt));
        //iv_meizhi.setImageDrawable(ShareElement.shareDrawable);
        ViewCompat.setTransitionName(iv_meizhi, GankApi.TRANSLATE_GIRL_VIEW);
        attacher = new PhotoViewAttacher(iv_meizhi);

        AppComponent component= ArmsUtils.obtainAppComponentFromContext(this);
        LogUtils.debugInfo("测试",component.extras().keySet().toString());

        ArmsUtils.getImageLoader(this).loadImage(
                this,
                ImageConfigImpl.builder().url(meizi.url)
                        .targets(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                iv_meizhi.setImageBitmap(resource);
                                attacher.update();
                                girl = resource;
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                iv_meizhi.setImageDrawable(errorDrawable);
                            }
                        })
                        .imageView(iv_meizhi)
                        .build());
      /*  attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                hideOrShowToolBar();
            }

            @Override
            public void onOutsidePhotoTap() {
                //hideOrShowToolBar();
            }
        });*/
    }

    public void getIntentData() {
        meizi = (Meizi) getIntent().getSerializableExtra(GankApi.MEIZI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_girl, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveMeizhiImage(girl, DateUtil.toDateString(meizi.publishedAt).toString());
                break;
            case R.id.action_share:
                Toast.makeText(MeiZhiActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareElement.shareDrawable = null;
        if (girl!=null) {
            girl.recycle();
        }
        girl=null;
    }

    protected void hideOrShowToolBar() {
        app_bar.animate()
                .translationY(isToolBarHiding ? 0 : -app_bar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        isToolBarHiding = !isToolBarHiding;
    }

    public void saveMeizhiImage(final Bitmap bitmap, final String title) {
        Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> e) throws Exception {
                Uri uri = FileUtil.saveBitmapToSDCard(bitmap, title);
                if (uri == null) {
                    e.onError(new Exception(getString(R.string.girl_reject_your_request)));
                } else {
                    e.onNext(uri);
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) {
                        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                        sendBroadcast(scannerIntent);
                        Toast.makeText(MeiZhiActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
