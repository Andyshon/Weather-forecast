package com.andyshon.weather_forecast.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.db.entity.WeatherDay;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by andyshon on 26.07.18.
 */

class ProductAdapter extends ArrayAdapter<WeatherDay> {
    private LayoutInflater inflater;
    private int layout;
    private List<WeatherDay> productList;

    ProductAdapter(Context context, int resource, List<WeatherDay> products) {
        super(context, resource, products);
        this.productList = products;
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
        final WeatherDay product = productList.get(position);

        SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("E");
        String dayOfWeek = formatDayOfWeek.format(product.getDate().getTime());
        viewHolder.nameView.setText(dayOfWeek);

        String strTemp = product.getTempInteger().concat("˚/").concat(product.getTempInteger().concat("˚"));
        viewHolder.countView.setText(strTemp);

        viewHolder.ivWeatherState.setImageResource(R.drawable.ic_black_day_shower);

        return convertView;
    }

    private class ViewHolder {
        final ImageView ivWeatherState;
        final TextView nameView, countView;
        ViewHolder(View view){
            ivWeatherState = (ImageView) view.findViewById(R.id.ivWeatherState);
            nameView = (TextView) view.findViewById(R.id.nameView);
            countView = (TextView) view.findViewById(R.id.countView);
        }
    }
}