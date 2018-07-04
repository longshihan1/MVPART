package com.longshihan.mvpart;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.longshihan.mvpart.arch.model.User;
import com.longshihan.mvpart.arch.viewmodel.MyViewModel0;
import com.longshihan.mvpart.arch.viewmodel.MyViewModel1;
import com.longshihan.mvpcomponent.*;
import com.longshihan.mvpcomponent.arch.livedata.DataWrap;
import com.longshihan.mvpcomponent.arch.livedata.RxLiveData;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.main_txt);
        if (com.longshihan.mvpcomponent.BuildConfig.TDEBUG) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            Logger.addLogAdapter(new AndroidLogAdapter());//debug下使用Logger日志模块
        }
        final MyViewModel0 myViewModel0= ViewModelProviders.of(this).get(MyViewModel0.class);
        final MyViewModel1 myViewModel1=ViewModelProviders.of(this).get(MyViewModel1.class);
        myViewModel0.getUsers().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User users) {
                Logger.d(users.name);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel0.setUsers(new User("123"));
                myViewModel1.setUsers(new User("234"));
            }
        });


        myViewModel1.getUsers().observable(this).subscribe(new Consumer<DataWrap<User>>() {
            @Override
            public void accept(DataWrap<User> userDataWrap) throws Exception {
                if (!userDataWrap.isNull()) {
                    if (userDataWrap.get()!=null) {
                        Logger.d(userDataWrap.get().name);
                    }else {
                        Logger.d("出错了2");
                    }
                }else {
                    Logger.d("出错了");
                }
            }
        });

    }
}
