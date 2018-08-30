package com.longshihan.mvpcomponent.arch.livedata;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

/**
 * Created by longshihan.
 *
 * @time 2018/7/4 0004
 * @des
 * @function
 */

public class DataObservable<T> extends Observable<T> implements LifecycleObserver {

    static <T> Function<Observable<T>, DataObservable<T>> toFunction(final DataCheck dataCheck) {
        return new Function<Observable<T>, DataObservable<T>>() {
            @Override
            public DataObservable<T> apply(Observable<T> tObservable) {
                return new DataObservable<>(tObservable, dataCheck);
            }
        };
    }

    private final Observable<T> observable;
    private final DataCheck dataCheck;
    private DataObserver<T> dataObserver;

    DataObservable(Observable<T> observable, DataCheck dataCheck) {
        this.observable = observable;
        this.dataCheck = dataCheck;
        if (dataCheck.owner != null) {
            if (!dataCheck.isLifecycleState(dataCheck.owner, Lifecycle.State.DESTROYED)) {
                dataCheck.owner.getLifecycle().addObserver(this);
            }
        }
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        dataObserver = new DataObserver<>(observer);
        observable.subscribe(dataObserver);
        if (dataCheck.isLifecycleState(dataCheck.owner, Lifecycle.State.DESTROYED)) {
            if (dataObserver != null && !dataObserver.isDisposed()) {
                dataObserver.dispose();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onLifecycleDestroy() {
        if (dataObserver != null && !dataObserver.isDisposed()) {
            dataObserver.dispose();
        }
        if (dataCheck.owner != null) {
            dataCheck.owner.getLifecycle().removeObserver(this);
        }
    }
}
