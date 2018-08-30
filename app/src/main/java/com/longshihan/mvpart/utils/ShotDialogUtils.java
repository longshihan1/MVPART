package com.longshihan.mvpart.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.longshihan.mvpart.R;


/**
 * Created by longshihan.
 *
 * @time 2018/7/23 0023
 * @des
 * @function
 */

public class ShotDialogUtils {

    public Context context;
    private ScreenShotListenManager manager;
    private  ScreenShotDialog dialog;
    private   CountDownTimer timer;

    public ShotDialogUtils(Context context) {
        this.context = context;
    }


    public void startScreenShot(){
        manager = ScreenShotListenManager.newInstance(context);
        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    public void onShot(String imagePath) {
                        saveScreenShotJPG(imagePath);
                    }
                }
        );
        manager.startListen();
    }
    //全局弹框
    public void saveScreenShotJPG(final String filepath) {
        if (context == null) {
            return;
        }
        final ShotBitmapUtils bitmapUtils=new ShotBitmapUtils(context);
        bitmapUtils.setListener(new OnSaveScreenShotListener() {
            @Override
            public void onSaveBitmap(String filePath) {
                showDialog(filePath);
                bitmapUtils.onDestory();
            }

            @Override
            public void onSaveFailure() {
                bitmapUtils.onDestory();
            }
        });
        bitmapUtils.addShotPath(filepath,R.drawable.test);
    }

    public void showDialog(final String filepath) {
        /**
         * 拿到修改之后的数据
         */

        //此时已在主线程中，可以更新UI了
        final ScreenShotView screenShotView = new ScreenShotView(context);
        screenShotView.setListener(new OnShowScreenShotListener() {
            @Override
            public void showSuccess() {
                View mView = LayoutInflater.from(context).inflate(R.layout.layout_screenshot, null);
                LinearLayout linCancel = mView.findViewById(R.id.layout_screenshotlin);
                linCancel.addView(screenShotView,0);
                dialog = new ScreenShotDialog(context);
                dialog.setContentView(mView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int displayWidth = dm.widthPixels;
                android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
                p.width = (int) (displayWidth * 0.36);
                p.dimAmount = 0;
                p.gravity = Gravity.RIGHT;
                p.x=20;
                dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                dialog.setCancelable(false);
                dialog.getWindow().setAttributes(p);     //设置生效
                timer = new CountDownTimer(5*1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if (dialog!=null&&dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }.start();
            }

            @Override
            public void showError() {
                //展示不出数据就不显示dialog
                screenShotView.onDestory();
            }
        });
        screenShotView.addShotPath(filepath);
    }

    public void onDestory(){
        if (manager!=null){
            manager.stopListen();
        }
        if (dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
        if (timer!=null){
            timer.cancel();
        }
    }
}
