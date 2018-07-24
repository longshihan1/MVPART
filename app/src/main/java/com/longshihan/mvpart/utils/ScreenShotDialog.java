package com.longshihan.mvpart.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.orhanobut.logger.Logger;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/24 0024
 * @des 处理向右滑动
 * @function
 */

public class ScreenShotDialog extends Dialog {
    private float mPosX;
    private float mCurPosX;

    public ScreenShotDialog(@NonNull Context context) {
        super(context);
    }

    public ScreenShotDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mPosX < mCurPosX) {
                    //向右滑动
                    dismiss();
                }
                break;
        }
        return true;
    }
}
