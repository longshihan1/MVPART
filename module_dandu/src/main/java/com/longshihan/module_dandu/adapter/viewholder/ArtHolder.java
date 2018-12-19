package com.longshihan.module_dandu.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.mvpcomponent.base.adapter.BaseHolder;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.ArmsUtils;

/**
 * @author longshihan
 * @time 2017/9/4 11:29
 * @des
 */

public class ArtHolder extends BaseHolder<Item> {
    private TextView title;
    private TextView author;
    public ImageView image;
    public RelativeLayout mLayout;

    public ArtHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title_tv);
        author = (TextView) itemView.findViewById(R.id.author_tv);
        image = (ImageView) itemView.findViewById(R.id.image_iv);
        mLayout = (RelativeLayout) itemView.findViewById(R.id.type_container);
    }

    @Override
    public void bindHolder(Item item, int position, Context mContext) {
        author.setText(item.getAuthor());
        title.setText(item.getTitle());
        ArmsUtils.obtainAppComponentFromContext(mContext)
                .imageLoader().loadImage(mContext, ImageConfigImpl.builder()
                .url(item.getThumbnail())
                .imageView(image)
                .build());
    }
}
