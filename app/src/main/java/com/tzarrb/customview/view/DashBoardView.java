package com.tzarrb.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tzarrb.customview.R;

public class DashBoardView extends View {

	private float mRingBias = 0.15f;
	private float mSectionRatio = 3.0f;
	private RectF mSectionRect = new RectF();
	protected float mSectionHeight;

	protected float mRadius;

	protected int mMaxProgress = 100;
	protected int mProgress = 0;

	protected float mCenterX;
	protected float mCenterY;

	private Paint mPaint;
	private int mAcitiveColor;
	private int mInactiveColor;

	public DashBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttributes(context, attrs);
		init();
	}

	public DashBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributes(context, attrs);
		init();
	}

	public DashBoardView(Context context)
	{
		super(context);
	}

	private void init(){
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeCap(Paint.Cap.ROUND);

	}
	
	private void initAttributes(Context context, AttributeSet attrs) {
		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DashBoardView, 0, 0);
		try {
			// Read and clamp max
			int max = attributes.getInteger(R.styleable.DashBoardView_dbMax, 100);
			mMaxProgress = Math.max(max, 1); 
			
			// Read and clamp progress
			int progress = attributes.getInteger(R.styleable.DashBoardView_dbProgress, 0);
			mProgress = Math.max(Math.min(progress, mMaxProgress), 0);
			
			mAcitiveColor = attributes.getColor(R.styleable.DashBoardView_dbactiveColor, Color.parseColor("#3BB7F7"));
			mInactiveColor = attributes.getColor(R.styleable.DashBoardView_dbInactiveColor, Color.parseColor("#E5EAF0"));
			
			mRingBias = attributes.getFloat(R.styleable.DashBoardView_dbRingBias, 0.15f);
			mSectionRatio = attributes.getFloat(R.styleable.DashBoardView_dbSectionRatio, 8.0f);
		}
		finally {
			attributes.recycle();
		}
	}
	
	private void updateDimensions(int width, int height)
	{
		// Update center position
		mCenterX = width / 2.0f;
		mCenterY = height / 2.0f;
		
		// Find shortest dimension
		int diameter = Math.min(width, height);
		
		float outerRadius = diameter / 2;
		float sectionHeight = outerRadius * mRingBias;
		float sectionWidth = sectionHeight / mSectionRatio;
		
		mRadius = outerRadius - sectionHeight / 2;
		mSectionRect.set(-sectionWidth / 2, -sectionHeight / 2, sectionWidth / 2, sectionHeight / 2);
		mSectionHeight = sectionHeight;
	}
	
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		
		if (width > height)
			super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		else
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		
		updateDimensions(getWidth(), getHeight());
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		updateDimensions(w, h);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// Center our canvas
		canvas.translate(mCenterX, mCenterY);
		
		float rotation = 270.0f / (float) mMaxProgress;
		for (int i = 0; i < mMaxProgress; ++i)
		{
			canvas.save();
			
			canvas.rotate((float) i * rotation + 225);
			canvas.translate(0, -mRadius);
			
			if (i < mProgress) {
				float bias = (float) i / (float) (mMaxProgress - 1);
				mPaint.setColor(mAcitiveColor);
			}
			else {
				//canvas.scale(0.7f, 0.7f);
				mPaint.setColor(mInactiveColor);
			}
			
			canvas.drawRect(mSectionRect, mPaint);
			canvas.restore();
		}
		
		super.onDraw(canvas);
	}

	/**
	 * Get max progress
	 * 
	 * @return Max progress
	 */
	public float getMax() {
		return mMaxProgress;
	}
	
	/**
	 * Set max progress
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		int newMax = Math.max(max, 1);
		if (newMax != mMaxProgress)
			mMaxProgress = newMax;
		
		updateProgress(mProgress);
		invalidate();
	}
	
	/**
	 * Get Progress
	 * 
	 * @return progress
	 */
	public int getProgress() {
		return mProgress;
	}
	
	/**
	 * Set progress
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		updateProgress(progress);
	}
	
	/**
	 * Get ratio
	 * 
	 * @return progress ratio compared to max progress 
	 */
	public float getRatio() {
		return (float)mProgress / (float)mMaxProgress;
	}
	
	/**
	 * Update progress internally. Clamp it to a valid range and invalidate the view if necessary 
	 * 
	 * @param progress
	 * @return true if progress was changed and the view needs an update
	 */
	protected boolean updateProgress(int progress) {
		// Clamp progress
		progress = Math.max(0, Math.min(mMaxProgress, progress));
		
		if (progress != mProgress) {
			mProgress = progress;
			invalidate();
			
			return true;
		}
		
		return false;
	}
}
