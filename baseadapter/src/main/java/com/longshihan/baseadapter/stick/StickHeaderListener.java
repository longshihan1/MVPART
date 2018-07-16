package com.longshihan.baseadapter.stick;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface StickHeaderListener {
    boolean isHeader(int position);
    RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int position);
    void onBindHeaderViewHolder(final RecyclerView.ViewHolder viewholder, final int position);
}
