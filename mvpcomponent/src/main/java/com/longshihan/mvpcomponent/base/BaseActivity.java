package com.longshihan.mvpcomponent.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.longshihan.mvpcomponent.base.delegate.IActivity;
import com.longshihan.mvpcomponent.intergration.lifecycle.ActivityLifecycleable;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.longshihan.mvpcomponent.utils.ThirdViewUtil.convertAutoView;


/**
 * @author longshihan
 * @time 2017/8/17 14:43
 * @des 因为java普通Activity只能单继承, 所以如果有需要继承特定Activity的三方库, 那你就需要自己自定义Activity，
 * 继承于这个特定的Activity,然后按照将BaseActivity的格式,复制过去记住一定要实现{@link IActivity}
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivity,ActivityLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

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
        initData(savedInstanceState);
    }


}
