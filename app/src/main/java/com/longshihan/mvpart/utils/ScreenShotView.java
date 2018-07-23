package com.longshihan.mvpart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.longshihan.mvpcomponent.utils.DeviceUtils;

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

    public void addShotPath(String filePath,@DrawableRes int res) throws Exception {

        int IMAGE_MAX_SIZE = 600;

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

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        b = Bitmap.createBitmap(b, 0,  (int)DeviceUtils.getStatusBarHeight(context), (int) DeviceUtils.getScreenWidth(context),  (int)DeviceUtils.getScreenHeight(context) -  (int)DeviceUtils.getStatusBarHeight(context));
        fis.close();

        Drawable drawable = getResources().getDrawable(res);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bmm = bd.getBitmap();
        setImageBitmap(combineBitmap(b,bmm));
    }



    /**

     * 合并两张bitmap为一张
     * @param background
     * @param foreground
     * @return Bitmap
     */
    public  Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2, (bgHeight - fgHeight) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }
}
