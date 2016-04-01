package com.tzarrb.customview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tzarrb.customview.activity.BatteryViewActivity;
import com.tzarrb.customview.activity.BikeClassifyActivity;
import com.tzarrb.customview.activity.BikeColorSelectorActivity;
import com.tzarrb.customview.activity.CircleProgressViewActivity;
import com.tzarrb.customview.activity.DashBoardViewActivity;
import com.tzarrb.customview.activity.TrackEndButtonActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        init();
    }

    public void init(){
        TextView btnBatteryView = (TextView)findViewById(R.id.btn_battery_view);
        btnBatteryView.setOnClickListener(this);
        TextView btnCircleProgressView = (TextView)findViewById(R.id.btn_circle_progress_view);
        btnCircleProgressView.setOnClickListener(this);
        TextView btnTrackBtn = (TextView)findViewById(R.id.btn_track_end);
        btnTrackBtn.setOnClickListener(this);
        TextView btnDashBoardView = (TextView)findViewById(R.id.btn_dashboard_view);
        btnDashBoardView.setOnClickListener(this);
        TextView bntBikeClassify = (TextView)findViewById(R.id.btn_bike_classify);
        bntBikeClassify.setOnClickListener(this);
        TextView bntBikeColorSelector = (TextView)findViewById(R.id.btn_bike_color_selector);
        bntBikeColorSelector.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btn_battery_view:
                intent = new Intent(context, BatteryViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_circle_progress_view:
                intent = new Intent(context, CircleProgressViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_track_end:
                intent = new Intent(context, TrackEndButtonActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_dashboard_view:
                intent = new Intent(context, DashBoardViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bike_classify:
                intent = new Intent(context, BikeClassifyActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bike_color_selector:
                intent = new Intent(context, BikeColorSelectorActivity.class);
                startActivity(intent);
                break;
        }
    }
}
