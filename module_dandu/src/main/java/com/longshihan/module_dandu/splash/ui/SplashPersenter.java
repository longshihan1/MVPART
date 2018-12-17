package com.longshihan.module_dandu.splash.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;

import com.longshihan.module_dandu.mvp.model.entity.SplashEntity;
import com.longshihan.module_dandu.service.DownloadImageService;
import com.longshihan.mvpcomponent.mvp.BasePresenter;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.utils.NetUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LONGHE001.
 *
 * @time 2018/12/17 0017
 * @des
 * @function
 */

public class SplashPersenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    private Context mContext;
    private ISplashModel mIModel;

    public SplashPersenter(SplashContract.View rootView, IRepositoryManager manager) {
        super(rootView);
        mContext = (Context) rootView;
        mIModel = new SplashModel(manager);

    }

    @Override
    public void StartTask(String deviceId) {
        Disposable disposable = mIModel.getAD(1, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<SplashEntity>() {
                    @Override
                    public void accept(@NonNull SplashEntity splashEntity) throws Exception {
                        Logger.d("-------------" + splashEntity.getImages());
                        if (NetUtil.isWifi(mContext)) {
                            if (splashEntity != null) {
                                List<String> imgs = splashEntity.getImages();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("map", splashEntity);
                                DownloadImageService.startUploadImg(mContext, bundle);
                                for (String url : imgs) {

                                }
                            }
                        } else {
                            Logger.i("不是WIFI环境,就不去下载图片了");
                        }
                    }
                });
        addDispose(disposable);
    }
}
