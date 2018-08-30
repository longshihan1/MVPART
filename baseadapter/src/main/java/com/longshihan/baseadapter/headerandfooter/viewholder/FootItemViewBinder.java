package com.longshihan.baseadapter.headerandfooter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.longshihan.baseadapter.ItemViewBinder;
import com.longshihan.baseadapter.R;
import com.longshihan.baseadapter.headerandfooter.model.FooterMultiTypeItem;


/**
 * Created by longshihan.
 *
 * @time 2018/7/13 0013
 * @des
 * @function
 */

public class FootItemViewBinder extends ItemViewBinder<FooterMultiTypeItem, FootItemViewBinder.ViewHolder> {

    public FootItemViewBinder() {
    }

    @Override
    protected @NonNull
    FootItemViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new FootItemViewBinder.ViewHolder(inflater.inflate(R.layout.header_empty, parent, false));
    }


    @Override
    protected long getItemId(@NonNull FooterMultiTypeItem item) {
        return super.getItemId(item);
    }

    @Override
    protected void onBindViewHolder(@NonNull FootItemViewBinder.ViewHolder holder, @NonNull FooterMultiTypeItem heavyItem) {
        holder.container.removeAllViews();
        ViewGroup parentViewGroup = (ViewGroup) heavyItem.getView().getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeView(heavyItem.getView());
        }
        holder.container.addView(heavyItem.getView());
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout container;

        ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.header_empty);
        }
    }
}
