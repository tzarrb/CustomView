package com.tzarrb.customview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tzarrb.customview.R;

/**
 * Created by dev on 16/3/21.
 */
public class TrackEndButton extends View {
    private int mBgColor;
    private int mInnerColor;
    private int mOutterColor;
    private int mInnerWidth;
    private int mOutterWidth;
    private int mIconScr;
    private int mDuration;

    private Context mContext;
    private RectF mInnerRectF;
    private RectF mOutterRectF;
    private Rect mIconRect;
    private Paint mPaint;
    private Bitmap mBitmap;
    private int padding;

    private float mPercent = 0;

    private ValueAnimator animator;
    private int animatorStartTime = 1;
    private int animatorEndTime = 3000;

    private boolean mTouch;
    private boolean mIsEnd;

    private OnTrackButtonEndListener onTrackButtonEndListener;

    public TrackEndButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TrackEndButton,
                0,0);

        try {
            mBgColor = a.getColor(R.styleable.TrackEndButton_teBgColor, getResources().getColor(R.color.red));
            mInnerColor = a.getColor(R.styleable.TrackEndButton_teInnerColor, getResources().getColor(R.color.gray_light));
            mOutterColor = a.getColor(R.styleable.TrackEndButton_teOutterWidth, getResources().getColor(R.color.white));
            mInnerWidth = a.getDimensionPixelSize(R.styleable.TrackEndButton_teinnerWidth, dp2px(1));
            mOutterWidth = a.getDimensionPixelSize(R.styleable.TrackEndButton_teOutterWidth, dp2px(5));
            mIconScr = a.getResourceId(R.styleable.TrackEndButton_teIconSrc, R.mipmap.ic_track_end_button);
            mDuration = a.getInt(R.styleable.TrackEndButton_teDuration, 3000);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        padding = dp2px(1);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        animator = ValueAnimator.ofInt(animatorStartTime, animatorEndTime);
        animator.setDuration(mDuration);
        animator.addUpdateListener(new TrackEndingListener());
        animator.addListener(new TrackEndListener());
    }

    private int dp2px(float dp) {
        return (int) (mContext.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    private GradientDrawable creatGradientDrawable(){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR, new int[] { getResources().getColor(R.color.red), getResources().getColor(R.color.red_highlight), getResources().getColor(R.color.red_dark)});
        gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        gradientDrawable.setShape(GradientDrawable.RING);
        gradientDrawable.setCornerRadius(getWidth()/2);

        return gradientDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int hei = canvas.getHeight();

        int mColorRadial[] = {Color.parseColor("#D91D18"), Color.parseColor("#D91D18"), Color.parseColor("#D91D18"), Color.parseColor("#C61C18")};
        RadialGradient mShader = new RadialGradient(hei/2, hei/2, hei/2, mColorRadial, null, Shader.TileMode.MIRROR);
        //mPaint.setColor(mBgColor);
        mPaint.setShader(mShader);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(hei / 2, hei / 2, hei / 2, mPaint);

        mPaint.setShader(null);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mInnerColor);
        mPaint.setStrokeWidth(mInnerWidth);
        canvas.drawArc(mInnerRectF, 0, 360, false, mPaint);

        mPaint.setColor(mOutterColor);
        mPaint.setStrokeWidth(mOutterWidth);
        canvas.drawArc(mOutterRectF, 270, mPercent * 3.6f, false, mPaint);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        mBitmap = ((BitmapDrawable)mContext.getResources().getDrawable(mIconScr)).getBitmap();
        Rect srcRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        canvas.drawBitmap(mBitmap, srcRect, mIconRect, mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > height)
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updateOval();
    }


    private void updateOval() {
        mInnerRectF = new RectF(getPaddingLeft() + padding + mInnerWidth/2 + mOutterWidth, getPaddingTop() + padding + mInnerWidth/2 + mOutterWidth,
                getWidth() - getPaddingRight() - padding - mInnerWidth/2 - mOutterWidth,
                getHeight() - getPaddingBottom() - padding - mInnerWidth/2 - mOutterWidth);

        mOutterRectF = new RectF(getPaddingLeft() + padding + mOutterWidth/2, getPaddingTop() + padding + mOutterWidth/2,
                getWidth() - getPaddingRight() - padding - mOutterWidth/2,
                getHeight() - getPaddingBottom() - padding - mOutterWidth/2);

        mIconRect = new Rect(getPaddingLeft() + padding*12 + mInnerWidth + mOutterWidth, getPaddingTop() + padding*12 + mInnerWidth + mOutterWidth,
                getWidth() - getPaddingRight() - padding*12 - mInnerWidth - mOutterWidth,
                getHeight() - getPaddingBottom() - padding*12 - mInnerWidth - mOutterWidth);
    }


    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float mPercent) {
        this.mPercent = mPercent;
        refreshTheLayout();
    }

    public void setOnTrackButtonEndListener(OnTrackButtonEndListener onTrackButtonEndListener){
        this.onTrackButtonEndListener = onTrackButtonEndListener;
    }

    public void refreshTheLayout() {
        invalidate();
        requestLayout();
    }


    private class TrackEndingListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            setPercent(value * 100f / animatorEndTime);

        }
    }

    private class TrackEndListener implements ValueAnimator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
            setPercent(0);
            mIsEnd = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mTouch){
                setPercent(100);
                mIsEnd = true;
                if (onTrackButtonEndListener != null){
                    onTrackButtonEndListener.OnTrackButtonEnd(getId());
                }
            }else{
                setPercent(0);
                mIsEnd = false;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Handle return value better.
        // TODO Moving outside view doesn't cancel notification nor progress updates

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mTouch = true;
                if (!mIsEnd){
                    animator.start();
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:
                mTouch = false;
                if (!mIsEnd){
                    animator.cancel();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mTouch = false;
                if (!mIsEnd){
                    animator.cancel();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public interface OnTrackButtonEndListener{
        public void OnTrackButtonEnd(int viewId);
    }

}
