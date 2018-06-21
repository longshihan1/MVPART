package com.longshihan.mvpcomponent.utils;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * 滑动居中对齐，获取position
 */
public class GallerySnapHelper extends SnapHelper {
    private static final float INVALID_DISTANCE = 1f;
    private static final float MILLISECONDS_PER_INCH = 40f;
    private OrientationHelper mHorizontalHelper;
    private RecyclerView mRecyclerView;
    private String TAG = "GallerySnapHelper";


    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        mRecyclerView = recyclerView;
        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(layoutManager, targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }
        return out;
    }

    private int distanceToStart(RecyclerView.LayoutManager layoutManager, View targetView, OrientationHelper helper) {
        //找到targetView的中心坐标
        final int childCenter = helper.getDecoratedStart(targetView) +
                (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter;
        //找到容器（RecyclerView）的中心坐标
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        //两个中心坐标的差值就是targetView需要滚动的距离
        return childCenter - containerCenter;

        //    return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    @Nullable
    protected LinearSmoothScroller createSnapScroller(final RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(mRecyclerView.getContext()) {
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistances = calculateDistanceToFinalSnap(mRecyclerView.getLayoutManager(), targetView);
                final int dx = snapDistances[0];
                final int dy = snapDistances[1];
                final int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return RecyclerView.NO_POSITION;
        }

        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION;
        }

        final View currentView = findSnapView(layoutManager);
        if (currentView == null) {
            return RecyclerView.NO_POSITION;
        }

        final int currentPosition = layoutManager.getPosition(currentView);
        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider =
                (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
        // deltaJumps sign comes from the velocity which may not match the order of children in
        // the LayoutManager. To overcome this, we ask for a vector from the LayoutManager to
        // get the direction.
        PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
        if (vectorForEnd == null) {
            // cannot get a vector for the given position.
            return RecyclerView.NO_POSITION;
        }

        //在松手之后,列表最多只能滚多一屏的item数
        int deltaThreshold = layoutManager.getWidth() / getHorizontalHelper(layoutManager).getDecoratedMeasurement(currentView);

        int hDeltaJump;
        if (layoutManager.canScrollHorizontally()) {
            hDeltaJump = estimateNextPositionDiffForFling(layoutManager,
                                                          getHorizontalHelper(layoutManager), velocityX, 0
                                                         );

            if (hDeltaJump > deltaThreshold) {
                hDeltaJump = deltaThreshold;
            }
            if (hDeltaJump < -deltaThreshold) {
                hDeltaJump = -deltaThreshold;
            }

            if (vectorForEnd.x < 0) {
                hDeltaJump = -hDeltaJump;
            }
        } else {
            hDeltaJump = 0;
        }

        if (hDeltaJump == 0) {
            return RecyclerView.NO_POSITION;
        }

        int targetPos = currentPosition + hDeltaJump;
        if (targetPos < 0) {
            targetPos = 0;
        }
        if (targetPos >= itemCount) {
            targetPos = itemCount - 1;
        }
        return targetPos;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
    }

    private View findCenterView(RecyclerView.LayoutManager layoutManager,
                                OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        int firstChildPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        int centerPosition = (firstChildPosition + ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition()) / 2;
        Log.d(TAG, centerPosition + "");
        View closestChild = null;
        //找到RecyclerView的中心坐标
        final int center;
        if (layoutManager.getClipToPadding()) {
            center = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        } else {
            center = helper.getEnd() / 2;
        }
        int absClosest = Integer.MAX_VALUE;

        //遍历当前layoutManager中所有的ItemView
        for (int i = 0; i < childCount; i++) {
            final View child = layoutManager.getChildAt(i);
            //ItemView的中心坐标
            int childCenter = helper.getDecoratedStart(child) +
                    (helper.getDecoratedMeasurement(child) / 2);
            //计算此ItemView与RecyclerView中心坐标的距离
            int absDistance = Math.abs(childCenter - center);

            //对比每个ItemView距离到RecyclerView中心点的距离，找到那个最靠近中心的ItemView然后返回
            if (absDistance < absClosest) {
                absClosest = absDistance;
                closestChild = child;
            }
        }
        return closestChild;
    }

    private View findStartView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        if (layoutManager instanceof LinearLayoutManager) {
            int firstChildPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int centerPosition = (firstChildPosition + ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition()) / 2;

            if (firstChildPosition == RecyclerView.NO_POSITION) {
                return null;
            }
            Log.d(TAG, centerPosition + ":" + mRecyclerView.getAdapter().getItemId(centerPosition));
            if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                return null;
            }

            View firstChildView = layoutManager.findViewByPosition(centerPosition);
            if (helper.getDecoratedEnd(firstChildView) >= helper.getDecoratedMeasurement(firstChildView) / 2 && helper.getDecoratedEnd(firstChildView) > 0) {
                return firstChildView;
            } else {
                return layoutManager.findViewByPosition(centerPosition + 1);
            }
        } else {
            return null;
        }
    }


    private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager layoutManager,
                                                 OrientationHelper helper, int velocityX, int velocityY) {
        int[] distances = calculateScrollDistance(velocityX, velocityY);
        float distancePerChild = computeDistancePerChild(layoutManager, helper);
        if (distancePerChild <= 0) {
            return 0;
        }
        int distance = distances[0];
        if (distance > 0) {
            return (int) Math.floor(distance / distancePerChild);
        } else {
            return (int) Math.ceil(distance / distancePerChild);
        }
    }

    private float computeDistancePerChild(RecyclerView.LayoutManager layoutManager,
                                          OrientationHelper helper) {
        View minPosView = null;
        View maxPosView = null;
        int minPos = Integer.MAX_VALUE;
        int maxPos = Integer.MIN_VALUE;
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return INVALID_DISTANCE;
        }

        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            final int pos = layoutManager.getPosition(child);
            if (pos == RecyclerView.NO_POSITION) {
                continue;
            }
            if (pos < minPos) {
                minPos = pos;
                minPosView = child;
            }
            if (pos > maxPos) {
                maxPos = pos;
                maxPosView = child;
            }
        }
        if (minPosView == null || maxPosView == null) {
            return INVALID_DISTANCE;
        }
        int start = Math.min(
                helper.getDecoratedStart(minPosView),
                helper.getDecoratedStart(maxPosView)
                            );
        int end = Math.max(
                helper.getDecoratedEnd(minPosView),
                helper.getDecoratedEnd(maxPosView)
                          );
        int distance = end - start;
        if (distance == 0) {
            return INVALID_DISTANCE;
        }
        return 1f * distance / ((maxPos - minPos) + 1);
    }


    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

    private OnCenterListener mOnCenterListener;

    public void setOnCenterListener(OnCenterListener onCenterListener) {
        mOnCenterListener = onCenterListener;
    }

    interface OnCenterListener{
        void setOnCenterListener(int position);
    }

}
