package com.longshihan.baseadapter.refresh;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.longshihan.baseadapter.refresh.listener.OnScrollDownListener;
import com.longshihan.baseadapter.refresh.listener.ScrollCallBack;
import com.longshihan.baseadapter.refresh.models.CircleMaterialModel;
import com.longshihan.baseadapter.refresh.models.SimplePullModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 一个简单的刷新Layout
 */
public class CommonRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild, ScrollCallBack {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CIRCLE_MATERIAL_MODEL, SIMPLE_PULL_MODEL})
    public @interface HeadStyleModel {
    }

    /**
     * 圆形Material加载头，原生风格
     */
    public static final int CIRCLE_MATERIAL_MODEL = 0;

    /**
     * 常规简易加载头
     */
    public static final int SIMPLE_PULL_MODEL = 1;

    private static final String LOG_TAG = CommonRefreshLayout.class.getSimpleName();



    private static final int INVALID_POINTER = -1;

    private OnChildScrollUpCallback mChildScrollUpCallback;
    /**
     * the target of the gesture
     */
    private View mTarget;
    private BaseRefreshView mRefreshView;
    private int mRefreshViewIndex = -1;

    private int mActivePointerId = INVALID_POINTER;

    private OnRefreshListener mListener;
    /**
     * Target is returning to its start offset because it was cancelled or a
     * refresh was triggered.
     */
    private boolean mReturningToStart;
    /**
     * If nested scrolling is enabled, the total amount that needed to be
     * consumed by this as the nested scrolling parent is used in place of the
     * overscroll determined by MOVE events in the onTouch handler
     */
    private float mTotalUnconsumed;
    private boolean mNestedScrollInProgress;
    private boolean mIsBeingDragged;
    private float mInitialDownY;
    private float mInitialMotionY;

    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    private int mTouchSlop;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;

    private int targetViewOffset = 0;

    public CommonRefreshLayout(Context context) {
        this(context, null);
    }

    public CommonRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        mTargetAnimationDuration = getResources().getInteger(
//                android.R.integer.config_mediumAnimTime);
        setWillNotDraw(false);

        setChildrenDrawingOrderEnabled(true);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);


        if (isInEditMode()) {
            setRefreshView(new CircleMaterialModel(getContext()));
        }
    }

    void reset() {
        mRefreshView.reset();
    }

    /**
     * 配置view的属性：不可用状态
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
        }
    }

    /**
     * 配置view的属性：从屏幕中消失的状态
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    // NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mReturningToStart && !mRefreshView.isRefreshing()
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            pullRefresh(mTotalUnconsumed);
        }

        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishPull(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    //后于child滚动
    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
            pullRefresh(mTotalUnconsumed);
        }
    }

    //NestedScrollingChild
    /**
     * 设置嵌套滑动是否能用
     *
     *  @param enabled true to enable nested scrolling, false to disable
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }
    /**
     * 判断嵌套滑动是否可用
     *
     * @return true if nested scrolling is enabled
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }
    /**
     * 开始嵌套滑动
     *
     * @param axes 表示方向轴，有横向和竖向
     *             {@link CommonRefreshLayout#onStartNestedScroll(View, View, int)}}
     *             返回true->
     *             {@link CommonRefreshLayout#onNestedScrollAccepted(View, View, int)}
     */
    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }
    /**
     * 停止嵌套滑动
     */
    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }
    /**
     * 判断是否有父View 支持嵌套滑动
     * @return whether this view has a nested scrolling parent
     */
    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }
    /**
     * 子view处理scroll后调用
     *
     * @param dxConsumed x轴上被消费的距离（横向）
     * @param dyConsumed y轴上被消费的距离（竖向）
     * @param dxUnconsumed x轴上未被消费的距离
     * @param dyUnconsumed y轴上未被消费的距离
     * @param offsetInWindow 子View的窗体偏移量
     * @return  true if the event was dispatched, false if it could not be dispatched.
     *  {@link CommonRefreshLayout#onNestedPreScroll(View, int, int, int[])}
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }
    /**
     * 在子View的onInterceptTouchEvent或者onTouch中，调用该方法通知父View滑动的距离
     *
     * @param dx  x轴上滑动的距离
     * @param dy  y轴上滑动的距离
     * @param consumed 父view消费掉的scroll长度
     * @param offsetInWindow   子View的窗体偏移量
     * @return 支持的嵌套的父View 是否处理了 滑动事件
     * {@link CommonRefreshLayout#onNestedScroll(View, int, int, int, int)}
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }
    /**
     * 进行滑行前调用
     *
     * @param velocityX x 轴上的滑动速率
     * @param velocityY y 轴上的滑动速率
     * @return true if a nested scrolling parent consumed the fling
     * {@link CommonRefreshLayout#onNestedPreFling(View, float, float)}
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }
    /**
     * 滑行时调用
     * 返回值：是否消费了fling
     *
     * @param velocityX x 轴上的滑动速率
     * @param velocityY y 轴上的滑动速率
     * @param consumed 是否被消费
     * @return  true if the nested scrolling parent consumed or otherwise reacted to the fling
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private void pullRefresh(float overscrollTop) {
        mRefreshView.pullRefresh(overscrollTop);
    }

    private void finishPull(float overscrollTop) {
        if (mRefreshView.finishPull(overscrollTop) && mListener != null) {
            mListener.onRefresh();
        }
    }

    public BaseRefreshView build(@HeadStyleModel int model) {
        switch (model) {
            case SIMPLE_PULL_MODEL:
                return setRefreshView(new SimplePullModel(getContext()));
            case CIRCLE_MATERIAL_MODEL:
            default:
                return setRefreshView(new CircleMaterialModel(getContext()));
        }
    }

    public void setRefreshing(boolean refreshing) {
        mRefreshView.setRefreshing(refreshing);
    }

    public boolean isRefreshing() {
        return mRefreshView.isRefreshing();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        ensureRefreshView();

        final int action = ev.getAction();
        int pointerIndex;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollUp()
                || mRefreshView.isRefreshing() || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mRefreshView.reset();
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                startDragging(y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            default:
                break;
        }

        return mIsBeingDragged;
    }

    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mIsBeingDragged = true;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && mTarget instanceof AbsListView) {
            return;
        }
        if (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget)) {
            return;
        }
        super.requestDisallowInterceptTouchEvent(b);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        int pointerIndex = -1;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || mReturningToStart || canChildScrollUp()
                || mRefreshView.isRefreshing() || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                startDragging(y);

                if (mIsBeingDragged) {
                    final float overscrollTop = (y - mInitialMotionY);
                    if (overscrollTop > 0) {
                        pullRefresh(overscrollTop);
                    } else {
                        return false;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                pointerIndex = ev.getActionIndex();
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG,
                            "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                if (mIsBeingDragged) {
                    final float y = ev.getY(pointerIndex);
                    final float overscrollTop = (y - mInitialMotionY);
                    mIsBeingDragged = false;
                    finishPull(overscrollTop);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                return false;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        if (mRefreshView == null) {
            ensureRefreshView();
        }
        if (mRefreshView == null) {
            throw new RuntimeException("RefreshView is Null");
        }
        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        int circleWidth = mRefreshView.getMeasuredWidth();
        int circleHeight = mRefreshView.getMeasuredHeight();
        mRefreshView.layout((width / 2 - circleWidth / 2), 0,
                (width / 2 + circleWidth / 2), circleHeight);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        if (mRefreshView == null) {
            ensureRefreshView();
        }

        if (mRefreshView == null) {
            throw new RuntimeException("RefreshView is Null");
        }

        mTarget.measure(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        mRefreshView.measure(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST));
        mRefreshViewIndex = -1;
        // Get the index of the circleview.
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mRefreshView) {
                mRefreshViewIndex = index;
                break;
            }
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mRefreshViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            // Draw the selected child last
            return mRefreshViewIndex;
        } else if (i >= mRefreshViewIndex) {
            // Move the children after the selected child earlier one
            return i + 1;
        } else {
            // Keep the children before the selected child the same
            return i;
        }
    }

    public <T extends BaseRefreshView> T setRefreshView(T view) {
        //如果已经存在刷新头
        if (mRefreshView != null) {
            //那么去掉历史控件的刷新接口引用
            mRefreshView.refreshListener = null;
            //去除历史控件的Body控制引用
            mRefreshView.targetViewScroll = null;
            //移除刷新控件
            removeView(mRefreshView);
        }
        //保存新控件引用
        mRefreshView = view;
        //关联刷新接口引用
        mRefreshView.refreshListener = mListener;
        //关联Body控制引用
        mRefreshView.targetViewScroll = this;
        //添加到Layout中
        addView(mRefreshView);
        //返回控件，以方便参数设置
        return view;
    }

    public BaseRefreshView getParams() {
        return mRefreshView;
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mRefreshView) && !child.getClass().isAssignableFrom(BaseRefreshView.class)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    private void ensureRefreshView() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mRefreshView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof BaseRefreshView && !child.equals(mTarget)) {
                    setRefreshView((BaseRefreshView) child);
                    break;
                }
            }
        }
    }

    @Override
    public void scrollTo(float offsetY) {
        mTarget.setTranslationY(offsetY);
    }

    @Override
    public void scrollWith(float offsetY) {
        mTarget.setTranslationY(mTarget.getTranslationX() + offsetY);
    }

    @Override
    public void lockedScroll() {
        targetViewOffset = (int) mTarget.getTranslationX();
        ViewCompat.offsetTopAndBottom(mTarget, targetViewOffset);
    }

    @Override
    public void resetScroll() {
        ViewCompat.offsetTopAndBottom(mTarget, -targetViewOffset);
    }

    public interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mChildScrollUpCallback != null) {
            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        return mTarget.canScrollVertically(-1);
    }

    /**
     * Set a callback to override {@link CommonRefreshLayout#canChildScrollUp()} method. Non-null
     * callback will return the value provided by the callback and ignore all internal logic.
     *
     * @param callback Callback that should be called when canChildScrollUp() is called.
     */
    public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
        mChildScrollUpCallback = callback;
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * Classes that wish to override {@link CommonRefreshLayout#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link CommonRefreshLayout#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent SwipeRefreshLayout that this callback is overriding.
         * @param child  The child view of SwipeRefreshLayout.
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(CommonRefreshLayout parent, @Nullable View child);
    }


    public void setMoreListener(OnScrollDownListener.OnScrollListener onScrollListener) {
        ensureTarget();
        if (mTarget == null) {
            throw new RuntimeException(getClass().getSimpleName() + " 找不到可用的内容体");
        }
        RecyclerView recyclerView;
        if (mTarget instanceof RecyclerView) {
            recyclerView = (RecyclerView) mTarget;
        } else {
            throw new RuntimeException(getClass().getSimpleName() + " 目前仅支持RecyclerView的上拉加载更多功能");
        }
        recyclerView.addOnScrollListener(new OnScrollDownListener((LinearLayoutManager) recyclerView.getLayoutManager(),onScrollListener));
    }

}
