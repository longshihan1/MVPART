package com.longshihan.mvpcomponent.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.longshihan.mvpcomponent.utils.ArouterUtil;
import com.longshihan.mvpcomponent.utils.LoginUtils;

/**
 * @author longshihan
 * @time 2017/8/25 10:25
 * @des Arouter的拦截器
 */

@Interceptor(priority = 8)
public class TestInterceptor implements IInterceptor {
    private Context mContext;

    @Override
    public void process(Postcard postcard, InterceptorCallback interceptorCallback) {
        if (postcard.getExtra() == ArouterUtil.CheckLOGIN && LoginUtils.checkLogin()) {
            interceptorCallback.onInterrupt(new RuntimeException("账号未登录"));
            //在这里面处理登录事件。清除缓存就行了
            ARouter.getInstance().build("/login/LoginActivity").navigation();
        } else {
            interceptorCallback.onContinue(postcard);//交还控制权
        }
    }

    @Override
    public void init(Context context) {
        // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
        mContext = context;
    }
}
