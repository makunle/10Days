package com.iflytek.klma.iweather.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.County;
import com.iflytek.klma.iweather.util.DatabaseUtil;

import java.util.List;

/**
 * 热点城市RecyclerView使用
 */

public class HotCountyAdapter extends RecyclerView.Adapter<HotCountyAdapter.MViewHolder> {

    private Context context = null;

    private static final String TAG = "IWeather";

    private List<County> mCounties;

    static class MViewHolder extends RecyclerView.ViewHolder{

        Button btnCountyName;
        public MViewHolder(View itemView) {
            super(itemView);
            btnCountyName = (Button) itemView.findViewById(R.id.county_name_item);
        }

    }

    public HotCountyAdapter(List<County> counties){
        mCounties = counties;
    }

    @Override
    public MViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_county, parent, false);
        final MViewHolder viewHolder = new MViewHolder(view);

        /***
         * 点击热点城市标签时，尝试将此标签添加到BookMark中，如果成功则跳转到天气详情页面
         */
        viewHolder.btnCountyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                County county = mCounties.get(position);
                if(DatabaseUtil.getInstance().addWeatherBookMark(county.getName())){
                    WeatherShowActivity.startMe(context, county.getName());
                    ((Activity)context).finish();
                } else {
                    Toast.makeText(context, "城市名称错误，请尝试更新热点城市列表", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        County county = mCounties.get(position);
        holder.btnCountyName.setText(county.getName());
    }

    @Override
    public int getItemCount() {
        return mCounties.size();
    }


}
