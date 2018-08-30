package com.longshihan.mvpart;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longshihan.mvpart.test.GradientTextView;
import com.longshihan.mvpcomponent.utils.AppUtil;

public class Main5Activity extends AppCompatActivity {
    private Context context;
    private View view;
    private RelativeLayout bottomRl,containerRl;
    private TextView msgTxt,btn,ssidName;
    private ImageView bigImg,bgImg,planeImg;
    private int viewHeight,bgViewHeight,planeViewHeight;
    private int timeDeley=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        context=this;
        ssidName=findViewById(R.id.layout_wificonnsuccess_name);
        msgTxt=findViewById(R.id.layout_wificonnsuccess_noticetxt);
        btn=findViewById(R.id.layout_wificonnsuccess_btn);
        bottomRl=findViewById(R.id.layout_wificonnsuccess_bottomrl);
        bigImg=findViewById(R.id.layout_wificonnsuccess_bigimg);
        containerRl=findViewById(R.id.layout_wificonnsuccess_contanerrl);
        bgImg=findViewById(R.id.layout_wificonnsuccess_bgimg);
        planeImg=findViewById(R.id.layout_wificonnsuccess_planeimg);
        initData();
    }

    private void initData() {
        viewHeight=1920-AppUtil.dp2px(context,185);
        bgViewHeight= AppUtil.dp2px(context,90);

        RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams) containerRl.getLayoutParams();
        layoutParams1.height= (int) (1920*0.74);
        layoutParams1.width= (int) (1920*0.74);
        containerRl.setLayoutParams(layoutParams1);

        ssidName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSuccess();
            }
        });
    }


    /**
     * 开始成功的view
     */
    public void resetSuccess() {
        Log.d("[HZWifi]","成功页面开始动画:"+viewHeight+":"+bgViewHeight+":");
        //设置半屏高度

        bgImg.setAlpha(0f);
        planeImg.setAlpha(0f);

        bottomRl.scrollTo(0,-viewHeight);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,viewHeight);
        valueAnimator.setDuration(timeDeley);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int currentValue = (Integer) animator.getAnimatedValue();
                bottomRl.scrollTo(0,currentValue-viewHeight);
            }
        });
        valueAnimator.start();
        ObjectAnimator objectAnimator= ObjectAnimator.ofFloat(bottomRl,"alpha",0,1);
        objectAnimator.setDuration(timeDeley/2);
        objectAnimator.start();

        ObjectAnimator translationY0 = new ObjectAnimator().ofFloat(bgImg,"translationY",0,-bgViewHeight);
        translationY0.setDuration(timeDeley);
        translationY0.setStartDelay((long) (timeDeley*0.45f));
        translationY0.start();
        ObjectAnimator objectAnimator0= ObjectAnimator.ofFloat(bgImg,"alpha",0,1);
        objectAnimator0.setDuration(timeDeley);
        objectAnimator0.setStartDelay((long) (timeDeley*0.45f));
        objectAnimator0.start();

        ObjectAnimator translationY1 = new ObjectAnimator().ofFloat(planeImg,"translationY",0,-bgViewHeight);
        translationY1.setStartDelay(timeDeley);
        translationY1.setDuration(timeDeley);
        translationY1.start();

        ObjectAnimator translationX1 = new ObjectAnimator().ofFloat(planeImg,"translationX",0,bgViewHeight/2);
        translationX1.setStartDelay(timeDeley);
        translationX1.setDuration(timeDeley);
        translationX1.start();

        ObjectAnimator objectAnimator1= ObjectAnimator.ofFloat(planeImg,"alpha",0,1);
        objectAnimator1.setDuration(timeDeley);
        objectAnimator1.setStartDelay(timeDeley);
        objectAnimator1.start();

    }
}
