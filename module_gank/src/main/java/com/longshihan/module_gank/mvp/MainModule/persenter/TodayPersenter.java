package com.longshihan.module_gank.mvp.MainModule.persenter;


import com.longshihan.module_gank.mvp.MainModule.contract.TodayGankContract;
import com.longshihan.module_gank.mvp.MainModule.model.entity.MeiziData;
import com.longshihan.module_gank.mvp.MainModule.model.impl.ITodayModel;
import com.longshihan.module_gank.mvp.MainModule.model.impl.TodayModel;
import com.longshihan.mvpcomponent.mvp.BasePresenter;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author longshihan
 * @time 2017/9/11 11:36
 * @des
 */
public class TodayPersenter extends BasePresenter<TodayGankContract.View> implements TodayGankContract.Presenter {

    // private Context mContext;
    private ITodayModel mIModel;

    public TodayPersenter(TodayGankContract.View rootView, IRepositoryManager manager) {
        super(rootView);
        // mContext = (Context) rootView;
        mIModel = new TodayModel(manager);

    }

    @Override
    public void getDetail(String page) {
        Disposable disposable = mIModel.getCommond(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MeiziData>() {
                    @Override
                    public void accept(@NonNull MeiziData detailEntityData)  {
                        mRootView.updateListUI(detailEntityData);
                    }
                });
        addDispose(disposable);
    }
}
