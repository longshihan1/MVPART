package com.longshihan.baseadapter.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.longshihan.baseadapter.refresh.listener.ScrollCallBack;


/**
 * Created by longshihan.
 *
 * @time 2018/7/13 0013
 * @des
 * @function
 */

/**
 * 下拉刷新的显示View
 * 此View同时也控制下拉刷新的属性以及状态
 */


public abstract class BaseRefreshView extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {

    protected long targetViewAnimatorDuration = 200;
    /**
     * Default offset in dips from the top of the view to where the progress spinner should stop
     */
    private static final int DEFAULT_PULL_TARGET = 64;
    protected float mTotalDragDistance = -1;
    protected float mTargetViewOffset = 0;
    protected CommonRefreshLayout.OnRefreshListener refreshListener;
    protected boolean mRefreshing = false;
    protected ScrollCallBack targetViewScroll;

    protected ValueAnimator targetViewAnimator;

    protected void setTotalDragDistance(float mTotalDragDistance) {
        this.mTotalDragDistance = mTotalDragDistance;
    }

    public BaseRefreshView(Context context) {
        this(context, null);
    }

    public BaseRefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mTotalDragDistance = DEFAULT_PULL_TARGET * metrics.density;

        targetViewAnimator = ValueAnimator.ofFloat(0, 1);
        targetViewAnimator.setDuration(targetViewAnimatorDuration);
        targetViewAnimator.addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == targetViewAnimator) {
            mTargetViewOffset = (float) animation.getAnimatedValue();
            targetScrollTo(mTargetViewOffset);
        }
    }

    protected void targetScrollTo(float offsetY) {
        if (targetViewScroll != null) {
            targetViewScroll.scrollTo(offsetY);
        }
    }

    protected void targetScrollWith(float offsetY) {
        if (targetViewScroll != null) {
            targetViewScroll.scrollWith(offsetY);
        }
    }

    protected void targetLockedScroll() {
        if (targetViewScroll != null) {
            targetViewScroll.lockedScroll();
        }
    }

    protected void targetResetScroll() {
        if (targetViewScroll != null) {
            targetViewScroll.resetScroll();
        }
    }

    /**
     * 下拉刷新的方法，此方法将直接接受SimpleRefreshLayout的调用
     * 主控件View的位置变化以及刷新显示控件的状态变化都是由此方法开始
     *
     * @param overscrollTop 下拉的距离
     */
    protected void pullRefresh(float overscrollTop) {
        //计算当前下拉的百分比
        float originalDragPercent = overscrollTop / mTotalDragDistance;
        //得到目标View的位置变化
        mTargetViewOffset = targetViewOffset(overscrollTop, mTotalDragDistance, originalDragPercent);
        targetScrollTo(mTargetViewOffset);
        onPullToRefresh(overscrollTop, mTotalDragDistance, originalDragPercent);
    }

    protected boolean finishPull(float overscrollTop) {
        //计算当前下拉的百分比
        float originalDragPercent = overscrollTop / mTotalDragDistance;
        //触发结束下拉的方法
        onFinishPull(overscrollTop, mTotalDragDistance, originalDragPercent);
        //最后调用一次获取目标View的高度
        mTargetViewOffset = targetViewFinishOffset(overscrollTop, mTotalDragDistance, originalDragPercent);
        targetScrollTo(mTargetViewOffset);
        //返回是否用于刷新的方法
        mRefreshing = canRefresh(overscrollTop, mTotalDragDistance, originalDragPercent);
        if (mRefreshing) {
            setRefreshing(true);
        }
        return mRefreshing;
    }

    /**
     * 是否开始刷新的确定方法，重写次方法用于决定是否开始刷新动作
     *
     * @param pullPixels          下拉的实际距离
     * @param totalDragDistance   设定的触发刷新距离
     * @param originalDragPercent 下拉距离与目标距离间的百分比
     * @return 返回是否开始刷新，如果是true,那么认为是确认为刷新状态，
     * OnRefreshListener将得到触发
     */
    protected boolean canRefresh(float pullPixels, float totalDragDistance, float originalDragPercent) {
        return pullPixels > totalDragDistance;
    }

    /**
     * 返回当前的状态，用于告诉其他部件，确认刷新状态
     *
     * @return 返回刷新状态
     */
    protected boolean isRefreshing() {
        return mRefreshing;
    }

    /**
     * 下拉刷新的控制方法，实际显示的View需要实现此方法，
     * 用于实现对用户操作的反馈，建议将变化过程细化，增加跟随手指的感觉
     * 并且给予足够明显并且有明显暗示性的显示，告知用户当前状态
     *
     * @param pullPixels          下拉的实际距离
     * @param totalDragDistance   设定的触发刷新距离
     * @param originalDragPercent 下拉距离与目标距离间的百分比
     */
    protected abstract void onPullToRefresh(float pullPixels, float totalDragDistance, float originalDragPercent);

    /**
     * 此方法将在结束下拉之后触发，实现或者重写此方法，
     * 将可以在松手后将View复位或者进行其他相关设置
     *
     * @param pullPixels          下拉的实际距离
     * @param totalDragDistance   设定的触发刷新距离
     * @param originalDragPercent 下拉距离与目标距离间的百分比
     */
    protected abstract void onFinishPull(float pullPixels, float totalDragDistance, float originalDragPercent);

    /**
     * 开始刷新的方法，实现或重写此方法，
     * 用于展示自定义的加载动画方法
     */
    protected void setRefreshing(boolean refreshing) {
        if (refreshing && !mRefreshing) {
            callOnRefresh();
        }
        mRefreshing = refreshing;
    }

    /**
     * 这是用来控制主控件View高度变化的方法，在下拉动作触发的时候，
     * 将会调用此方法并且移动主控件View
     *
     * @param pullPixels          下拉的实际距离
     * @param totalDragDistance   设定的触发刷新距离
     * @param originalDragPercent 下拉距离与目标距离间的百分比
     * @return 主控件的位置，此位置为最终位置，并非变化距离。并且此距离为像素距离
     */
    protected float targetViewOffset(float pullPixels, float totalDragDistance, float originalDragPercent) {
        //默认不跟随滑动
        return 0;
    }

    /**
     * 这是用来控制主控件View高度变化的方法，在松手动作触发的时候，
     * 将会调用此方法并且移动主控件View
     *
     * @param pullPixels          下拉的实际距离
     * @param totalDragDistance   设定的触发刷新距离
     * @param originalDragPercent 下拉距离与目标距离间的百分比
     * @return 主控件的位置，此位置为最终位置，并非变化距离。并且此距离为像素距离
     */
    protected float targetViewFinishOffset(float pullPixels, float totalDragDistance, float originalDragPercent) {
        //默认不跟随滑动
        return 0;
    }

    /**
     * 主动触发刷新监听器，
     * 用于特殊情况下，
     * 主动触发刷新
     */
    protected void callOnRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    /**
     * 此方法用于重置显示状态
     */
    protected void reset() {
    }
}
