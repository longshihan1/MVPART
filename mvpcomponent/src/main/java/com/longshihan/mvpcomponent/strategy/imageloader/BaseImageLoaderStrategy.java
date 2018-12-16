package com.longshihan.mvpcomponent.strategy.imageloader;

import android.content.Context;

/**
 * @author Administrator
 * @time 2017/8/10 15:17
 * @des 类作用：
 */

public interface BaseImageLoaderStrategy<T extends ImageConfig> {
    /**
     * 加载图片
     *
     * @param ctx {@link Context}
     * @param config 图片加载配置信息
     */
    void loadImage(Context ctx, T config);
    /**
     * 停止加载
     *
     * @param ctx {@link Context}
     * @param config 图片加载配置信息
     */
    void clear(Context ctx, T config);
}
