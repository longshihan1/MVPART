package com.longshihan.module_gank.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.module_gank.R;
import com.longshihan.module_gank.adapter.viewholder.MeiziHolder;
import com.longshihan.module_gank.http.GankApi;
import com.longshihan.module_gank.mvp.MainModule.model.entity.Meizi;
import com.longshihan.module_gank.mvp.MeiZiModule.MeiZhiActivity;
import com.longshihan.module_gank.utils.ShareElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 */
public class MeiZiAdapter extends RecyclerView.Adapter<MeiziHolder> {

    List<Meizi> list;
    Context context;
    int lastPosition = 0;


    public MeiZiAdapter(Context context, List<Meizi> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meizi, parent, false);
        return new MeiziHolder(view);
    }

    public void replaceList(List<Meizi> datas) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list = datas;
        notifyDataSetChanged();
    }

    public void appendList(List<Meizi> datas) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MeiziHolder holder, int position) {
        Meizi meizi = list.get(position);
        holder.bindHolder(meizi,position,context);
        holder.ivMeizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareElement.shareDrawable = holder.ivMeizi.getDrawable();
                Intent intent = new Intent(context, MeiZhiActivity.class);
                intent.putExtra(GankApi.MEIZI, (Serializable) holder.card.getTag());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((Activity) context, holder.ivMeizi, GankApi.TRANSLATE_GIRL_VIEW);
                ActivityCompat.startActivity((Activity) context, intent, optionsCompat.toBundle());
            }
        });
        holder.rl_gank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareElement.shareDrawable = holder.ivMeizi.getDrawable();
//                Intent intent = new Intent(context, GankActivity.class);
//                intent.putExtra(GankApi.MEIZI, (Serializable) holder.card.getTag());
//                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
//                        .makeSceneTransitionAnimation((Activity) context, holder.ivMeizi, GankApi.TRANSLATE_GIRL_VIEW);
//                ActivityCompat.startActivity((Activity) context, intent, optionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private void showItemAnimation(MeiziHolder holder, int position) {
        if (position > lastPosition) {
            lastPosition = position;
            ObjectAnimator.ofFloat(holder.card, "translationY", 1f * holder.card.getHeight(), 0f)
                    .setDuration(500)
                    .start();
        }
    }
}
