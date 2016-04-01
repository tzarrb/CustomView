package com.tzarrb.customview.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tzarrb.customview.R;
import com.tzarrb.customview.adapter.BikeClassifyAdapter;

import java.util.ArrayList;
import java.util.List;

public class BikeClassifyActivity extends AppCompatActivity {

    Context context;

    CardView cvBikeClassify;
    Spinner spBikeClassify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_classify);
        context = this;

        cvBikeClassify = (CardView)findViewById(R.id.bike_classify_card);
        spBikeClassify = (Spinner)findViewById(R.id.bike_classify_spinner);
        init();
    }

    private boolean isFirstIn = true;

    public void init(){
        spBikeClassify.setDropDownVerticalOffset((int) dp2px(30));
        spBikeClassify.setDropDownWidth((int) dp2px(300));
        spBikeClassify.setPopupBackgroundResource(R.drawable.bike_classify_spinner_background);
        spBikeClassify.setGravity(Gravity.END);
        BikeClassifyAdapter adapter = new BikeClassifyAdapter(context, getData());
        spBikeClassify.setAdapter(adapter);
        spBikeClassify.setSelection(0,true);
        spBikeClassify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstIn){
                    isFirstIn = false;
                    return;
                }else{
                    String itemName = (String) parent.getItemAtPosition(position);
                    Toast.makeText(context, itemName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private List<String> getData() {
        // 数据源
        List<String> dataList = new ArrayList<String>();
        dataList.add("奔驰白");
        dataList.add("金属银");
        dataList.add("太空灰");
        dataList.add("热情红");
        dataList.add("薄荷绿");
        return dataList;
    }


    @SuppressLint("NewApi")
    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
