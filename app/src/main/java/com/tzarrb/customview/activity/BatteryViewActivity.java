package com.tzarrb.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.tzarrb.customview.R;
import com.tzarrb.customview.view.BatteryView;

public class BatteryViewActivity extends AppCompatActivity implements BatteryView.OnBatteryChangedListener {

    BatteryView batteryView;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_view);
        batteryView = (BatteryView)(findViewById(R.id.battery_view));
        batteryView.setOnBatteryChangedListener(this);

        seekBar = (SeekBar)this.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
    }

    public void onBatteryChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress){
        if (progress > 50 && progress <= 100 ){
            batteryView.setProgressDrawable(R.drawable.battery_view_progress_green);
        }else if(progress > 15 && progress <= 50){
            batteryView.setProgressDrawable(R.drawable.battery_view_progress_yellow);
        }else{
            batteryView.setProgressDrawable(R.drawable.battery_view_progress_red);
        }
    }


    private class OnSeekBarChangeListenerImp implements
            SeekBar.OnSeekBarChangeListener {

        // 触发操作，拖动
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            batteryView.setProgress(progress);
        }

        // 表示进度条刚开始拖动，开始拖动时候触发的操作
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 停止拖动时候
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
    }
}
