package com.longshihan.mvpart.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.longshihan.mvpcomponent.utils.DeviceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.logging.Logger;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by longshihan.
 *
 * @time 2018/7/23 0023
 * @des
 * @function
 */

public class ScreenShotView extends android.support.v7.widget.AppCompatImageView {
    private Context context;
    private MyHandler handler = null;
    private OnShowScreenShotListener listener;

    public ScreenShotView(Context context) {
        this(context,null);
    }

    public ScreenShotView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScreenShotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContext(context);
    }


    private void initContext(Context context) {
        this.context=context;
    }

    public void addShotPath(final String filePath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                 try{
                     File f = new File(filePath);
                     if (f == null){
                         handler.sendEmptyMessage(2);
                         return ;
                     }
                     FileInputStream fis = new FileInputStream(f);
                     BitmapFactory.Options options=new BitmapFactory.Options();
                     options.inSampleSize=3;
                     options.inPreferredConfig= Bitmap.Config.RGB_565;
                     Bitmap  b = BitmapFactory.decodeStream(fis, null, options);
                     b=Bitmap.createBitmap(b,0,b.getHeight()/4,b.getWidth(),b.getHeight()/2);
                     com.orhanobut.logger.Logger.d("显示大小:"+(b.getWidth()*b.getHeight()*2)/1024+"KB");
                     fis.close();
                     Message message=new Message();
                     message.obj=b;
                     message.what=0;
                     handler.sendMessage(message);
                 }catch (Exception e){
                     handler.sendEmptyMessage(1);
                 }

            }
        }).start();
    }

    public void setListener(OnShowScreenShotListener listener) {
        this.listener = listener;
        handler=new MyHandler(this,getScreenShotListener());
    }

    public OnShowScreenShotListener getScreenShotListener() {
        return new OnShowScreenShotListener() {
            @Override
            public void showSuccess() {
                if (listener!=null){
                    listener.showSuccess();
                }
            }

            @Override
            public void showError() {
                if (listener!=null){
                    listener.showError();
                }
            }
        };
    }

    /**
     * 持有imageview的变量
     */
    public class MyHandler extends Handler {
        private final WeakReference<ImageView> imageViewWeakReference;
        private OnShowScreenShotListener listener;
        public MyHandler(ImageView imageView,OnShowScreenShotListener showScreenShotListener) {
           imageViewWeakReference=new WeakReference<ImageView>(imageView);
           this.listener=showScreenShotListener;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Bitmap bitmap = (Bitmap)msg.obj;
                if(bitmap != null&&imageViewWeakReference.get()!=null) {
                    imageViewWeakReference.get().setImageBitmap(bitmap);
                    listener.showSuccess();
                }else {
                    listener.showError();
                }
            }else {
                listener.showError();
            }
        }
    }

    public void onDestory() {
        handler.removeCallbacks(null);
    }

}
