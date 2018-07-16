package com.longshihan.baseadapter.group;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.baseadapter.Items;
import com.longshihan.baseadapter.MultiTypeAdapter;
import com.longshihan.baseadapter.listener.OnItemClickListener;
import com.longshihan.baseadapter.utils.WrapperUtils;

import java.util.Collections;
import java.util.List;


/**
 * Created by LONGHE001.
 *
 * @time 2018/7/16 0016
 * @des 组布局的适配器
 * @function
 */

public class GroupWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MultiTypeAdapter mInnerAdapter;
    GroupWrapperDateHelper helper = new GroupWrapperDateHelper();
    private IExpandListener listener;
    OnItemClickListener<Object> onItemClickListener;
    public GroupWrapperDateHelper getHelper() {
        return helper;
    }

    public GroupWrapper(MultiTypeAdapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    public void setGroupList(List<GroupInfo> groupList) {
        helper.sourceList = groupList;
        helper.calculateList();
        Items items=new Items();
        items.addAll(helper.adapterList);
        mInnerAdapter.setItems(items);
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (helper.isGroupHeader(position)) {
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
    public int getItemViewType(int position) {
        return mInnerAdapter.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mInnerAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mInnerAdapter.onBindViewHolder(holder,position, Collections.emptyList());
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position, @NonNull List<Object> payloads) {
        mInnerAdapter.onBindViewHolder(holder, position, payloads);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrShrikGroup(holder, mInnerAdapter.getItems().get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount();
    }

    private void expandOrShrikGroup(RecyclerView.ViewHolder holder, Object o, int position) {
        boolean needopen = true;
        if (helper.isGroupHeader(position)) {
            for (GroupInfo groupStructure : helper.openGroupList) {
                if (groupStructure.equalParent(o)) {
                    needopen = false;
                    notifyDataForShrik(groupStructure, position);
                    if (listener != null) {
                        listener.onShrink(holder, o, position);
                    }
                    break;
                }
            }
            if (needopen) {
                for (GroupInfo needAddGroupStrure : helper.sourceList) {
                    if (needAddGroupStrure.equalParent(o)) {
                        notifyDataForExpand(needAddGroupStrure, position);
                        if (listener != null) {
                            listener.onExpand(holder, o, position);
                        }
                        break;
                    }
                }
            }
        }

        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(holder.itemView, holder, o, position);
        }
    }

    void notifyDataForExpand(GroupInfo expandObj, int postiton) {
        helper.notifyDataForExpand(expandObj,postiton);
        if (expandObj.getChildrenCount() > 0) {
            if (helper.isAnimtor) {
                notifyItemRangeInserted(postiton + 1, expandObj.getChildrenCount());
            } else {
                mInnerAdapter.setItems(helper.adapterList);
                notifyDataSetChanged();
            }
        }
    }

    void notifyDataForShrik(GroupInfo shrikObj, int postiton) {
        helper.notifyDataForShrik(shrikObj);
        if (shrikObj.getChildrenCount() > 0) {
            if (helper.isAnimtor) {
                notifyItemRangeRemoved(postiton + 1, shrikObj.getChildrenCount());
            } else {
                mInnerAdapter.setItems(helper.adapterList);
                notifyDataSetChanged();
            }
        }
    }

    public void setListener(IExpandListener listener) {
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    public interface IExpandListener {
        void onExpand(RecyclerView.ViewHolder holder, Object o, int position);

        void onShrink(RecyclerView.ViewHolder holder, Object o, int position);
    }


}
