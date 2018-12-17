package com.longshihan.module_dandu.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.longshihan.module_dandu.mvp.model.entity.SplashEntity;
import com.longshihan.module_dandu.tools.OkHttpImageDownloader;


/**
 * 下载loading界面上的图片
 */
public class DownloadImageService extends IntentService {
    private static final String ACTION_UPLOAD_IMG = "com.zhy.blogcodes.intentservice.action.UPLOAD_IMAGE";
    public static final String EXTRA_IMG_PATH = "com.zhy.blogcodes.intentservice.extra.IMG_PATH";

    public static void startUploadImg(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DownloadImageService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtras(bundle);
        context.startService(intent);
    }


    public DownloadImageService() {
        super("UploadImgService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMG.equals(action)) {
                Bundle bundle = intent.getExtras();
                SplashEntity entity = (SplashEntity) bundle.getSerializable("map");
                for (int i = 0; i < entity.getImages().size(); i++) {
                    OkHttpImageDownloader.download(entity.getImages().get(i));
                }
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy");
    }


}
