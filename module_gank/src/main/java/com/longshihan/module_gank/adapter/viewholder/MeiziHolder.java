package com.longshihan.module_gank.adapter.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longshihan.module_gank.R;
import com.longshihan.module_gank.mvp.MainModule.model.entity.Meizi;
import com.longshihan.module_gank.utils.DateUtil;
import com.longshihan.mvpcomponent.base.adapter.BaseHolder;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.ArmsUtils;


/**
 * @author longshihan
 * @time 2017/9/11 11:59
 * @des
 */

public class MeiziHolder extends BaseHolder<Meizi> {
    public ImageView ivMeizi;
    public TextView tvWho;
    public TextView tvAvatar;
    public TextView tvDesc;
    public TextView tvTime;
    public View card;
    public RelativeLayout rl_gank;

    public MeiziHolder(View itemView) {
        super(itemView);
        card = itemView;
        ivMeizi = (ImageView) itemView.findViewById(R.id.iv_meizi);
        tvWho = (TextView) itemView.findViewById(R.id.tv_who);
        tvAvatar = (TextView) itemView.findViewById(R.id.tv_avatar);
        tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        rl_gank = (RelativeLayout) itemView.findViewById(R.id.rl_gank);
    }

    @Override
    public void bindHolder(Meizi meizi, int position, Context mContext) {
        card.setTag(meizi);
        if (meizi != null) {
            ArmsUtils.getImageLoader(mContext)
                    .loadImage(mContext, ImageConfigImpl.builder()
                            .url(meizi.url)
                            .imageView(ivMeizi)
                            .build());
            tvWho.setText(meizi.who);
            tvAvatar.setText(TextUtils.isEmpty(meizi.who) ? "" : meizi.who.substring(0, 1).toUpperCase());
            tvDesc.setText(meizi.desc);
            if (meizi.publishedAt != null) {
                tvTime.setText(DateUtil.toDateTimeStr(meizi.publishedAt));
            }
        }
    }
}
