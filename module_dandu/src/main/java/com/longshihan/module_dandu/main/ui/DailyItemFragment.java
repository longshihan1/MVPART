package com.longshihan.module_dandu.main.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.mvpcomponent.base.BaseMVPFragment;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.ArmsUtils;
import com.longshihan.mvpcomponent.utils.TimeUtil;

/**
 * @author longshihan
 * @time 2017/9/5 15:31
 * @des
 */

public class DailyItemFragment extends BaseMVPFragment {
    TextView monthTv;
    TextView yearTv;
    RelativeLayout dateRl;
    ImageView calendarIv;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    public static Fragment getInstance(Item item) {
        Fragment fragment = new DailyItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Item item = getArguments().getParcelable("item");
        ArmsUtils.obtainAppComponentFromContext(this)
                .imageLoader().loadImage(this, ImageConfigImpl.builder()
                .url(item.getThumbnail())
                .imageView(calendarIv)
                .build());
        String[] arrayOfString = TimeUtil.getCalendarShowTime(item.getUpdate_time());
        if ((arrayOfString != null) && (arrayOfString.length == 3)) {
            monthTv.setText(arrayOfString[1] + " , " + arrayOfString[2]);
            yearTv.setText(arrayOfString[0]);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dandu_item_daily, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        monthTv = (TextView) mRootview.findViewById(R.id.dandu_dailyitem_month_tv);
        yearTv = (TextView) mRootview.findViewById(R.id.dandu_dailyitem_year_tv);
        dateRl = (RelativeLayout) mRootview.findViewById(R.id.dandu_dailyitem_date_rl);
        calendarIv = (ImageView) mRootview.findViewById(R.id.dandu_dailyitem_calendar_iv);
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }
}
