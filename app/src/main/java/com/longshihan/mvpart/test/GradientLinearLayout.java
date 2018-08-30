package com.longshihan.mvpart.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.longshihan.mvpart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longshihan.
 *
 * @time 2018/8/2 0002
 * @des
 * @function
 */

public class GradientLinearLayout extends LinearLayout {
    private Context context;
    private View view;
    private ImageView icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8;
    private List<ImageView> imageViews;
    MyHandlerAnimation handlerAnimation;
    private int viewWidth;
    private LinearGradient mLinearGradient;
    private boolean mAnimating=true;
    private Matrix mGradientMatrix;
    private int mViewWidth = 0;
    private int mTranslate = 0;

    public GradientLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public GradientLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GradientLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.layout_gradientlayout, this);
        initView();
    }

    private void initView() {
        imageViews=new ArrayList<>();
        icon1 = findViewById(R.id.layout_gradienticon1);
        icon2 = findViewById(R.id.layout_gradienticon2);
        icon3 = findViewById(R.id.layout_gradienticon3);
        icon4 = findViewById(R.id.layout_gradienticon4);
        icon5 = findViewById(R.id.layout_gradienticon5);
        icon6 = findViewById(R.id.layout_gradienticon6);
        icon7 = findViewById(R.id.layout_gradienticon7);
        icon8 = findViewById(R.id.layout_gradienticon8);
        imageViews.add(icon1);
        imageViews.add(icon2);
        imageViews.add(icon3);
        imageViews.add(icon4);
        imageViews.add(icon5);
        imageViews.add(icon6);
        imageViews.add(icon7);
        imageViews.add(icon8);
        handlerAnimation=new MyHandlerAnimation();
        startAnimation();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null) {
            mTranslate += mViewWidth / 10;//表示每一次运动的递增值
            if (mTranslate > 2 * mViewWidth) {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            //50ms刷新一次
            postInvalidateDelayed(10);
        }
    }


    public void startAnimation(){
        handlerAnimation.sendEmptyMessageDelayed(1,100);

        mLinearGradient = new LinearGradient(-viewWidth, 0, 0, 0, new int[]
                { 0x33ffffff, 0xffffffff, 0x33ffffff }, new float[]
                { 0, 0.5f, 1 }, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            //getWidth得到是某个view的实际尺寸.
            //getMeasuredWidth是得到某view想要在parent view里面占的大小.
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
//                mPaint = getPaint();
                //线性渐变
                mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, 0, new int[]
                        { 0x33ffffff, 0xffffffff, 0x33ffffff }, new float[]
                        { 0, 0.5f, 1 }, Shader.TileMode.CLAMP);
//                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    class MyHandlerAnimation extends Handler{
        public int count;
        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i <imageViews.size(); i++) {
                if (i%6==count){
                    float offect=0.2f;
                    if (imageViews.get(i).getAlpha()>=0.8){
                        offect=-0.2f;
                    }else if (imageViews.get(i).getAlpha()<=0.2){
                        offect=0.2f;
                    }
                    imageViews.get(i).setAlpha(imageViews.get(i).getAlpha()+offect);
                }
            }
            for (int i = 0; i <imageViews.size(); i++) {
                Log.d("亮度"+i,imageViews.get(i).getAlpha()+"");
            }
            count++;
            count%=6;
//            sendEmptyMessageDelayed(1,100);
        }
    }


}
