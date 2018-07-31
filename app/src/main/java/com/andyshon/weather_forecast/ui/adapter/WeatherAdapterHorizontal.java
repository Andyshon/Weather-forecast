package com.andyshon.weather_forecast.ui.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.entity.WeatherDayHourForecast;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by andyshon on 28.07.18.
 */

public class WeatherAdapterHorizontal extends RecyclerView.Adapter<WeatherAdapterHorizontal.BookViewHolder> {

    private List<WeatherDayHourForecast> mWeatherList;
    private Context context;


    public WeatherAdapterHorizontal(Context context) {
        this.context = context;
    }

    public void setProductList(final List<WeatherDayHourForecast> weatherList) {

        if (mWeatherList == null) {
            mWeatherList = weatherList;
            notifyItemRangeInserted(0, weatherList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mWeatherList.size();
                }

                @Override
                public int getNewListSize() {
                    return weatherList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mWeatherList.get(oldItemPosition) == weatherList.get(newItemPosition);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    WeatherDayHourForecast newWeather = weatherList.get(newItemPosition);
                    WeatherDayHourForecast oldWeather = mWeatherList.get(oldItemPosition);
                    return Objects.equals(newWeather, oldWeather)
                            && Objects.equals(newWeather.getCity(), oldWeather.getCity())
                            && Objects.equals(newWeather.getDt_txt(), oldWeather.getDt_txt())
                            && Objects.equals(newWeather.getPressure(), oldWeather.getPressure())
                            && Objects.equals(newWeather.getHumidity(), oldWeather.getHumidity());
                }
            });
            mWeatherList = weatherList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_horiz, parent, false);

        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        WeatherDayHourForecast currentDay = mWeatherList.get(position);

        String hourOfDay = (new SimpleDateFormat("k").format(new Date(currentDay.getTimestamp() * 1000)));
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

    static class BookViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDate, tvTemp;
        final ImageView ivImage;

        public BookViewHolder(View view) {
            super(view);
            this.tvDate = view.findViewById(R.id.tvTime);
            this.tvTemp = view.findViewById(R.id.tvTemp);
            this.ivImage = view.findViewById(R.id.ivWeatherState);
        }
    }
}

