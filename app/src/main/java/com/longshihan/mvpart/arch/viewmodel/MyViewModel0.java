package com.longshihan.mvpart.arch.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.longshihan.mvpart.arch.model.User;

/**
 * Created by longshihan.
 *
 * @time 2018/7/3 0003
 * @des
 * @function
 */

public class MyViewModel0 extends ViewModel {
    private MutableLiveData<User> user=new MutableLiveData<>();

    public void setUsers(User userItem){
        user.setValue(userItem);
    }
    public LiveData<User> getUsers() {
        return user;
    }
}
