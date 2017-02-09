package com.xiaov.ruleview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 描述: 三角形游标
 * 作者：zhangjiawei
 * 时间：2017/2/7
 */
public class TriangleView extends View {

    private Paint mPaint;
    private Path mPath;
    private int length=60;

    public TriangleView(Context context) {
        super(context);
        init();
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setPathEffect(new CornerPathEffect(3));
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(length,length);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mPath.moveTo(getMeasuredWidth()/2-length/2,0);
        mPath.lineTo(getMeasuredWidth()/2+length/2,0);
        mPath.lineTo(getMeasuredWidth()/2,length);
        mPath.close();
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
    }

    public float getSelfCenterLeft(){
        return getLeft()+length/2;
    }



    public void setLength(int length) {
        this.length = length;
    }
}
