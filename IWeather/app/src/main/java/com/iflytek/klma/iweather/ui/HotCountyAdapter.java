package com.iflytek.klma.iweather.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iflytek.klma.iweather.R;
import com.iflytek.klma.iweather.db.County;

import java.util.List;

/**
 * Created by makunle on 2017/7/30.
 */

public class HotCountyAdapter extends RecyclerView.Adapter<HotCountyAdapter.MViewHolder> {

    private static final String TAG = "IWeather";

    static class MViewHolder extends RecyclerView.ViewHolder{

        Button btnCountyName;

        public MViewHolder(View itemView) {
            super(itemView);
            btnCountyName = (Button) itemView.findViewById(R.id.county_name_item);
        }
    }

    private List<County> counties;

    public HotCountyAdapter(List<County> counties){
        this.counties = counties;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_county_item, parent, false);
        final MViewHolder viewHolder = new MViewHolder(view);

        viewHolder.btnCountyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Log.d(TAG, "onClick: "+counties.get(position).getName());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        County county = counties.get(position);
        holder.btnCountyName.setText(county.getName());
    }

    @Override
    public int getItemCount() {
        return counties.size();
    }


}
