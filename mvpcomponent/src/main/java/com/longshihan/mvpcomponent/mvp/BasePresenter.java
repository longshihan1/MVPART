package com.longshihan.mvpcomponent.mvp;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by longshihan on 2017/8/16.
 */

public class BasePresenter<V extends IView> implements IPresenter {
    protected final String TAG = this.getClass().getSimpleName();
    //rx2，和1的使用CompositeDisposable统一管理persenter线程
    protected CompositeDisposable mCompositeDisposable;
    protected V mRootView;
    //使用弱引用，但实际效果不好
    protected WeakReference<V> mViewRef;

    public BasePresenter() {
        onStart();
    }

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
       // attachView(rootView);
        onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCloseView(){
        mRootView.closeActivity();
    }

    @Override
    public void onDestroy() {
        detachView();
        unDispose();//解除订阅

        this.mCompositeDisposable = null;
    }

    private void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        this.mRootView = null;
    }

    /**
     * 获取view对象
     *
     * @return
     */
    protected V getView() {
       /* if (mViewRef == null) {
            return null;
        }
        return mViewRef.get();*/
        return mRootView;
    }


    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有disposable放入,集中处理
    }

    public void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证activity结束时取消所有正在执行的订阅
        }
    }
}
