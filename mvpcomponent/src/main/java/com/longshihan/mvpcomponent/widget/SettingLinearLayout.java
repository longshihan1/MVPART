package com.longshihan.mvpcomponent.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longshihan.mvpcomponent.R;

/**
 * @author longshihan
 * @time 2017/8/18 11:35
 * @des 绘制单一条目，主要用于设置
 */

public class SettingLinearLayout extends LinearLayout {
    private ImageView leftImage;
    private TextView textView;
    private ImageView rightImage;
    private TextView rightText;
    private LayoutParams leftParams, rightParams, rightTextParams;
    int leftimage_resource, rightimage_resource;
    String text, righttextStr;
    float textsize, righttext_textsize;
    float leftImagewidth, leftImageheight, rightImagewidth, rightImageheight;
    float leftmargin, leftmarginText, rightmargin, righttext_marginright;
    private boolean imageshow, righttext_show, leftImage_show;


    public SettingLinearLayout(Context context) {
        this(context, null);
    }

    public SettingLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.settinglinear);
        leftimage_resource = a.getResourceId(R.styleable.settinglinear_leftImage, -1);
        rightimage_resource = a.getResourceId(R.styleable.settinglinear_rightImage, R.drawable.back_right);
        text = a.getString(R.styleable.settinglinear_settingText);
        textsize = a.getDimension(R.styleable.settinglinear_textsize, 20);
        leftImagewidth = a.getDimension(R.styleable.settinglinear_leftImagewidth, 24);
        leftImageheight = a.getDimension(R.styleable.settinglinear_leftImageheight, 24);
        rightImagewidth = a.getDimension(R.styleable.settinglinear_rightImagewidth, 8);
        rightImageheight = a.getDimension(R.styleable.settinglinear_rightImageheight, 14);
        leftmargin = a.getDimension(R.styleable.settinglinear_leftmargin, 10);
        leftmarginText = a.getDimension(R.styleable.settinglinear_leftmarginText, 20);
        rightmargin = a.getDimension(R.styleable.settinglinear_rightmargin, 17);
        imageshow = a.getBoolean(R.styleable.settinglinear_rightimage_show, true);
        righttext_show = a.getBoolean(R.styleable.settinglinear_righttext_show, true);
        righttext_marginright = a.getDimension(R.styleable.settinglinear_righttext_marginleft, 15);
        righttext_textsize = a.getDimension(R.styleable.settinglinear_righttext_textsize, 20);
        righttextStr = a.getString(R.styleable.settinglinear_righttext);
        leftImage_show = a.getBoolean(R.styleable.settinglinear_leftImage_show, true);
        a.recycle();

        if (leftImage_show) {
            leftParams = new LayoutParams((int) leftImagewidth, (int) leftImageheight);
            leftParams.setMargins((int) leftmargin, 0, (int) leftmarginText, 0);
            leftImage.setLayoutParams(leftParams);
            leftImage.setImageResource(leftimage_resource);
        } else {
            leftImage.setVisibility(GONE);
        }

        if (imageshow) {
            rightParams = new LayoutParams((int) rightImagewidth, (int) rightImageheight);
            rightParams.setMargins(0, 0, (int) rightmargin, 0);
            rightImage.setLayoutParams(rightParams);
            rightImage.setImageResource(rightimage_resource);
        } else {
            rightImage.setVisibility(GONE);
        }


        if (righttext_show) {
            rightTextParams = (LayoutParams) rightText.getLayoutParams();
            rightTextParams.setMargins(0, 0, (int) righttext_marginright, 0);
            rightText.setLayoutParams(rightTextParams);
            rightText.setText(righttextStr);
            rightText.setTextSize(righttext_textsize);
        } else {
            rightText.setVisibility(GONE);
        }

        textView.setText(text);
        textView.setTextSize(textsize);


    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.settinglinear, this, true);
        leftImage = (ImageView) view.findViewById(R.id.settinglinear_leftimage);
        textView = (TextView) view.findViewById(R.id.settinglinear_textview);
        rightText = (TextView) view.findViewById(R.id.settinglinear_righttext);
        rightImage = (ImageView) view.findViewById(R.id.settinglinear_rightimage);
    }

    public void setrightText(String message) {
        rightText.setText(message);
    }

    public TextView getTextView(){
        return textView;
    }

    public ImageView getRightImage() {
        return rightImage;
    }
}
