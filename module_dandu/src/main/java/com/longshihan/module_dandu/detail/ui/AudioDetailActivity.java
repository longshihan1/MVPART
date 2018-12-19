package com.longshihan.module_dandu.detail.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.longshihan.module_dandu.R;
import com.longshihan.module_dandu.detail.preserenter.DetailContract;
import com.longshihan.module_dandu.detail.preserenter.DetailPresenter;
import com.longshihan.module_dandu.mvp.model.entity.DetailEntity;
import com.longshihan.module_dandu.mvp.model.entity.Item;
import com.longshihan.module_dandu.player.IPlayback;
import com.longshihan.module_dandu.player.PlayState;
import com.longshihan.module_dandu.player.PlaybackService;
import com.longshihan.module_dandu.tools.AnalysisHTML;
import com.longshihan.module_dandu.tools.TimeUtils;
import com.longshihan.mvpcomponent.base.BaseMVPActivity;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.intergration.AppManager;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.AppUtil;
import com.longshihan.mvpcomponent.utils.ArmsUtils;
import com.orhanobut.logger.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class AudioDetailActivity extends BaseMVPActivity<DetailPresenter> implements DetailContract.View, ObservableScrollViewCallbacks, IPlayback.Callback,
        View.OnClickListener {
    ImageView favorite;
    ImageView write;
    ImageView share;
    Toolbar toolBar;
    WebView webView;
    ObservableScrollView scrollView;
    ImageView image;
    LinearLayout newsParseWeb;
    TextView newsTopType;
    TextView newsTopDate;
    TextView newsTopTitle;
    TextView newsTopAuthor;
    TextView newsTopLead;
    LinearLayout newsTop;
    View newsTopImgUnderLine;
    View newsTopLeadLine;

    AppCompatImageView buttonPlayLast;
    AppCompatImageView buttonPlayToggle;
    AppCompatImageView buttonPlayNext;
    TextView textViewProgress;
    AppCompatSeekBar seekBar;
    TextView textViewDuration;

    private int mParallaxImageHeight;
    private PlaybackService mPlaybackService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("PlaybackService初始化成功");
            mPlaybackService = ((PlaybackService.LocalBinder) service).getService();
            mPlaybackService.setName(item.getAuthor());
            mPlaybackService.setTitle(item.getTitle());
            register();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d("PlaybackService关闭");
            unRegister();
            mPlaybackService = null;
        }
    };
    private Timer timer;

    private void register() {
        mPlaybackService.registerCallback(this);
    }

    private void unRegister() {
        if (mPlaybackService != null) {
            mPlaybackService.unregisterCallback(this);
        }
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(int type, String message) {

    }

    @Override
    public void closeActivity() {
        cancelTimer();
        unRegister();
        mPlaybackService = null;
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public void showLoading() {

    }


    String song;

    @Override
    public void updateListUI(DetailEntity detailEntity) {
        song = detailEntity.getFm();
        if (detailEntity.getParseXML() == 1) {
            int i = detailEntity.getLead().trim().length();
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, detailEntity.getContent(), analysisHTML.HTML_STRING, newsParseWeb, i);
            newsTopType.setText("文 字");
        } else {
            initWebViewSetting();
            newsParseWeb.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            newsTop.setVisibility(View.GONE);
            webView.loadUrl(addParams2WezeitUrl(detailEntity.getHtml5(), false));
        }
    }

    @Override
    public void showOnFailure() {

    }

    AppComponent mAppComponent;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
        mPresenter=new DetailPresenter(this,appComponent.repositoryManager());

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.dandu_activity_audio_detail;
    }

    Item item = null;

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        item = bundle.getParcelable("item");
        favorite = getView(R.id.favorite);
        write = getView(R.id.write);
        share = getView(R.id.share);
        toolBar = getView(R.id.dandu_detailtoolBar);
        webView = getView(R.id.webView);
        scrollView = getView(R.id.dandu_detaul_scrollView);
        image = getView(R.id.dandu_detaul_image);
        newsParseWeb = getView(R.id.news_parse_web);
        newsTopType = getView(R.id.news_top_type);
        newsTopDate = getView(R.id.news_top_date);
        newsTopTitle = getView(R.id.news_top_title);
        newsTopAuthor = getView(R.id.news_top_author);
        newsTopLead = getView(R.id.news_top_lead);
        newsTop = getView(R.id.news_top);
        newsTopImgUnderLine = getView(R.id.news_top_img_under_line);
        newsTopLeadLine = getView(R.id.news_top_lead_line);
        buttonPlayLast = getView(R.id.button_play_last);
        buttonPlayToggle = getView(R.id.button_play_toggle);
        buttonPlayNext = getView(R.id.button_play_next);
        textViewProgress = getView(R.id.text_view_progress);
        seekBar = getView(R.id.seek_bar);
        textViewDuration = getView(R.id.text_view_duration);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        scrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        if (item != null) {
            ArmsUtils.obtainAppComponentFromContext(this)
                    .imageLoader().loadImage(this, ImageConfigImpl.builder()
                    .url(item.getThumbnail())
                    .imageView(image)
                    .build());
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            newsTopType.setText("音 频");
            newsTopDate.setText(item.getUpdate_time());
            newsTopTitle.setText(item.getTitle());
            newsTopAuthor.setText(item.getAuthor());
            newsTopLead.setText(item.getLead());
            newsTopLead.setLineSpacing(1.5f, 1.8f);
            mPresenter.getDetail(item.getId());
        }
        initListener();
        bindPlaybackService();
    }

    private void initListener() {
        buttonPlayToggle.setOnClickListener(this);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                cancelTimer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlaybackService.seekTo(getSeekDuration(seekBar.getProgress()));
                playTimer();
            }
        });
    }

    public void bindPlaybackService() {
        this.bindService(new Intent(this, PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);

    }

    private void initWebViewSetting() {
        WebSettings localWebSettings = this.webView.getSettings();
        localWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setSupportZoom(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setLoadWithOverviewMode(true);
    }


    public String addParams2WezeitUrl(String url, boolean paramBoolean) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(url);
        localStringBuffer.append("?client=android");
        localStringBuffer.append("&device_id=" + AppUtil.getDeviceId(this));
        localStringBuffer.append("&version=" + "1.3.0");
        if (paramBoolean)
            localStringBuffer.append("&show_video=0");
        else {
            localStringBuffer.append("&show_video=1");
        }
        return localStringBuffer.toString();
    }

    @Override
    public boolean useFragment() {
        return false;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onComplete(PlayState state) {
        Logger.d("onComplete.......");
        cancelTimer();
    }


    @Override
    public void onPlayStatusChanged(PlayState status) {
        Logger.d("onPlayStatusChanged.......status=" + status);
        switch (status) {
            case INIT:
                break;
            case PREPARE:
                break;
            case PLAYING:
                updateDuration();
                playTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                Logger.d(mPlaybackService.getDuration());
                break;
            case PAUSE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_play);
                break;
            case ERROR:
                break;
            case COMPLETE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.ic_play);
                seekBar.setProgress(0);
                break;
        }
    }

    @Override
    public void onPosition(int position) {
        Logger.d("onPosition.......=" + position);
    }

    private void playTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mPlaybackService == null)
                    return;
                if (mPlaybackService.isPlaying()) {
                    handleProgress.post(runnable);
                }
            }
        }, 0, 1000);
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mPlaybackService.isPlaying()) {
                if (isFinishing()) {
                    return;
                }
                int progress = (int) (seekBar.getMax()
                        * ((float) mPlaybackService.getProgress() / (float) mPlaybackService.getDuration()));
                updateProgressTextWithProgress(mPlaybackService.getProgress());
                if (progress >= 0 && progress <= seekBar.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar.setProgress(progress, true);
                    } else {
                        seekBar.setProgress(progress);
                    }
                }
            }
        }
    };
    Handler handleProgress = new Handler();

    private void updateProgressTextWithProgress(int progress) {
        textViewProgress.setText(TimeUtils.formatDuration(progress));
    }

    private void updateDuration() {
        textViewDuration.setText(TimeUtils.formatDuration(mPlaybackService.getDuration()));
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_play_toggle) {
            if (mPlaybackService == null || song == null) {
                Logger.d("mPlaybackService == null");
                return;
            }
            if (mPlaybackService.isPlaying()) {
                if (song.equals(mPlaybackService.getSong())) {
                    mPlaybackService.pause();
                    buttonPlayToggle.setImageResource(R.drawable.ic_play);
                } else {
                    mPlaybackService.play(song);
                    buttonPlayToggle.setImageResource(R.drawable.ic_pause);
                }
            } else {
                if (song.equals(mPlaybackService.getSong())) {
                    mPlaybackService.play();
                } else {
                    mPlaybackService.play(song);
                }
                buttonPlayToggle.setImageResource(R.drawable.ic_pause);
            }
        }
    }

    private int getSeekDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / seekBar.getMax()));
    }

    private int getCurrentSongDuration() {
        int duration = 0;
        if (mPlaybackService != null) {
            duration = mPlaybackService.getDuration();
        }
        return duration;
    }
}
