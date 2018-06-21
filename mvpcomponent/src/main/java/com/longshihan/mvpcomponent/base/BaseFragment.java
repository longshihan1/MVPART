package com.longshihan.mvpcomponent.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.mvpcomponent.base.delegate.IFragment;
import com.longshihan.mvpcomponent.intergration.lifecycle.FragmentLifecycleable;
import com.longshihan.mvpcomponent.mvp.IPresenter;
import com.trello.rxlifecycle2.android.FragmentEvent;


import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * @author longshihan
 * @time 2017/8/18 10:59
 * @des
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment,FragmentLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    protected P mPresenter;
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    protected Activity mActivity;
    protected Context mContext;

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mContext = context;
    }

    public BaseFragment() {
        //必须确保在Fragment实例化时setArguments()
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);
        initData(savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }
}
