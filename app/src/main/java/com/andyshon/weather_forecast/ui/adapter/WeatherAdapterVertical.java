package com.andyshon.weather_forecast.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.entity.WeatherFiveDaysForecast;
import com.andyshon.weather_forecast.ui.WeatherClickCallback;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by andyshon on 28.07.18.
 */

public class WeatherAdapterVertical extends RecyclerView.Adapter<WeatherAdapterVertical.BookViewHolder> {

    private List<WeatherFiveDaysForecast> allDaysForecast;
    private int row_index = 0;

    @Nullable
    private final WeatherClickCallback mWeatherClickCallback;

    public WeatherAdapterVertical(@Nullable WeatherClickCallback clickCallback) {
        mWeatherClickCallback = clickCallback;
    }


    public void setProductList(final List<WeatherFiveDaysForecast> allDaysForecastList) {

        row_index = 0;  // hover first row

        if (allDaysForecast == null) {
            allDaysForecast = allDaysForecastList;
            notifyItemRangeInserted(0, allDaysForecastList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return allDaysForecast.size();
                }

                @Override
                public int getNewListSize() {
                    return allDaysForecastList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return allDaysForecast.get(oldItemPosition) == allDaysForecastList.get(newItemPosition);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    WeatherFiveDaysForecast newWeather = allDaysForecastList.get(newItemPosition);
                    WeatherFiveDaysForecast oldWeather = allDaysForecast.get(oldItemPosition);
                    return Objects.equals(newWeather, oldWeather)
                            && Objects.equals(newWeather.getSpeed(), oldWeather.getSpeed())
                            && Objects.equals(newWeather.getDt(), oldWeather.getDt())
                            && Objects.equals(newWeather.getPressure(), oldWeather.getPressure())
                            && Objects.equals(newWeather.getHumidity(), oldWeather.getHumidity());
                }
            });
            allDaysForecast = allDaysForecastList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new BookViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        WeatherFiveDaysForecast currentDay = allDaysForecast.get(position);

        String temp = currentDay.getTempMax().concat("˚/").concat(currentDay.getTempMin().concat("˚"));
        holder.tvTemp.setText(temp);
        String dayOfWeekRU = (new SimpleDateFormat("EE").format(new Date(currentDay.getDt() * 1000))).toUpperCase();
        holder.tvDate.setText(dayOfWeekRU);

        int image = WeatherUtils.getIconByWeatherState(currentDay.getDescription().description, false);

        holder.ivImage.setImageResource(image);

        holder.layoutHorizontal.setOnClickListener(view -> {
            row_index=position;
            notifyDataSetChanged();

            mWeatherClickCallback.onClick(currentDay);
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

    static class BookViewHolder extends RecyclerView.ViewHolder {

        final LinearLayout layoutHorizontal;
        final TextView tvDate, tvTemp;
        final ImageView ivImage;

        public BookViewHolder(View view) {
            super(view);
            this.layoutHorizontal = view.findViewById(R.id.layoutItem);
            this.tvDate = view.findViewById(R.id.nameView);
            this.tvTemp = view.findViewById(R.id.countView);
            this.ivImage = view.findViewById(R.id.ivWeatherState);
        }
    }
}

