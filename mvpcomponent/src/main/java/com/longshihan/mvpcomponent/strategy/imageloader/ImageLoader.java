package com.longshihan.mvpcomponent.strategy.imageloader;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;


/**
 * @author Administrator
 * @time 2017/8/10 14:00
 * @des 类作用：管理图片的框架的入口，使用策略模式
 */
public class ImageLoader {
    private BaseImageLoaderStrategy mStrategy;

    public ImageLoader(BaseImageLoaderStrategy strategy) {
        setLoadImgStrategy(strategy);
    }

    public <T extends ImageConfig> void loadImage(Activity activity, T config) {
        loadImage((Context) activity, config);
    }

    public <T extends ImageConfig> void clear(Activity activity, T config) {
        clear((Context) activity, config);
    }

    public <T extends ImageConfig> void loadImage(Fragment fragment, T config) {
        loadImage(fragment.getActivity(), config);
    }

    public <T extends ImageConfig> void clear(Fragment fragment, T config) {
        clear(fragment.getActivity(), config);
    }

    public <T extends ImageConfig> void loadImage(android.support.v4.app.Fragment fragment, T config) {
        loadImage(fragment.getActivity(), config);
    }

    public <T extends ImageConfig> void clear(android.support.v4.app.Fragment fragment, T config) {
        clear(fragment.getActivity(), config);
    }

    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }

    public <T extends ImageConfig> void clear(Context context, T config) {
        this.mStrategy.clear(context, config);
    }


    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }

    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }
}
