package com.longshihan.mvpart.arch.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.longshihan.mvpart.arch.model.User;
import com.longshihan.mvpcomponent.arch.livedata.RxLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by longshihan.
 *
 * @time 2018/7/3 0003
 * @des
 * @function
 */

public class MyViewModel1 extends ViewModel {

    private RxLiveData<User> users=new RxLiveData<>();

    public RxLiveData<User> getUsers() {
        return users;
    }

    public void setUsers(User user) {
        users.set(user);
    }
}
