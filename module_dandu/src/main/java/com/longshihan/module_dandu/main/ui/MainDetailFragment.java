package com.longshihan.module_dandu.main.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.mvpcomponent.base.BaseMVPFragment;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.ArmsUtils;

/**
 * Created by LONGHE001.
 *
 * @time 2018/12/19 0019
 * @des
 * @function
 */

public class MainDetailFragment extends BaseMVPFragment {

    String title;
    ImageView imageIv;
    LinearLayout typeContainer;
    TextView commentTv;
    TextView likeTv;
    TextView readcountTv;
    TextView titleTv;
    TextView contentTv;
    TextView authorTv;
    TextView typeTv;
    TextView timeTv;
    ImageView imageType;
    ImageView downloadStartWhite;
    ImageView homeAdvertiseIv;
    RelativeLayout pagerContent;

    public MainDetailFragment() {
        // Required empty public constructor
    }

    public static Fragment instance(Item item) {
        Fragment fragment = new MainDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Item item = getArguments().getParcelable("item");
        final int model = Integer.valueOf(item.getModel());
        if (model == 5) {
            pagerContent.setVisibility(View.GONE);
            homeAdvertiseIv.setVisibility(View.VISIBLE);
            ArmsUtils.getImageLoader(this).loadImage(this, ImageConfigImpl.builder()
                    .url(item.getThumbnail())
                    .imageView(homeAdvertiseIv).build());
        } else {
            pagerContent.setVisibility(View.VISIBLE);
            homeAdvertiseIv.setVisibility(View.GONE);
            title = item.getTitle();
            ArmsUtils.getImageLoader(this).loadImage(
                    this, ImageConfigImpl.builder()
                            .url(item.getThumbnail())
                            .imageView(imageIv).build());
            commentTv.setText(item.getComment());
            likeTv.setText(item.getGood());
            readcountTv.setText(item.getView());
            titleTv.setText(item.getTitle());
            contentTv.setText(item.getExcerpt());
            authorTv.setText(item.getAuthor());
            typeTv.setText(item.getCategory());
            switch (model) {
                case 2:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setImageResource(R.drawable.library_video_play_symbol);
                    break;
                case 3:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.VISIBLE);
                    imageType.setImageResource(R.drawable.library_voice_play_symbol);
                    break;
                default:
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setVisibility(View.GONE);
            }
        }
        typeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (model) {
                    case 5:
                        Uri uri = Uri.parse(item.getHtml5());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    case 3:
//                        intent = new Intent(getActivity(), AudioDetailActivity.class);
//                        intent.putExtra("item", item);
//                        startActivity(intent);
                        break;
                    case 2:
//                        intent = new Intent(getActivity(), VideoDetailActivity.class);
//                        intent.putExtra("item", item);
//                        startActivity(intent);
                        break;
                    case 1:
//                        intent = new Intent(getActivity(), DetailActivity.class);
//                        intent.putExtra("item", item);
//                        startActivity(intent);
                        break;
                    default:
//                        intent = new Intent(getActivity(), DetailActivity.class);
//                        intent.putExtra("item", item);
//                        startActivity(intent);
                }
            }
        });
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.dandu_fragment_main_detail, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        imageIv = (ImageView) mRootview.findViewById(R.id.image_iv);
        typeContainer = (LinearLayout) mRootview.findViewById(R.id.type_container);
        commentTv = (TextView) mRootview.findViewById(R.id.comment_tv);
        likeTv = (TextView) mRootview.findViewById(R.id.like_tv);
        readcountTv = (TextView) mRootview.findViewById(R.id.readcount_tv);
        titleTv = (TextView) mRootview.findViewById(R.id.title_tv);
        contentTv = (TextView) mRootview.findViewById(R.id.content_tv);
        authorTv = (TextView) mRootview.findViewById(R.id.author_tv);
        typeTv = (TextView) mRootview.findViewById(R.id.type_tv);
        timeTv = (TextView) mRootview.findViewById(R.id.time_tv);
        imageType = (ImageView) mRootview.findViewById(R.id.image_type);
        downloadStartWhite = (ImageView) mRootview.findViewById(R.id.download_start_white);
        homeAdvertiseIv = (ImageView) mRootview.findViewById(R.id.home_advertise_iv);
        pagerContent = (RelativeLayout) mRootview.findViewById(R.id.pager_content);
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }
}
