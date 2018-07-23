package com.longshihan.module_gank.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.longshihan.mvpcomponent.utils.DeviceUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/23 0023
 * @des
 * @function
 */

public class ScreenShotView extends android.support.v7.widget.AppCompatImageView {

    private Bitmap b=null;
    private Context context;

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

    public void addShotPath(String filePath,@DrawableRes int res,int optionSize) throws Exception {
        int IMAGE_MAX_SIZE = (int) (DeviceUtils.getScreenWidth(context)/optionSize);

        File f = new File(filePath);
        if (f == null){
            return ;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //手动截屏
        Rect rectangle= new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        b = Bitmap.createBitmap(b, 0, rectangle.top/scale, (int) DeviceUtils.getScreenWidth(context)/scale,  rectangle.height()/scale);
        fis.close();

        Drawable drawable = getResources().getDrawable(res);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bmm = bd.getBitmap();
        setImageBitmap(combineBitmap(b,bmm));
    }



    public  Bitmap combineBitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width/second.getWidth(), (float) width/second.getWidth());// 使用后乘
        second = Bitmap.createBitmap(second, 0, 0, second.getWidth(), second.getHeight(),matrix,false);
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0,first.getHeight(), null);
        return result;
    }
}
