package com.longshihan.mvpart.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.longshihan.mvpart.R;


/**
 * Created by LONGHE001.
 *
 * @time 2018/7/23 0023
 * @des
 * @function
 */

public class ShotDialogUtils {

    //全局弹框
    public static void showAllDialog(final Activity context, final String filepath) {
        if (context == null) {
            return;
        }
//        if (Looper.getMainLooper() == Looper.myLooper()) {
//            Log.e("TAS", "主线程");
//        } else {
//            Log.e("TAS", "不是主线程(子线程)");
//        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //此时已在主线程中，可以更新UI了
                View mView = LayoutInflater.from(context).inflate(R.layout.layout_screenshot, null);
                LinearLayout linCancel = (LinearLayout) mView.findViewById(R.id.layout_screenshotlin);
                ScreenShotView screenShotView=new ScreenShotView(context);
                try {
                    screenShotView.addShotPath(filepath,R.drawable.back_right);
                } catch (Exception e) {
                    return;
                }
                linCancel.addView(screenShotView);
                Dialog dialog = new Dialog(context);
                dialog.setContentView(mView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int displayWidth = dm.widthPixels;
                int displayHeight = dm.heightPixels;
                android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
                p.width = (int) (displayWidth * 0.8);    //宽度设置为屏幕的0.5
                p.height = (int) (displayHeight * 0.35);    //高度设置为屏幕的0.5
                dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                dialog.setCancelable(false);
                dialog.getWindow().setAttributes(p);     //设置生效
            }
        });
    }
}
