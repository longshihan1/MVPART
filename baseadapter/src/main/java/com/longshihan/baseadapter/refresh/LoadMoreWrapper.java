package com.longshihan.baseadapter.refresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.baseadapter.base.BaseViewHolder;
import com.longshihan.baseadapter.headerandfooter.viewholder.FooterViewHolder;
import com.longshihan.baseadapter.utils.WrapperUtils;


/**
 * Created by LONGHE001.
 *
 * @time 2018/7/13 0013
 * @des
 * @function
 */

public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private boolean hasLoadMore = true;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore() {
        return hasLoadMore && mLoadMoreView != null;
    }


    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
           FooterViewHolder holder =new FooterViewHolder(parent.getContext(),mLoadMoreView);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        } else {
            onViewAttachedToWindow(holder, holder.getLayoutPosition());
        }
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder, int postion) {
        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
            return;
        }
        mInnerAdapter.onViewAttachedToWindow(holder);

    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        WrapperUtils.setFullSpan(holder);
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public void addFooter(View view){
        this.mLoadMoreView=view;
        hasLoadMore=true;
        notifyDataSetChanged();
    }

    public void removeFooter(){
        hasLoadMore=false;
        notifyDataSetChanged();
    }
}

