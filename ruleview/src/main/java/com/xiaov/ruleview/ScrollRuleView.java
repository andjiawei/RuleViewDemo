package com.xiaov.ruleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import static android.R.attr.value;


/**
 * 描述:带游标 可滚动的尺子
 * 作者：zhangjiawei
 * 时间：2017/2/6
 */
public class ScrollRuleView extends RelativeLayout implements RuleView.OnMoveChangeListener {

    private TextView mTvResult;
    private RuleView ruleView;
    private TriangleView triangleView;

    private int gap;//一格是20个像素
    private int count;
    private int fatLine;
    private int thinLine;
    private int length;

    public ScrollRuleView(Context context) {
        super(context);
        init();
    }

    public ScrollRuleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.ScrollRuleView);

        gap=ta.getInt(R.styleable.ScrollRuleView_gap,20);
        count=ta.getInt(R.styleable.ScrollRuleView_count,100);
        fatLine=ta.getInt(R.styleable.ScrollRuleView_fatLine,10);
        thinLine=ta.getInt(R.styleable.ScrollRuleView_thinLine,8);
        length =ta.getInt(R.styleable.ScrollRuleView_length,80);

        ta.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_scroll_rule,this);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        ruleView = (RuleView) findViewById(R.id.rule_View);
        triangleView = (TriangleView) findViewById(R.id.triangleView);
        ruleView.setOnMoveChangeListener(this);
        initRuleViewData();
        initTriangleViewData();
        post(new Runnable() {
            @Override
            public void run() {
                //限制尺子到0时，停止
                ruleView.setOffsetRight(triangleView.getSelfCenterLeft()-ruleView.getSelfLeft());
                //默认值
                mTvResult.setText(new DecimalFormat("#0.0").format((triangleView.getSelfCenterLeft()-ruleView.getSelfLeft())/gap)+""+"CM");
            }
        });
    }

    private void initTriangleViewData() {
        triangleView.setLength(length);
    }

    private void initRuleViewData() {
        ruleView.setCount(count);
        ruleView.setFatLine(fatLine);
        ruleView.setThinLine(thinLine);
        ruleView.setGap(gap);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        //限制尺子到0时，停止
//        ruleView.setOffsetRight(triangleView.getSelfCenterLeft()-ruleView.getSelfLeft());
    }

    @Override
    public void onMoveChange(final float value) {
        mTvResult.setText(new DecimalFormat("#0.0").format((-value+triangleView.getSelfCenterLeft()-ruleView.getSelfLeft())/gap)+""+"CM");
    }
}
