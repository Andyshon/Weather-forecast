package com.andyshon.weather_forecast.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.data.entity.weather_today_hour_forecast.WeatherTodayHourForecast_list;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by andyshon on 28.07.18.
 */

public class WeatherAdapterHorizontal extends RecyclerView.Adapter<WeatherAdapterHorizontal.WeatherViewHolder> {

    private List<WeatherTodayHourForecast_list> mWeatherList;

    public WeatherAdapterHorizontal() {}


    public void setWeatherList(final List<WeatherTodayHourForecast_list> weatherList) {
        mWeatherList = weatherList;
        notifyDataSetChanged();
    }


    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forecast_horizontal, parent, false);

        return new WeatherViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherTodayHourForecast_list currentDay = mWeatherList.get(position);

        String hourOfDay = (new SimpleDateFormat("k").format(new Date(currentDay.getTimestamp() * 1000)));

        int n = Integer.parseInt(hourOfDay);
        String p1="0", p2="00";
        if (n<10) {
            hourOfDay = p1.concat(hourOfDay).concat(":").concat(p2);
        }
        else {
            hourOfDay = hourOfDay.concat(":").concat(p2);
        }
        holder.tvDate.setText(hourOfDay);

        String temp = splitByDot(currentDay.getTemp()).concat("˚");
        holder.tvTemp.setText(temp);

        int image = WeatherUtils.getIconByWeatherState(currentDay.getDescription().getDescription(), true);

        holder.ivImage.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return mWeatherList == null ? 0 : mWeatherList.size();
    }


    private String splitByDot(String string) {
        String[] splitMas = string.split("\\.");
        // since i need first element -> no need to check splitMas length
        return splitMas[0];
    }


    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDate, tvTemp;
        final ImageView ivImage;

        WeatherViewHolder(View view) {
            super(view);
            this.tvDate = view.findViewById(R.id.tvTime);
            this.tvTemp = view.findViewById(R.id.tvTemp);
            this.ivImage = view.findViewById(R.id.ivWeatherState);
        }
    }
}

