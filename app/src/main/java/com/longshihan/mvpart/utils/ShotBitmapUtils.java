package com.longshihan.mvpart.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.longshihan.mvpcomponent.utils.DeviceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by longshihan.
 *
 * @time 2018/7/24 0024
 * @des 截图和二维码拼接，返回文件地址
 * @function
 */

public class ShotBitmapUtils implements OnSaveScreenShotListener {
    private Context context;
    private OnSaveScreenShotListener listener;
    private MyHandler handler;

    public ShotBitmapUtils(Context context) {
        this.context = context;
        handler=new MyHandler(this);
    }

    public void addShotPath(final String filePath, @DrawableRes final int res){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    File f = new File(filePath);
                    if (f == null){
                        handler.sendEmptyMessage(0);
                        return ;
                    }
                    //手动截屏
                    Rect rectangle= new Rect();
                    ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
                    FileInputStream fis = new FileInputStream(f);
                    Bitmap b = BitmapFactory.decodeStream(fis, null, null);
                    b = Bitmap.createBitmap(b, 0, rectangle.top, (int) DeviceUtils.getScreenWidth(context),  rectangle.height());
                    fis.close();
                    Drawable drawable = context.getResources().getDrawable(res);
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    Bitmap bmm = bd.getBitmap();
                    Bitmap resultBitmap=combineBitmap(b,bmm);
                    com.orhanobut.logger.Logger.d("真实大小:"+(resultBitmap.getWidth()*resultBitmap.getHeight()*4)/1024+"KB");
                    saveBitmap(context,resultBitmap);
                }catch (Exception e){
                    handler.sendEmptyMessage(1);
                }

            }
        }).start();



    }


    public  Bitmap combineBitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width/second.getWidth(), (float) width/second.getWidth());
        second = Bitmap.createBitmap(second, 0, 0, second.getWidth(), second.getHeight(),matrix,false);
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0,first.getHeight(), null);
        return result;
    }

    /**
     * 随机生产文件名
     *
     * @return
     */
    private  String generateFileName() {
        return UUID.randomUUID().toString();
    }


    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public  void saveBitmap(Context context, Bitmap mBitmap) {
        File filePic=null;
        File file=null;
        FileOutputStream fos=null;
        try {
            filePic=context.getExternalFilesDir(DIRECTORY_PICTURES);
            if (filePic.exists()) {
                filePic.mkdirs();//多级目录
            }
            file=new File(filePic,generateFileName()+".jpeg");
            fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (Exception e) {
            handler.sendEmptyMessage(3);
        }finally {
            try {
                fos.flush();
                fos.close();
            } catch (Exception e) {}
        }
        Message message=new Message();
        message.obj= file.getAbsolutePath();
        com.orhanobut.logger.Logger.d("保存路径："+ file.getAbsolutePath());
        message.what=100;
        handler.sendMessage(message);
    }

    public void setListener(OnSaveScreenShotListener listener) {
        this.listener = listener;

    }

    @Override
    public void onSaveBitmap(String filePath) {
        if (listener!=null){
            listener.onSaveBitmap(filePath);
        }
    }

    @Override
    public void onSaveFailure() {
        if (listener!=null){
            listener.onSaveFailure();
        }
    }

    public class MyHandler extends Handler {
        private OnSaveScreenShotListener listener;
        public MyHandler(OnSaveScreenShotListener listener) {
            this.listener=listener;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                String filePath = (String) msg.obj;
                if (!TextUtils.isEmpty(filePath)) {
                    listener.onSaveBitmap(filePath);
                }else {
                    listener.onSaveFailure();
                }
            }else {
                listener.onSaveFailure();
            }
        }
    }

    /**
     * 销毁存在的数据
     */
    public void onDestory(){
        handler.removeCallbacks(null);
    }
}
