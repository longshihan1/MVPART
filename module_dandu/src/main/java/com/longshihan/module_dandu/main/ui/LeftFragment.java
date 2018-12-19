package com.longshihan.module_dandu.main.ui;


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
import com.longshihan.module_dandu.mvp.model.entity.LeftMenuEvent;
import com.longshihan.module_dandu.utils.DanDuConstacts;
import com.longshihan.mvpcomponent.base.BaseMVPFragment;
import com.longshihan.mvpcomponent.di.component.AppComponent;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftFragment extends BaseMVPFragment implements View.OnClickListener {
    ImageView rightSlideSetting;
    ImageView search;
    TextView homePageTv;
    TextView wordsTv;
    TextView voiceTv;
    TextView videoTv;
    TextView calendarTv;
    LinearLayout messageLinear;
    LinearLayout collLinear;
    LinearLayout nolineLinear;
    LinearLayout noteLinear;
    RelativeLayout titleBar;
    View mRootView;
    private ImageView login;

    public LeftFragment() {
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dandu_leftmenu_right_slide_close) {
//            startActivity(new Intent(getActivity(), SettingActivity.class));
        } else if (i == R.id.dandu_leftmenu_search) {
//            startActivity(new Intent(getActivity(), SearchActivity.class));
        } else if (i == R.id.dandu_leftmenu_home_page_tv) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.HomePageTV));
        } else if (i == R.id.dandu_leftmenu_words_tv) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.WordsTV));
        } else if (i == R.id.dandu_leftmenu_voice_tv) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.VoiceTV));
        } else if (i == R.id.dandu_leftmenu_video_tv) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.VodeoTV));
        } else if (i == R.id.dandu_leftmenu_calendar_tv) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.CalendarTv));
        } else if (i == R.id.dandu_leftmenu_msg) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.MessageLinear));
        } else if (i == R.id.dandu_leftmenu_coll) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.CollLinear));
        } else if (i == R.id.dandu_leftmenu_noline) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.NolineLinear));
        } else if (i == R.id.dandu_leftmenu_neta) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.NoteLinear));
        } else if (i == R.id.dandu_leftmenu_title_bar) {
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.TitleBar));
        } else if (i== R.id.dandu_leftmenu_login){
            EventBus.getDefault().postSticky(new LeftMenuEvent(DanDuConstacts.Login));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dandu_fragment_left, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        rightSlideSetting = (ImageView) mRootView.findViewById(R.id.dandu_leftmenu_right_slide_close);
        search = (ImageView) mRootView.findViewById(R.id.dandu_leftmenu_search);
        homePageTv = (TextView) mRootView.findViewById(R.id.dandu_leftmenu_home_page_tv);
        wordsTv = (TextView) mRootView.findViewById(R.id.dandu_leftmenu_words_tv);
        voiceTv = (TextView) mRootView.findViewById(R.id.dandu_leftmenu_voice_tv);
        videoTv = (TextView) mRootView.findViewById(R.id.dandu_leftmenu_video_tv);
        calendarTv = (TextView) mRootView.findViewById(R.id.dandu_leftmenu_calendar_tv);
        messageLinear = (LinearLayout) mRootView.findViewById(R.id.dandu_leftmenu_msg);
        collLinear = (LinearLayout) mRootView.findViewById(R.id.dandu_leftmenu_coll);
        nolineLinear = (LinearLayout) mRootView.findViewById(R.id.dandu_leftmenu_noline);
        noteLinear = (LinearLayout) mRootView.findViewById(R.id.dandu_leftmenu_neta);
        titleBar = (RelativeLayout) mRootView.findViewById(R.id.dandu_leftmenu_title_bar);
        login= (ImageView) mRootView.findViewById(R.id.dandu_leftmenu_login);
        rightSlideSetting.setOnClickListener(this);
        search.setOnClickListener(this);
        homePageTv.setOnClickListener(this);
        wordsTv.setOnClickListener(this);
        voiceTv.setOnClickListener(this);
        videoTv.setOnClickListener(this);
        calendarTv.setOnClickListener(this);
        messageLinear.setOnClickListener(this);
        collLinear.setOnClickListener(this);
        nolineLinear.setOnClickListener(this);
        noteLinear.setOnClickListener(this);
        titleBar.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {

    }
}
