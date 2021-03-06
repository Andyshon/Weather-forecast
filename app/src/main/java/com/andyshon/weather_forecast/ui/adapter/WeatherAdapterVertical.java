package com.andyshon.weather_forecast.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast_list;
import com.andyshon.weather_forecast.ui.WeatherClickCallback;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by andyshon on 28.07.18.
 */

public class WeatherAdapterVertical extends RecyclerView.Adapter<WeatherAdapterVertical.WeatherViewHolder> {

    private List<WeatherTodayForecast_list> allDaysForecast;
    private int row_index = 0;

    @Nullable
    private final WeatherClickCallback mWeatherClickCallback;


    public WeatherAdapterVertical(@Nullable WeatherClickCallback clickCallback) {
        mWeatherClickCallback = clickCallback;
    }


    public void setWeatherList(final List<WeatherTodayForecast_list> allDaysForecastList) {

        row_index = 0;  // hover first row

        allDaysForecast = allDaysForecastList;
        notifyDataSetChanged();
    }


    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forecast_vertical, parent, false);

        return new WeatherViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherTodayForecast_list currentDay = allDaysForecast.get(position);

        String temp = currentDay.getTempMax().concat("˚/").concat(currentDay.getTempMin().concat("˚"));
        holder.tvTemp.setText(temp);
        String dayOfWeekRU = (new SimpleDateFormat("EE").format(new Date(currentDay.getDt() * 1000))).toUpperCase();
        holder.tvDate.setText(dayOfWeekRU);

        int image = WeatherUtils.getIconByWeatherState(currentDay.getDescription().description, false);

        holder.ivImage.setImageResource(image);

        holder.layoutHorizontal.setOnClickListener(view -> {
            row_index=position;
            notifyDataSetChanged();

            mWeatherClickCallback.onClick(currentDay, row_index == 0);
        });

        if(row_index==position){
            holder.layoutHorizontal.setBackgroundResource(R.color.colorVerticalAdapterHover);
        }
        else {
            holder.layoutHorizontal.setBackgroundColor(Color.WHITE);
        }
    }


    @Override
    public int getItemCount() {
        return allDaysForecast == null ? 0 : allDaysForecast.size();
    }


    static class WeatherViewHolder extends RecyclerView.ViewHolder {

        final LinearLayout layoutHorizontal;
        final TextView tvDate, tvTemp;
        final ImageView ivImage;

        WeatherViewHolder(View view) {
            super(view);
            this.layoutHorizontal = view.findViewById(R.id.layoutItem);
            this.tvDate = view.findViewById(R.id.nameView);
            this.tvTemp = view.findViewById(R.id.countView);
            this.ivImage = view.findViewById(R.id.ivWeatherState);
        }
    }
}

