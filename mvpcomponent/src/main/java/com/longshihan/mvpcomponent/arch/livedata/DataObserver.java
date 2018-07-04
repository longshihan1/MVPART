package com.longshihan.mvpcomponent.arch.livedata;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/4 0004
 * @des
 * @function
 */

public class DataObserver<T> implements Observer<T>, Disposable {

    final AtomicReference<Disposable> s = new AtomicReference<>();

    final Observer<? super T> observer;

    DataObserver(Observer<? super T> observer) {
        this.observer = observer;
    }

    @Override
    public final void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.setOnce(this.s, s, getClass())) {
        }
        observer.onSubscribe(s);
    }

    @Override
    public void onNext(T t) {
        if (t instanceof DataWrap){
            if (!((DataWrap) t).isNull()&&((DataWrap) t).get()!=null){
                observer.onNext(t);
            }
        }

    }

    @Override
    public void onError(Throwable e) {
        observer.onError(e);
    }

    @Override
    public void onComplete() {
        observer.onComplete();
    }

    @Override
    public final boolean isDisposed() {
        return s.get() == DisposableHelper.DISPOSED;
    }

    @Override
    public final void dispose() {
        DisposableHelper.dispose(s);
    }

}
