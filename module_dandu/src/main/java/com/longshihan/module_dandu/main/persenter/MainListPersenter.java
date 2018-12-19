package com.longshihan.module_dandu.main.persenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.google.gson.internal.LinkedTreeMap;
import com.longshihan.module_dandu.main.model.IMainListModel;
import com.longshihan.module_dandu.main.model.MainListModel;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.module_dandu.mvp.model.entity.Result;
import com.longshihan.mvpcomponent.mvp.BasePresenter;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;
import com.longshihan.mvpcomponent.utils.TimeUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LONGHE001.
 *
 * @time 2018/12/19 0019
 * @des
 * @function
 */

public class MainListPersenter extends BasePresenter<MainListContract.View> implements
        MainListContract.Presenter {
    private Context mContext;
    private IMainListModel mIModel;

    public MainListPersenter(MainListContract.View rootView, IRepositoryManager manager) {
        super(rootView);
        //mContext = (Context) rootView;
        mIModel = new MainListModel(manager);

    }


    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
        super.onDestroy(owner);
    }

    @Override
    public void getListByPage(int page, int model, String pageId, String deviceId, String
            createTime) {
        Disposable disposable = mIModel.getAD(page, model, pageId, createTime, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result.Data<List<Item>>>() {
                    @Override
                    public void accept(@NonNull Result.Data<List<Item>> listData) throws Exception {
                        int size = listData.getDatas().size();
                        if (size > 0) {
                            mRootView.updateListUI(listData.getDatas());
                        } else {
                            mRootView.showNoMore();
                        }
                    }
                });
        addDispose(disposable);
    }

    @Override
    public void getRecommend(String deviceId) {
        Disposable commendDis = mIModel.getCommond(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result.Data>() {
                    @Override
                    public void accept(@NonNull Result.Data result) throws Exception {
                        String key = TimeUtil.getDate("yyyyMMdd");
                        try {
                            LinkedTreeMap rootTreeMap = (LinkedTreeMap) result.getDatas();
                            LinkedTreeMap childtreeMap = (LinkedTreeMap) rootTreeMap.get(key);
                            mRootView.showLunar((String) childtreeMap.get("thumbnail"));
                        } catch (Exception e) {
                            mRootView.showOnFailure();
                            e.printStackTrace();
                        }
                    }

                });
        addDispose(commendDis);
    }

}
