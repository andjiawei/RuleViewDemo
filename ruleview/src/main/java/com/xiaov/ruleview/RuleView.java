package com.xiaov.ruleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import static android.R.attr.y;
import static android.content.ContentValues.TAG;
import static android.support.v7.widget.AppCompatDrawableManager.get;
import static android.transition.Fade.IN;

/**
 * 描述: 尺子
 * 作者：zhangjiawei
 * 时间：2017/2/6
 */
public class RuleView extends View {

    private int gap;//一格是20个像素
    private int count;//标尺总数
    private int fatLine;//粗线的宽度
    private int thinLine;//细线的宽度

    private Paint mPaint;
    private int startX;
    private int lastX;
    private int mMeasureHeigth;
    private float mOffsetRight;
    private Scroller mScroller;
    private VelocityTracker velocityTracker;

    public RuleView(Context context) {
        super(context);
        init();
    }

    public RuleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RuleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.parseColor("#FCDD00"));
        mScroller = new Scroller(getContext());
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(60);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) (mPaint.measureText(count+"")/2+mPaint.measureText("0")/2+count*gap), mMeasureHeigth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startX= (int)(mPaint.measureText("0")/2);//fix view destroy data exit
        for (int i = 0; i < count+1; i++) {
            if(i%10==0){
                String text = String.valueOf(i);
                mPaint.setStrokeWidth(fatLine);
                canvas.drawLine(startX, 0, startX , 100, mPaint);//line height 100
                canvas.drawText(text,startX- mPaint.measureText(text)/2,150, mPaint);//from y 150
            }else{
                mPaint.setStrokeWidth(thinLine);
                canvas.drawLine(startX, 0, startX ,60, mPaint);//line height 60
            }
            startX = startX + gap;
        }
    }

    //用属性动画滚动
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = x - lastX;
//               layout(getLeft() + offsetX, getTop() ,getRight() + offsetX, getBottom());//用layout移动的方式 回调给上层View时 move失效
                float translationX = getTranslationX() + offsetX;
                //限制向右滑动 0
                if(translationX>= mOffsetRight && mOffsetRight !=0 ){
                    translationX= mOffsetRight;
                }
                //限制向左滑动 100
                if(Math.abs(translationX)>=getMeasuredWidth()- mOffsetRight -mPaint.measureText(count+"")/2-mPaint.measureText("0")/2){
                    translationX=-getMeasuredWidth()+ mOffsetRight +mPaint.measureText(count+"")/2+mPaint.measureText("0")/2;
                }
                setTranslationX(translationX);

                if(mListener!=null){
                    mListener.onMoveChange(getTranslationX());
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public float getSelfLeft(){
        return getLeft()+mPaint.measureText("0")/2;
    }

    //设置的移动到0时停止
    public void setOffsetRight(float offsetRight) {
        mOffsetRight = offsetRight;
    }

    /**
     * ----------------在外配置的setter 方法----------------------------
     */

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFatLine(int fatLine) {
        this.fatLine = fatLine;
    }

    public void setThinLine(int thinLine) {
        this.thinLine = thinLine;
    }

    /**
     * ----------------移动时回调的监听----------------------------
     */

    public OnMoveChangeListener mListener;
    public void setOnMoveChangeListener(OnMoveChangeListener listener){
        mListener = listener;
    }
    public interface OnMoveChangeListener{
        void onMoveChange(float value);
    }
}
