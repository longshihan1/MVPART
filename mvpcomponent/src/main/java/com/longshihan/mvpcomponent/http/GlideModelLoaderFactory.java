package com.longshihan.mvpcomponent.http;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * @author longshihan
 * @time 2017/9/7 15:15
 * @des
 */

public class GlideModelLoaderFactory implements ModelLoaderFactory<GlideUrl, InputStream> {
    private static volatile Call.Factory internalClient;
    private Call.Factory client;

    private static Call.Factory getInternalClient() {
        if (internalClient == null) {
            synchronized (GlideModelLoaderFactory.class) {
                if (internalClient == null) {
                    internalClient = new OkHttpClient();
                }
            }
        }
        return internalClient;
    }

    /**
     * Constructor for a new Factory that runs requests using a static singleton client.
     */
    public GlideModelLoaderFactory() {
        this(getInternalClient());
    }

    /**
     * Constructor for a new Factory that runs requests using given client.
     *
     * @param client this is typically an instance of {@code OkHttpClient}.
     */
    public GlideModelLoaderFactory(Call.Factory client) {
        this.client = client;
    }

    @Override
    public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
        return new OkHttpUrlLoader(client);
    }

    @Override
    public void teardown() {
        // Do nothing, this instance doesn't own the client.
    }
}
