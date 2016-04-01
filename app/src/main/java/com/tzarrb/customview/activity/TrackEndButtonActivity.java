package com.tzarrb.customview.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tzarrb.customview.R;
import com.tzarrb.customview.view.TrackEndButton;

public class TrackEndButtonActivity extends AppCompatActivity implements TrackEndButton.OnTrackButtonEndListener {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_end_button);
        this.context = this;
        TrackEndButton trackEndButton = (TrackEndButton)findViewById(R.id.track_end_btn);
        trackEndButton.setOnTrackButtonEndListener(this);
    }

    public void OnTrackButtonEnd(int viewId){
        Toast.makeText(context, "骑行结束", Toast.LENGTH_SHORT);
    }
}
