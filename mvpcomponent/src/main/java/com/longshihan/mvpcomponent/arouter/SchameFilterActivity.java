package com.longshihan.mvpcomponent.arouter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.logger.Logger;

/**
 * @author longshihan
 * @time 2017/8/25 10:10
 * @des Arouter通过URL跳转
 */

public class SchameFilterActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        Logger.d(uri.getHost()+":"+uri.getPath());
        ARouter.getInstance().build(uri).navigation();
        finish();
    }
}
