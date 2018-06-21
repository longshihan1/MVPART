package com.longshihan.mvpcomponent.intergration.lifecycle;

import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @author longshihan
 * @time 2017/9/6 14:51
 * @des 让 Activity 实现此接口,即可正常使用 {@link com.trello.rxlifecycle2.RxLifecycle}
 */

public interface  ActivityLifecycleable extends Lifecycleable<ActivityEvent>  {
}
