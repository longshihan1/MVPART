package com.longshihan.module_dandu.detail.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.longshihan.module_dandu.tools.AnalysisHTML;
import com.longshihan.mvpcomponent.base.BaseMVPActivity;
import com.longshihan.mvpcomponent.di.component.AppComponent;
import com.longshihan.mvpcomponent.strategy.imageloader.glide.ImageConfigImpl;
import com.longshihan.mvpcomponent.utils.AppUtil;
import com.longshihan.mvpcomponent.utils.ArmsUtils;

public class DetailActivity extends BaseMVPActivity<DetailPresenter> implements DetailContract.View, ObservableScrollViewCallbacks {
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
    private int mParallaxImageHeight;


    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(int type, String message) {

    }

    @Override
    public void closeActivity() {

    }

    @Override
    public void showLoading() {

    }


    @Override
    public void updateListUI(DetailEntity detailEntity) {
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

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
       mPresenter=new DetailPresenter(this,appComponent.repositoryManager());
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.dandu_activity_detail;
    }

    @Override
    public void initData() {
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

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        scrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        Bundle bundle = getIntent().getExtras();
        Item item = bundle.getParcelable("item");
        if (item != null) {
            ArmsUtils.obtainAppComponentFromContext(this)
                    .imageLoader().loadImage(this, ImageConfigImpl.builder()
                    .url(item.getThumbnail())
                    .imageView(image)
                    .build());
            int mode = Integer.valueOf(item.getModel());
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            newsTopType.setText("文 字");
            newsTopDate.setText(item.getUpdate_time());
            newsTopTitle.setText(item.getTitle());
            newsTopAuthor.setText(item.getAuthor());
            newsTopLead.setText(item.getLead());
            newsTopLead.setLineSpacing(1.5f, 1.8f);
            mPresenter.getDetail(item.getId());
        }

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
}
