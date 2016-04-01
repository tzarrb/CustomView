package com.tzarrb.customview.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tzarrb.customview.R;

import java.util.List;

/**
 * Created by dev on 16/3/22.
 */
public class BikeClassifyAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;

    public BikeClassifyAdapter(Context pContext, List<String> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.adapter_bike_classify_spinner, null);
        if(convertView!=null) {
            String classifyName = mList.get(position);
            TextView tvBikeClassifyName=(TextView)convertView.findViewById(R.id.bike_classify_item_name);
            tvBikeClassifyName.setText(mList.get(position));
            CardView cvBikeClassifyColor = (CardView)convertView.findViewById(R.id.bike_classify_item_card);
            if (classifyName.equals("奔驰白")){
                cvBikeClassifyColor.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
            }else if (classifyName.equals("金属银")){
                cvBikeClassifyColor.setCardBackgroundColor(mContext.getResources().getColor(R.color.gray_light));
            }else if (classifyName.equals("金属银")){
                cvBikeClassifyColor.setCardBackgroundColor(mContext.getResources().getColor(R.color.gray));
            }else if (classifyName.equals("太空灰")){
                cvBikeClassifyColor.setCardBackgroundColor(mContext.getResources().getColor(R.color.gray));
            }else if (classifyName.equals("热情红")){
                cvBikeClassifyColor.setCardBackgroundColor(mContext.getResources().getColor(R.color.red));
            }else if (classifyName.equals("薄荷绿")){
                cvBikeClassifyColor.setCardBackgroundColor(mContext.getResources().getColor(R.color.green));
            }
        }
        return convertView;
    }
}
