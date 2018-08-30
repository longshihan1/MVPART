package com.longshihan.mvpart.utils;

/**
 * Created by longshihan.
 *
 * @time 2018/7/24 0024
 * @des 截图拼接保存回调
 * @function
 */

public interface OnSaveScreenShotListener {
    void onSaveBitmap(String filePath);
    void onSaveFailure();
}
