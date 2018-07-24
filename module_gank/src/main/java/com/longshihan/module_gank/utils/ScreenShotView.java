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
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.longshihan.mvpcomponent.utils.DeviceUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/23 0023
 * @des
 * @function
 */

public class ScreenShotView extends android.support.v7.widget.AppCompatImageView {
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
        File f = new File(filePath);
        if (f == null){
            return ;
        }
        //手动截屏
        Rect rectangle= new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        FileInputStream fis = new FileInputStream(f);
        Bitmap  b = BitmapFactory.decodeStream(fis, null, null);
        b = Bitmap.createBitmap(b, 0, rectangle.top, (int) DeviceUtils.getScreenWidth(context),  rectangle.height());
        fis.close();

        Drawable drawable = getResources().getDrawable(res);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bmm = bd.getBitmap();
        Bitmap resultBitmap=combineBitmap(b,bmm);

        saveBitmap(context,resultBitmap);

        Matrix matrix = new Matrix();
        matrix.setScale(0.3f, 0.3f);

        Bitmap smallBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(),
                resultBitmap.getHeight(), matrix, true);
        setImageBitmap(smallBitmap);
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

    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

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
    public  String saveBitmap(Context context, Bitmap mBitmap) {
        File filePic=null;
        try {
            filePic=context.getExternalFilesDir(DIRECTORY_DCIM);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }
}
