package com.tzarrb.customview.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tzarrb.customview.R;

/**
 * Created by dev on 16/3/29.
 */
public class BikeColorSelector extends LinearLayout implements View.OnClickListener {

    protected final static int DEFAULT_HEIGHT = 40;
    protected final static int DEFAULT_PADDING = 5;


    private Context context;

    private CardView bikeColorContainer;
    private LinearLayout bikeColorTopContainer;
    private CardView bikeColorSelectorImg;
    private TextView bikeColorSelectorTitle;
    private ImageView bikeColorSelectorBtn;
    private LinearLayout bikeColorSelectorContainer;
    private ListView bikeColorSelectorListView;

    private int height;
    private int padding;
    private int radius;
    private int selectedColor;
    private int hideIconSrc;
    private int showIconSrc;
    private String title;

    private boolean colorSelectorShow;
    private AdapterView.OnItemClickListener onItemClickListener;

    public BikeColorSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (isInEditMode()) {
            previewLayout(context);
        } else {
            setup(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BikeColorSelector(Context context, AttributeSet attrs, int defStyleAttr) {
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
        LayoutInflater.from(context).inflate(R.layout.view_bike_color_selector, this);
        // Initial default view
        bikeColorContainer = (CardView) findViewById(R.id.bike_color_container);
        bikeColorTopContainer = (LinearLayout) findViewById(R.id.bike_color_top_container);
        bikeColorSelectorImg = (CardView) findViewById(R.id.bike_color_selector_img);
        bikeColorSelectorTitle = (TextView) findViewById(R.id.bike_color_selector_title);
        bikeColorSelectorBtn = (ImageView) findViewById(R.id.bike_color_selector_btn);
        bikeColorSelectorContainer = (LinearLayout) findViewById(R.id.bike_color_selector_container);
        bikeColorSelectorListView = (ListView) findViewById(R.id.bike_color_selector_list);

        bikeColorContainer.setRadius(radius);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        bikeColorTopContainer.setLayoutParams(params);
        bikeColorTopContainer.setPadding(padding, padding, padding, padding);
        bikeColorSelectorImg.setCardBackgroundColor(selectedColor);
        bikeColorSelectorTitle.setText(title);
        bikeColorSelectorBtn.setImageResource(hideIconSrc);
        bikeColorSelectorListView.setPadding(padding, padding, padding, padding);

        bikeColorSelectorBtn.setOnClickListener(this);
    }

    // Retrieve initial parameter from view attribute
    public void setupStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BikeColorSelector);

        height =  (int) typedArray.getDimension(R.styleable.BikeColorSelector_csHeight, dp2px(DEFAULT_HEIGHT));
        radius = height/2;
        radius = (int) typedArray.getDimension(R.styleable.BikeColorSelector_csRadius, radius);
        padding = (int) typedArray.getDimension(R.styleable.BikeColorSelector_csPadding, dp2px(DEFAULT_PADDING));

        title = (String)typedArray.getString(R.styleable.BikeColorSelector_csTitle);
        int colorSelectedDefault = context.getResources().getColor(R.color.white);
        selectedColor = typedArray.getColor(R.styleable.BikeColorSelector_csSelectedColor, colorSelectedDefault);

        hideIconSrc = typedArray.getResourceId(R.styleable.BikeColorSelector_csHideIconSrc, R.mipmap.ic_triangle_down);
        showIconSrc = typedArray.getResourceId(R.styleable.BikeColorSelector_csShowIconSrc, R.mipmap.ic_triangle_up);

        typedArray.recycle();
    }

    @Override
    public void onClick(View v){
        if (colorSelectorShow){
            colorSelectorShow = false;
            bikeColorSelectorBtn.setImageResource(hideIconSrc);
            bikeColorSelectorContainer.setVisibility(GONE);
        }else{
            colorSelectorShow = true;
            bikeColorSelectorBtn.setImageResource(showIconSrc);
            bikeColorSelectorContainer.setVisibility(VISIBLE);
        }
    }

    public void setAdapter(BaseAdapter adapter){
        bikeColorSelectorListView.setAdapter(adapter);
        if (onItemClickListener != null){
            bikeColorSelectorListView.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
        if (onItemClickListener != null){
            bikeColorSelectorListView.setOnItemClickListener(onItemClickListener);
        }
    }

    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
