package com.longshihan.mvpcomponent.strategy.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;

/**
 * @author longshihan
 * @time 2017/9/6 15:42
 * @des
 */

public interface GlideAppliesOptions {
    /**
     * 配置 Glide 的自定义参数,此方法在 Glide 初始化时执行(Glide 在第一次被调用时初始化),只会执行一次
     *
     * @param context
     * @param builder {@link GlideBuilder} 此类被用来创建 Glide
     */
    void applyGlideOptions(Context context, GlideBuilder builder);
}
