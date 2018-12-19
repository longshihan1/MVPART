package com.longshihan.module_dandu.main.persenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.longshihan.module_dandu.main.model.IMainListModel;
import com.longshihan.module_dandu.main.model.MainListModel;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.module_dandu.mvp.model.entity.Result;
import com.longshihan.module_dandu.tools.RxUtils;
import com.longshihan.mvpcomponent.mvp.BasePresenter;
import com.longshihan.mvpcomponent.mvp.IRepositoryManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author longshihan
 * @time 2017/9/1 13:57
 * @des
 */

public class WordPersenter extends BasePresenter<WordContract.View> implements WordContract.Presenter {
    private Context mContext;
    private IMainListModel mIModel;

    public WordPersenter(WordContract.View rootView, IRepositoryManager manager) {
        super(rootView);
        mIModel = new MainListModel(manager);
    }

    @Override
    public void getListByPage(int page, int model, String pageId, String deviceId,
                              String createTime) {
        Disposable mdisPosable = mIModel.getAD(page, model, pageId, createTime, deviceId)
                .compose(RxUtils.<Result.Data<List<Item>>>applySchedulers(mRootView))
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRootView.showOnFailure();
                    }
                });
        addDispose(mdisPosable);
    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
        super.onDestroy(owner);
    }
}
