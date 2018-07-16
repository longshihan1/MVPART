package com.longshihan.baseadapter.refresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.longshihan.baseadapter.Items;
import com.longshihan.baseadapter.MultiTypeAdapter;
import com.longshihan.baseadapter.headerandfooter.HeaderAndFooterWrapper;
import com.longshihan.baseadapter.headerandfooter.model.FooterMultiTypeItem;
import com.longshihan.baseadapter.headerandfooter.viewholder.FootItemViewBinder;


/**
 * Created by LONGHE001.
 *
 * @time 2018/7/12 0012
 * @des 刷新的设配器包装类
 * @function  如果传过来的是普通的adapter，那么在刷新的时候推荐recyclerview.getAdapter这种方式进行刷新
 */

public class RefreshWrapper {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private View mLoadMoreView;
    private int mLoadMoreLayoutId;

    private HeaderAndFooterWrapper wrapper;//封装好的数据
    private MultiTypeAdapter multiTypeAdapter;//多Item的adapter
    private RecyclerView.Adapter adapter;//正常的adapter
    private RecyclerView recyclerView;
    private boolean hasLoadMore = false;
    private boolean isLoading = false;
    private FooterMultiTypeItem footerMultiTypeItem;
    private LoadMoreWrapper loadMoreWrapper;

    public RefreshWrapper(RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        if (recyclerView.getAdapter() instanceof HeaderAndFooterWrapper){
            wrapper= (HeaderAndFooterWrapper) recyclerView.getAdapter();
        }else if (recyclerView.getAdapter() instanceof MultiTypeAdapter){
            multiTypeAdapter= (MultiTypeAdapter) recyclerView.getAdapter();
            multiTypeAdapter.register(FooterMultiTypeItem.class,new FootItemViewBinder());
        }else {
            adapter=recyclerView.getAdapter();
            loadMoreWrapper=new LoadMoreWrapper(adapter);
        }
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisiblePosition;
                if (layoutManager instanceof GridLayoutManager) {
                    lastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int into[] = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    // TODO: 2018/7/12 0012 留待确认
                    lastVisiblePosition = into[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                } else {
                    lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (layoutManager.getChildCount() > 0             //当当前显示的item数量>0
                        && lastVisiblePosition >= layoutManager.getItemCount() - 1           //当当前屏幕最后一个加载项位置>=所有item的数量
                        && layoutManager.getItemCount() > layoutManager.getChildCount()) { // 当当前总Item数大于可见Item数
                    Log.d("LoadMoreRecyclerView", "run onLoadMore");
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMoreRequested();
                    }
                }

                }
            }
        });
    }
    public void loadingMore() {
        if(!isLoading) {
            isLoading = true;
            if (hasLoadMore) {
                if (wrapper!=null) {//带头尾的adapter
                    wrapper.addFootView(mLoadMoreView);
                }else if (multiTypeAdapter!=null){//通用的adapter
                    footerMultiTypeItem=new FooterMultiTypeItem(mLoadMoreView);
                    Items items= (Items) multiTypeAdapter.getItems();
                    items.add(footerMultiTypeItem);
                    multiTypeAdapter.notifyItemInserted(multiTypeAdapter.getItems().size()-1);
                }else {//普通的adapter
                    loadMoreWrapper.addFooter(mLoadMoreView);
                }
                //刷新出最后一个
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
            }
        }
    }


    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public RefreshWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public RefreshWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        hasLoadMore=true;
        return this;
    }

    public RefreshWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        hasLoadMore=true;
        return this;
    }

    public void loadingMoreComplete() {
        isLoading = false;
        if (hasLoadMore) {
            if (wrapper != null) {
                wrapper.removeFooter(mLoadMoreView);
            }else if (multiTypeAdapter!=null){//通用的adapter
                Items items= (Items) multiTypeAdapter.getItems();
                items.remove(footerMultiTypeItem);
                multiTypeAdapter.notifyDataSetChanged();
            }else {//普通的adapter
                loadMoreWrapper.removeFooter();
            }
        }
    }

    public void setHasLoadMore(boolean isHasMore){
        hasLoadMore=isHasMore;
    }

    public boolean isMoreLoading() {
        return isLoading;
    }
}
