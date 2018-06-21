package com.longshihan.mvpcomponent.http;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;

import java.io.InputStream;

import okhttp3.Call;

/**
 * A simple model loader for fetching media over http/https using OkHttp.
 */
public class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final Call.Factory client;

    public OkHttpUrlLoader(Call.Factory client) {
        this.client = client;
    }


    /**
     * 在这里可以改url的地址
     *
     * @param url
     * @return
     */
    @Override
    public boolean handles(GlideUrl url) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl model, int width, int height,
                                               Options options) {
        return new LoadData<>(model, new OkHttpStreamFetcher(client, model));
    }
}
