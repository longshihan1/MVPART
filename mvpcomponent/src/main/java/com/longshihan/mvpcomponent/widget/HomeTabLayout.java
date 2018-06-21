package com.longshihan.mvpcomponent.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longshihan.mvpcomponent.R;


/**
 * MainActivity的tab
 * Created by dell on 2016/8/2.
 */
public class HomeTabLayout extends LinearLayout {
    private ImageView imageView;
    private TextView textView;
    private TextView msgCount;
    private int position;
    float txtsize;
    int image;
    String text;
    float imagemargin;
    float imagewh, imageww;
    float color;
    int count=0;


    public HomeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeTabLayout);
        image = a.getResourceId(R.styleable.HomeTabLayout_tabImage, -1);
        text = a.getString(R.styleable.HomeTabLayout_tabText);
        imagemargin = a.getDimension(R.styleable.HomeTabLayout_imagemargintext, 0);
        position = a.getInteger(R.styleable.HomeTabLayout_tabPosition, 0);
        imagewh = a.getDimension(R.styleable.HomeTabLayout_imagewh, 28);
        imageww = a.getDimension(R.styleable.HomeTabLayout_imagewh, 28);
        txtsize = a.getDimension(R.styleable.HomeTabLayout_tabtextsize, 12);
        a.recycle();

        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams((int) imageww, (int) imagewh);
        layout.setMargins(0, 0, 0, (int) imagemargin);
        layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layout);
        imageView.setImageResource(image);
        textView.setText(text);
        textView.setTextSize(txtsize);
    }

    public HomeTabLayout(Context context) {
        super(context);
        initView(context);
    }

    public HomeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeTabLayout);
        image = a.getResourceId(R.styleable.HomeTabLayout_tabImage, -1);
        text = a.getString(R.styleable.HomeTabLayout_tabText);
        imagemargin = a.getDimension(R.styleable.HomeTabLayout_imagemargintext, 0);
        position = a.getInteger(R.styleable.HomeTabLayout_tabPosition, 0);
        imagewh = a.getDimension(R.styleable.HomeTabLayout_imagewh, 25);
        imageww = a.getDimension(R.styleable.HomeTabLayout_imageww, 25);
        txtsize = a.getDimension(R.styleable.HomeTabLayout_tabtextsize, 12);
        a.recycle();

        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams((int) imageww, (int) imagewh);
        layout.setMargins(0, 0, 0, (int) imagemargin);
        layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layout);

        imageView.setImageResource(image);
        textView.setText(text);
        textView.setTextSize(txtsize);

    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_tab_item, this, true);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        textView = (TextView) view.findViewById(R.id.textview);
        textView.setTextSize(10);
        msgCount = (TextView) view.findViewById(R.id.tv_msg_count);
    }


    public void setMsgCount(int count) {

        if (count > 0) {
            msgCount.setText(String.valueOf(count));
            msgCount.setVisibility(View.VISIBLE);
        } else {
            msgCount.setVisibility(View.INVISIBLE);
        }
    }

    public void setCartCount(int count) {
        this.count=count;
        if (count > 0) {
            ViewGroup.LayoutParams layoutParams = msgCount.getLayoutParams();
            layoutParams.height = 20;
            layoutParams.width = 20;
            msgCount.setLayoutParams(layoutParams);
            msgCount.setVisibility(View.VISIBLE);
        } else {
            msgCount.setVisibility(View.GONE);
        }
    }

    public void setTextColor(int colors) {
        textView.setTextColor(getResources().getColor(colors));
    }

    public void setImageView(int imageId) {
        imageView.setImageResource(imageId);
    }

    public void setAnimation(AnimationSet animationset) {
        imageView.startAnimation(animationset);
    }

    public void stopAnimation() {
        imageView.clearAnimation();
        textView.clearAnimation();
    }

    public View getImageview() {
        return imageView;
    }
    public View getTexiView() {
        return msgCount;
    }

    public void cancelTextviewCount(){
        if (count>0){
            msgCount.setVisibility(GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    msgCount.setVisibility(VISIBLE);
                }
            }, 500);
        }
    }

    public void startTextAnimation(AnimationSet animationSet) {
        textView.startAnimation(animationSet);
    }


    public void startValueAnamition() {

    }
}
