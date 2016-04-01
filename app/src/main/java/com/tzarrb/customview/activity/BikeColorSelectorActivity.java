package com.tzarrb.customview.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tzarrb.customview.R;
import com.tzarrb.customview.adapter.BikeClassifyAdapter;
import com.tzarrb.customview.view.BikeColorSelector;

import java.util.ArrayList;
import java.util.List;

public class BikeColorSelectorActivity extends AppCompatActivity {

    private Context context;

    BikeColorSelector bikeColorSelector;

    List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_color_selector);
        this.context = this;
        init();
    }

    private void init(){
        dataList = getData();
        BikeClassifyAdapter adapter = new BikeClassifyAdapter(context, dataList);
        bikeColorSelector = (BikeColorSelector)findViewById(R.id.bike_color_selector);
        bikeColorSelector.setAdapter(adapter);
        bikeColorSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String color = dataList.get(position);
                Toast.makeText(context, color,Toast.LENGTH_SHORT);
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

}
