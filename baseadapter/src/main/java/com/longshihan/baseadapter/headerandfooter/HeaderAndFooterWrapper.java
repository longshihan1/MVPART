package com.longshihan.baseadapter.headerandfooter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.baseadapter.MultiTypeAdapter;
import com.longshihan.baseadapter.headerandfooter.viewholder.FooterViewHolder;
import com.longshihan.baseadapter.headerandfooter.viewholder.HeaderViewHolder;
import com.longshihan.baseadapter.utils.WrapperUtils;


/**
 * Created by LONGHE001.
 *
 * @time 2018/7/11 0011
 * @des 添加头尾的设置器包装类
 * @function
 */

public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 200000;
    private static final int BASE_ITEM_TYPE_FOOTER = 250000;
    public RecyclerView recyclerView;
    private RecyclerView.Adapter mInnerAdapter;
    private SparseArrayCompat<View> mHeaderViews;
    private SparseArrayCompat<View> mFootViews;

    public HeaderAndFooterWrapper(MultiTypeAdapter adapter) {
        this.mInnerAdapter = adapter;
        mHeaderViews = new SparseArrayCompat<>();
        mFootViews = new SparseArrayCompat<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            RecyclerView.ViewHolder holder = HeaderViewHolder.createViewHolder(parent.getContext(),mHeaderViews.get(viewType));
            return holder;
        } else if (mFootViews.get(viewType) != null) {
            RecyclerView.ViewHolder holder = FooterViewHolder.createViewHolder(parent.getContext(),mFootViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)||isFooterViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }



    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
        notifyDataSetChanged();
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position - getHeadersCount());
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            setFullSpan(holder);
            return;
        }
        if (holder.getItemViewType()>200000){
            return;
        }
        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            return;
        }
        if (holder.getItemViewType()>100000){
            return;
        }
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }

    public static void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    /**
     * 删除全部头部view
     */
    public void removeHeader() {
        if (mHeaderViews.size()>0) {
            mHeaderViews=new SparseArrayCompat<>();
            notifyDataSetChanged();
        }
    }

    /**
     * 删除指定头部view
     */
    public void removeHeader(View view) {
        if (mHeaderViews.size()>0) {
            if (mHeaderViews.size() == 1) {
                removeHeader();
            } else {
                for (int i = 0; i <mHeaderViews.size(); i++) {
                    if (view==mHeaderViews.get(mHeaderViews.keyAt(i))){
                        mHeaderViews.remove(mHeaderViews.keyAt(i));
                        break;
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 删除顶部头部view
     */
    public void removeTopHeader() {
        if (mHeaderViews.size()>0) {
            if (mHeaderViews.size() == 1) {
                removeHeader();
            } else {
                mHeaderViews.remove(mHeaderViews.keyAt(0));
                notifyDataSetChanged();
            }
        }
    }


    /**
     * 删除全部底部的view
     */
    public void removeFooter() {
        if (mFootViews.size()>0) {
            mFootViews=new SparseArrayCompat<>();
            notifyDataSetChanged();
        }
    }

    /**
     * 删除指定底部的view
     */
    public void removeFooter(View view) {
        if (mFootViews.size()>0) {
            if (mFootViews.size() == 1) {
                removeFooter();
            } else {
                for (int i = 0; i <mFootViews.size(); i++) {
                    if (view==mFootViews.get(mFootViews.keyAt(i))){
                        mFootViews.remove(mFootViews.keyAt(i));
                        break;
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 删除最底部的view
     */
    public void removeTopFooter() {
        if (mFootViews.size()>0) {
            if (mFootViews.size() == 1) {
                removeHeader();
            } else {
                mFootViews.remove(mFootViews.keyAt(mFootViews.size()-1));
                notifyDataSetChanged();
            }
        }
    }
}
