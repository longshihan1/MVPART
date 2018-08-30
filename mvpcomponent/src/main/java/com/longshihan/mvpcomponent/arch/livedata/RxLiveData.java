package com.longshihan.mvpcomponent.arch.livedata;

import android.arch.lifecycle.LifecycleOwner;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by longshihan.
 *
 * @time 2018/7/4 0004
 * @des
 * @function
 */

public class RxLiveData<T> {

    private Subject<DataWrap<T>> triggers;

    private T data;

    private final Object dataLock = new Object();

    public RxLiveData() {
        triggers = PublishSubject.create();
    }

    public RxLiveData(T data) {
        this();
        this.data = data;
    }

    public T get() {
        return data;
    }

    public void set(T data) {
        synchronized (dataLock) {
            this.data = data;
            notifyChangedInternal();
        }
    }

    public void notifyChanged() {
        synchronized (dataLock) {
            notifyChangedInternal();
        }
    }

    private void notifyChangedInternal() {
        if (data!=null) {
            triggers.onNext(new DataWrap<>(data));
        }
    }

    public DataObservable<DataWrap<T>> observable() {
        return observable(null);
    }

    public DataObservable<DataWrap<T>> observable(LifecycleOwner owner) {
        DataCheck<DataWrap<T>> dataCheck = new DataCheck<>(owner);
        return triggers
                .startWith(new DataWrap<>(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(dataCheck.getPredicateCheck())
                .to(DataObservable.<DataWrap<T>>toFunction(dataCheck));
    }
}
