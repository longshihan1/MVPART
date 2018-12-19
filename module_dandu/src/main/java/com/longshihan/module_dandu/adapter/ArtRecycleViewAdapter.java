package com.longshihan.module_dandu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.adapter.viewholder.ArtHolder;
import com.longshihan.module_dandu.detail.ui.AudioDetailActivity;
import com.longshihan.module_dandu.detail.ui.DetailActivity;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.module_dandu.detail.ui.VideoDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ArtRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTER_TYPE = 1001;
    private static final int CONTENT_TYPE = 1002;
    private List<Item> artList = new ArrayList<>();
    private Context context;


    public ArtRecycleViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_art, parent, false);
        return new ArtHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArtHolder artHolder = (ArtHolder) holder;
        final Item item = artList.get(position);
        artHolder.bindHolder(item, position, context);
        artHolder.mLayout.setOnClickListener(view -> {
            int model = Integer.valueOf(item.getModel());
            Intent intent = null;
            switch (model) {
                case 2://视频
                    intent = new Intent(context, VideoDetailActivity.class);
                    break;
                case 1://文字
                    intent = new Intent(context, DetailActivity.class);
                    break;
                case 3://音频
                    intent = new Intent(context, AudioDetailActivity.class);
                    break;
            }
            if (intent != null) {
                intent.putExtra("item", item);
                context.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    public void setArtList(List<Item> artList) {
        this.artList.addAll(artList);
        notifyDataSetChanged();
    }

    public void replaceAllData(List<Item> artList) {
        this.artList.clear();
        this.artList.addAll(artList);
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        if (artList.size() == 0) {
            return "0";
        }
        Item item = artList.get(artList.size() - 1);
        return item.getId();
    }

    public String getLastItemCreateTime() {
        if (artList.size() == 0) {
            return "0";
        }
        Item item = artList.get(artList.size() - 1);
        return item.getCreate_time();
    }

}
