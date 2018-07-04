package com.longshihan.mvpcomponent.arch.livedata;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import io.reactivex.functions.Predicate;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/4 0004
 * @des
 * @function
 */

public class DataCheck <T> {
    public LifecycleOwner owner;

    DataCheck(LifecycleOwner owner) {
        this.owner = owner;
    }

    private final Predicate<T> predicateCheck = new Predicate<T>() {
        @Override
        public boolean test(T data) {
            // 当生命周期已经结束时，不必再进行数据发送
            if (isLifecycleState(owner, Lifecycle.State.DESTROYED)) {
                return false;
            }
            if (data==null){
                return false;
            }
            return true;
        }
    };

    Predicate<T> getPredicateCheck() {
        return predicateCheck;
    }

    public static boolean isLifecycleState(LifecycleOwner owner, @android.support.annotation.NonNull Lifecycle.State state) {
        return owner != null && owner.getLifecycle().getCurrentState() == state;
    }
}
