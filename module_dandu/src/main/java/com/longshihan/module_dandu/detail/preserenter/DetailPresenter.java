package com.longshihan.module_dandu.detail.preserenter;

import android.content.Context;

import com.longshihan.module_dandu.mvp.model.entity.DetailEntity;
import com.longshihan.module_dandu.mvp.model.entity.Result;
import com.longshihan.module_dandu.detail.model.DetailModel;
import com.longshihan.module_dandu.detail.model.IDetailModel;
import com.longshihan.mvpcomponent.mvp.BasePresenter;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author longshihan
 * @time 2017/9/4 17:13
 * @des
 */
public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {

    private Context mContext;
    private IDetailModel mIModel;

    public DetailPresenter(DetailContract.View rootView, IRepositoryManager manager) {
        super(rootView);
        //mContext = (Context) rootView;
        mIModel = new DetailModel(manager);

    }

    @Override
    public void getDetail(String itemId) {
        Disposable disposable = mIModel.getCommond(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result.Data<DetailEntity>>() {
                    @Override
                    public void accept(@NonNull Result.Data<DetailEntity> detailEntityData) throws Exception {
                        mRootView.updateListUI(detailEntityData.getDatas());
                    }
                });
        addDispose(disposable);
    }
}
