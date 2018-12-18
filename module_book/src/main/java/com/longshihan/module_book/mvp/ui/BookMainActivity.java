package com.longshihan.module_book.mvp.ui;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.longshihan.module_book.R;
import com.longshihan.permissionlibrary.CheckPermissions;

public class BookMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_main);
        CheckPermissions checkPermissions = new CheckPermissions(this);
        checkPermissions.setLisener(permission -> {
            if (permission.granted) {
                Log.i("permissions", Manifest.permission.READ_CALENDAR + "：获取成功");
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
    }
}
