package com.longshihan.mvpcomponent.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.longshihan.mvpcomponent.base.delegate.IActivity;
import com.longshihan.mvpcomponent.mvp.IPresenter;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.longshihan.mvpcomponent.utils.ThirdViewUtil.convertAutoView;


/**
 * @author longshihan
 * @time 2017/8/17 14:43
 * @des 因为java只能单继承, 所以如果有需要继承特定Activity的三方库, 那你就需要自己自定义Activity，
 * 继承于这个特定的Activity,然后按照将BaseActivity的格式,复制过去记住一定要实现{@link IActivity}
 */

public abstract class BaseMVPActivity<P extends IPresenter> extends AppCompatActivity implements IActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected P mPresenter;
    protected RxPermissions rxPermissions;


    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException e) {
            Logger.e(TAG, "Could not cast View to concrete class.", e);
            throw e;
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = convertAutoView(name, context, attrs);
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            if (layoutResID != 0) {
                setContentView(layoutResID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLifecycle().addObserver(mPresenter);
    }

    private CompositeDisposable mCompositeDisposable;
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mPresenter = null;
    }

}
