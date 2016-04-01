package com.tzarrb.customview.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzarrb.customview.R;

/**
 * Created by dev on 16/3/21.
 */
public class BatteryView extends LinearLayout {
    protected final static int DEFAULT_MAX_PROGRESS = 100;
    protected final static int DEFAULT_PROGRESS = 0;
    protected final static int DEFAULT_PROGRESS_RADIUS = 30;
    protected final static int DEFAULT_CONTAINER_PADDING = 0;
    protected final static int DEFAULT_BACKGROUND_PADDING = 0;
    protected final static int DEFAULT_CONTAINER_STROKE_WIDTH = 1;

    private Context context;

    private LinearLayout layoutContainer;
    private LinearLayout layoutBackground;
    private LinearLayout layoutProgress;

    private int radius;
    private int containerPadding;
    private int backgroundPadding;
    private int totalWidth;
    private int containerStrokeWidth;

    private float max;
    private float progress;

    private int drawableBackground;
    private int drawableProgress;

    private Bitmap bitmapBackground;
    private Bitmap bitmapProgress;

    private int colorContainerStroke;
    private int colorBackground;
    private int colorProgress;

    private boolean isReverse;

    private OnBatteryChangedListener batteryChangedListener;

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attrs);
        }
    }

    private void previewLayout(Context context) {
        setGravity(Gravity.CENTER);
        TextView tv = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(getClass().getSimpleName());
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.GRAY);
        addView(tv);
    }

    public void setup(Context context, AttributeSet attrs) {
        setupStyleable(context, attrs);

        removeAllViews();
        // Setup layout for sub class
        LayoutInflater.from(context).inflate(R.layout.view_battery, this);
        // Initial default view
        layoutContainer = (LinearLayout) findViewById(R.id.layout_container);
        layoutBackground = (LinearLayout) findViewById(R.id.layout_background);
        layoutProgress = (LinearLayout) findViewById(R.id.layout_progress);
    }

    // Retrieve initial parameter from view attribute
    public void setupStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BatteryView);

        radius = (int) typedArray.getDimension(R.styleable.BatteryView_bvRadius, dp2px(DEFAULT_PROGRESS_RADIUS));
        containerPadding = (int) typedArray.getDimension(R.styleable.BatteryView_bvContainerPadding, dp2px(DEFAULT_BACKGROUND_PADDING));
        backgroundPadding = (int) typedArray.getDimension(R.styleable.BatteryView_bvBackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));
        containerStrokeWidth = (int) typedArray.getDimension(R.styleable.BatteryView_bvContainerStrokeWidth, dp2px(DEFAULT_CONTAINER_STROKE_WIDTH));

        isReverse = typedArray.getBoolean(R.styleable.BatteryView_bvReverse, false);

        max = typedArray.getFloat(R.styleable.BatteryView_bvMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.BatteryView_bvProgress, DEFAULT_PROGRESS);


        int colorContainerStrokeDefault = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        colorContainerStroke = typedArray.getColor(R.styleable.BatteryView_bvContainerStrokeColor, colorContainerStrokeDefault);

        drawableBackground = typedArray.getResourceId(R.styleable.BatteryView_bvBackgroundDrawable, R.drawable.battery_view_background);
        drawableProgress = typedArray.getResourceId(R.styleable.BatteryView_bvProgressDrawable, R.drawable.battery_view_progress_green);

        typedArray.recycle();
    }

    // Progress bar always refresh when view size has changed
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        if(!isInEditMode()) {
            totalWidth = newWidth;
            drawAll();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawPrimaryProgress();
                }
            }, 5);
        }
    }

    // Redraw all view
    protected void drawAll() {
        drawContainer();
        drawBackgroundProgress();
        drawPadding();
        //drawProgressReverse();
        drawPrimaryProgress();
    }

    private void drawContainer() {
        GradientDrawable containerDrawable = new GradientDrawable();
        containerDrawable.setShape(GradientDrawable.RECTANGLE);
        containerDrawable.setColor(Color.parseColor("#00ffffff"));
        containerDrawable.setStroke(containerStrokeWidth, colorContainerStroke);
        containerDrawable.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutContainer.setBackground(containerDrawable);
        } else {
            layoutContainer.setBackgroundDrawable(containerDrawable);
        }
    }

    // Draw progress background
    @SuppressWarnings("deprecation")
    private void drawBackgroundProgress() {
        GradientDrawable backgroundDrawable = (GradientDrawable)context.getResources().getDrawable(drawableBackground);
        int newRadius = radius - (containerPadding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutBackground.setBackground(backgroundDrawable);
        } else {
            layoutBackground.setBackgroundDrawable(backgroundDrawable);
        }
    }

    private void drawPrimaryProgress() {
        GradientDrawable progressDrawable = (GradientDrawable)context.getResources().getDrawable(drawableProgress);
        int newRadius = radius - (containerPadding / 2) - (backgroundPadding / 2);
        progressDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(progressDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(progressDrawable);
        }

        //layoutProgress.setBackgroundResource(drawableProgress);

        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - (containerPadding * 2) - (backgroundPadding * 2)) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
    }

    // Create an empty color rectangle gradient drawable
    protected GradientDrawable createGradientDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    private void drawProgressReverse() {
        setupReverse(layoutProgress);
    }

    // Change progress position by depending on isReverse flag
    private void setupReverse(LinearLayout layoutProgress) {
        RelativeLayout.LayoutParams progressParams = (RelativeLayout.LayoutParams) layoutProgress.getLayoutParams();
        removeLayoutParamsRule(progressParams);
        if (isReverse) {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            // For support with RTL on API 17 or more
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        } else {
            progressParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            // For support with RTL on API 17 or more
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                progressParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        layoutProgress.setLayoutParams(progressParams);
    }

    private void drawPadding() {
        layoutContainer.setPadding(containerPadding, containerPadding, containerPadding, containerPadding);
        layoutBackground.setPadding(backgroundPadding, backgroundPadding, backgroundPadding, backgroundPadding);
    }

    // Remove all of relative align rule
    private void removeLayoutParamsRule(RelativeLayout.LayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        }
    }

    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    protected Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    protected Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
        drawProgressReverse();
        drawPrimaryProgress();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius >= 0)
            this.radius = radius;
        drawBackgroundProgress();
        drawPrimaryProgress();
    }

    public int getPadding() {
        return backgroundPadding;
    }

    public void setPadding(int padding) {
        if (padding >= 0)
            this.backgroundPadding = padding;
        drawPadding();
        drawPrimaryProgress();
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max >= 0)
            this.max = max;
        if (this.progress > max)
            this.progress = max;
        drawPrimaryProgress();
    }

    public float getLayoutWidth() {
        return totalWidth;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        if (progress < 0)
            this.progress = 0;
        else if (progress > max)
            this.progress = max;
        else
            this.progress = progress;
        drawPrimaryProgress();
        if(batteryChangedListener != null)
            batteryChangedListener.onBatteryChanged(getId(), this.progress, true, false);
    }

    public int getProgressBackgroundDrawable() {
        return drawableBackground;
    }

    public void setProgressBackgroundDrawable(int drawableProgress) {
        this.drawableBackground = drawableProgress;
        drawBackgroundProgress();
    }

    public int getProgressDrawable() {
        return drawableProgress;
    }

    public void setProgressDrawable(int drawableProgress) {
        this.drawableProgress = drawableProgress;
        drawPrimaryProgress();
    }

    public void setOnBatteryChangedListener(OnBatteryChangedListener listener) {
        batteryChangedListener = listener;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        drawAll();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.radius = this.radius;
        ss.containerStrokeWidth = ss.containerStrokeWidth;
        ss.colorContainerStroke = ss.colorContainerStroke;

        ss.containerPadding = this.containerPadding;
        ss.backgroundPadding = this.backgroundPadding;

        ss.drawableBackground = this.drawableBackground;
        ss.drawableProgress = this.drawableProgress;

        ss.max = this.max;
        ss.progress = this.progress;

        ss.isReverse = this.isReverse;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.radius = ss.radius;
        this.containerStrokeWidth = ss.containerStrokeWidth;
        this.colorContainerStroke = ss.colorContainerStroke;

        this.containerPadding = ss.containerPadding;
        this.backgroundPadding = ss.backgroundPadding;

        this.drawableBackground = ss.drawableBackground;
        this.drawableProgress = ss.drawableProgress;

        this.max = ss.max;
        this.progress = ss.progress;

        this.isReverse = ss.isReverse;
    }

    private static class SavedState extends BaseSavedState {
        float max;
        float progress;

        int radius;

        int containerStrokeWidth;
        int colorContainerStroke;

        int containerPadding;
        int backgroundPadding;

        int drawableBackground;
        int drawableProgress;

        boolean isReverse;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.progress = in.readFloat();

            this.radius = in.readInt();

            this.containerStrokeWidth = in.readInt();
            this.colorContainerStroke = in.readInt();

            this.containerPadding = in.readInt();
            this.backgroundPadding = in.readInt();

            this.drawableBackground = in.readInt();
            this.drawableProgress = in.readInt();

            this.isReverse = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.max);
            out.writeFloat(this.progress);

            out.writeInt(this.radius);
            out.writeInt(this.containerStrokeWidth);
            out.writeInt(this.colorContainerStroke);

            out.writeInt(this.containerPadding);
            out.writeInt(this.backgroundPadding);

            out.writeInt(this.drawableBackground);
            out.writeInt(this.drawableProgress);

            out.writeByte((byte) (this.isReverse ? 1 : 0));
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnBatteryChangedListener {
        public void onBatteryChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress);
    }
}
