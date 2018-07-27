package com.andyshon.weather_forecast.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.entity.WeatherDay;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

class WeatherAdapter extends ArrayAdapter<WeatherDay> {
    private LayoutInflater inflater;
    private int layout;
    private List<WeatherDay> weatherDayList;
    private List<WeatherDay> allDays;

    WeatherAdapter(Context context, int resource, List<WeatherDay> weatherDayList, List<WeatherDay> allDays) {
        super(context, resource, weatherDayList);
        this.weatherDayList = weatherDayList;
        this.allDays = allDays;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final WeatherDay currentWeatherDay = weatherDayList.get(position);

        SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("E");
        String dayOfWeek = formatDayOfWeek.format(currentWeatherDay.getDate().getTime());
        viewHolder.nameView.setText(dayOfWeek);


        String strTemp = WeatherUtils.getMaxOrMinTemp(position+1, allDays, 1).concat("˚").concat("/")
                .concat(WeatherUtils.getMaxOrMinTemp(position+1, allDays, 2)).concat("˚");

        viewHolder.countView.setText(strTemp);

        viewHolder.ivWeatherState.setImageResource(R.drawable.ic_black_day_shower);

        viewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "item:" + currentWeatherDay.getDescription().getDescription(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        final LinearLayout layoutItem;
        final ImageView ivWeatherState;
        final TextView nameView, countView;
        ViewHolder(View view){
            layoutItem = (LinearLayout) view.findViewById(R.id.layoutItem);
            ivWeatherState = (ImageView) view.findViewById(R.id.ivWeatherState);
            nameView = (TextView) view.findViewById(R.id.nameView);
            countView = (TextView) view.findViewById(R.id.countView);
        }
    }
}