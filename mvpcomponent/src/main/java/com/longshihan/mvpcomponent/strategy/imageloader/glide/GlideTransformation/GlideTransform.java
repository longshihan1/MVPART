package com.longshihan.mvpcomponent.strategy.imageloader.glide.GlideTransformation;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @author longshihan
 * @time 2017/9/7 11:47
 * @des
 */

public class GlideTransform extends BitmapTransformation {
    public GlideTransform() {
        super();
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return new GlideEngine(toTransform,outWidth,outHeight).compress();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
