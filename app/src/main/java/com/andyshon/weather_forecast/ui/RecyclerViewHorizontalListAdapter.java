package com.andyshon.weather_forecast.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.entity.WeatherDay;

import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.MyViewHolder>{
    private List<WeatherDay> weatherDayList;
    Context context;

    public RecyclerViewHorizontalListAdapter(List<WeatherDay> weatherDayList, Context context){
        this.weatherDayList= weatherDayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_horiz, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.imageView.setImageResource(R.drawable.ic_white_day_shower);
        holder.tvTime.setText("21:00");
        String strTemp = weatherDayList.get(position).getTempInteger().concat("˚");
        holder.tvTemp.setText(strTemp);
    }

    @Override
    public int getItemCount() {
        return weatherDayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTime, tvTemp;
        MyViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.ivWeatherState);
            tvTime=view.findViewById(R.id.tvTime);
            tvTemp=view.findViewById(R.id.tvTemp);
        }
    }
}