package com.longshihan.mvpcomponent.arouter;

import android.app.Activity;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author longshihan
 * @time 2017/8/25 11:26
 * @des 写完之后发现在这里面写并不好，应该在做拦截器做，拦截登录，注册事件
 */

public class ArouterUtil {
    public static void GotoLogin(Activity activity) {
        ARouter.getInstance().build("/login/LoginActivity").navigation(activity, new NavigationCallback() {

            @Override
            public void onFound(Postcard postcard) {
                Log.d("ARouter", "找到了");
            }

            @Override
            public void onLost(Postcard postcard) {
                Log.d("ARouter", "找不到了");
            }

            @Override
            public void onArrival(Postcard postcard) {
                Log.d("ARouter", "跳转完了");
                //在这里面处理缓存消除的行为，和个人信息清除的行为
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                Log.d("ARouter", "被拦截了");
            }
        });
    }
}
