package com.zhouchengang.backuponline.album;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhouchengang.fileonlinelaunchapp.R;

/**
 * @author zhouchengang
 * @date 2021/2/8
 * @desc
 */
public class MyPicView extends androidx.appcompat.widget.AppCompatImageView {

    public MyPicView(Context context) {
        super(context);
        initView(context, null);
    }

    public MyPicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MyPicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        //LayoutInflater.from(context).inflate(R.layout.view_my_constra, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width;
        //Log.e("zcg",""+width);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
