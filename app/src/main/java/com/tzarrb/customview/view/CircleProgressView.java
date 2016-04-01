package com.tzarrb.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.tzarrb.customview.R;

/**
 * Created by qiqi on 15/11/3.
 */
public class CircleProgressView extends View {


    private float mPercent = 0;
    private float mStrokeWidth;
    private int mBgColor;
    private float mStartAngle = 0;
    private int mFgColorStart;
    private int mFgColorEnd;

    private LinearGradient mShader;
    private Context mContext;
    private RectF mOval;
    private Paint mPaint;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircleProgressView,
                0,0);

        try {
            mBgColor = a.getColor(R.styleable.CircleProgressView_cpBgColor, getResources().getColor(R.color.gray_dark));
            mFgColorEnd = a.getColor(R.styleable.CircleProgressView_cpFgColorEnd, getResources().getColor(R.color.green_light));
            mFgColorStart = a.getColor(R.styleable.CircleProgressView_cpFgColorStart, getResources().getColor(R.color.green_light));
            mPercent = a.getFloat(R.styleable.CircleProgressView_cpPercent, 0);
            mStartAngle = a.getFloat(R.styleable.CircleProgressView_cpStartAngle, 0) + 270;
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressView_cpStrokeWidth, dp2px(10));
            System.out.println("**** m"+mStrokeWidth);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private int dp2px(float dp) {
        return (int) (mContext.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setShader(null);
        mPaint.setColor(mBgColor);
        canvas.drawArc(mOval, 0, 360, false, mPaint);

        mPaint.setShader(mShader);
        canvas.drawArc(mOval, mStartAngle, mPercent * 3.6f, false, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updateOval();

        mShader = new LinearGradient(mOval.left, mOval.top,
                mOval.left, mOval.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.MIRROR);
    }

    private void updateOval() {
        int xp = getPaddingLeft() + getPaddingRight();
        int yp = getPaddingBottom() + getPaddingTop();
        mOval = new RectF(getPaddingLeft() + mStrokeWidth, getPaddingTop() + mStrokeWidth,
                getPaddingLeft() + (getWidth() - xp) - mStrokeWidth,
                getPaddingTop() + (getHeight() - yp) - mStrokeWidth);
    }

    public void refreshTheLayout() {
        invalidate();
        requestLayout();
    }

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float mPercent) {
        this.mPercent = mPercent;
        refreshTheLayout();
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
        updateOval();
        refreshTheLayout();
    }

    public void setStrokeWidthDp(float dp) {
        this.mStrokeWidth = dp2px(dp);
        mPaint.setStrokeWidth(mStrokeWidth);
        updateOval();
        refreshTheLayout();
    }

    public int getFgColorStart() {
        return mFgColorStart;
    }

    public void setFgColorStart(int mFgColorStart) {
        this.mFgColorStart = mFgColorStart;
        mShader = new LinearGradient(mOval.left, mOval.top,
                mOval.left, mOval.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.MIRROR);
        refreshTheLayout();
    }

    public int getFgColorEnd() {
        return mFgColorEnd;
    }

    public void setFgColorEnd(int mFgColorEnd) {
        this.mFgColorEnd = mFgColorEnd;
        mShader = new LinearGradient(mOval.left, mOval.top,
                mOval.left, mOval.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.MIRROR);
        refreshTheLayout();
    }


    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle + 270;
        refreshTheLayout();
    }
}
