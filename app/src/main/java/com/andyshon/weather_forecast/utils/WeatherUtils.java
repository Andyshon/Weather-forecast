package com.andyshon.weather_forecast.utils;

import android.util.Log;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andyshon on 27.07.18.
 */

public class WeatherUtils {

    public static String getDateTitle(long date) {
        String dayOfWeekRU = (new SimpleDateFormat("EE").format(new Date(date * 1000))).toUpperCase();
        String dayOfWeekRU2 = (new SimpleDateFormat("d").format(new Date(date * 1000))).toUpperCase();
        String dayOfWeekRU3 = (new SimpleDateFormat("MMMM").format(new Date(date * 1000)));

        return dayOfWeekRU.concat(", ").concat(dayOfWeekRU2).concat(" ").concat(dayOfWeekRU3);
    }

    /*
    * Convert city from RU to EN
    * */
    public static String fromRUtoEN (String city) {
        if (city != null) {
            for (int i = 0; i < GlobalConstants.getCitiesRuList().size(); i++) {
                if (city.equals(GlobalConstants.getCitiesRuList().get(i))) {
                    System.out.println("CITY TO FIND:" + GlobalConstants.getCitiesEnList().get(i));
                    return GlobalConstants.getCitiesEnList().get(i);
                }
            }
        }
        return GlobalConstants.getCitiesEnList().get(0);
    }

    public static int getIconByWeatherState(String weatherState, boolean isDayNow) {

        int image=0;

        switch (weatherState) {
            case "clear sky":
            case "sky is clear":
                if (isDayNow)
                    image = R.drawable.ic_white_day_bright;
                else image = R.drawable.ic_black_day_bright;
                break;
            case "scattered clouds":
            case "overcast clouds":
            case "broken clouds":
            case "few clouds":
                if (isDayNow)
                    image = R.drawable.ic_white_day_cloudy;
                else image = R.drawable.ic_black_day_cloudy;
                break;
            case "moderate rain":
            case "light rain":
                if (isDayNow)
                    image = R.drawable.ic_white_day_rain;
                else image = R.drawable.ic_black_day_rain;
                break;
            case "light intensity shower rain":
            case "heavy intensity shower rain":
            case "heavy intensity rain":
            case "ragged shower rain":
            case "very heavy rain":
            case "freezing rain":
            case "extreme rain":
            case "shower rain":
                if (isDayNow)
                    image = R.drawable.ic_white_day_shower;
                else image = R.drawable.ic_black_day_shower;
                break;
            case "thunderstorm with light rain":
            case "thunderstorm with rain":
            case "thunderstorm with heavy rain":
            case "light thunderstorm":
            case "thunderstorm":
            case "heavy thunderstorm":
            case "ragged thunderstorm":
            case "thunderstorm with light drizzle":
            case "thunderstorm with heavy drizzle":
            case "thunderstorm with drizzle":
                if (isDayNow)
                    image = R.drawable.ic_white_day_thunder;
                else image = R.drawable.ic_black_day_thunder;
                break;
                default:
                    Log.d("WeatherUtils", "Unknown weather state, SOS!!!" + weatherState);
                    if (isDayNow)
                        image = R.drawable.ic_white_day_bright;
                    else image = R.drawable.ic_black_day_bright;
        }

        return image;
    }


    public static int getIconByWindState(String windState) {

        int image = 0;
        int degree = Integer.parseInt(windState);
        System.out.println("degree:" + degree);

        if (degree==360){
            image = R.drawable.icon_wind_n;
        }
        else if (degree>0 && degree<90) {
            image = R.drawable.icon_wind_ne;
        }
        else if (degree>90 && degree<180) {
            image = R.drawable.icon_wind_se;
        }
        else if (degree==180) {
            image = R.drawable.icon_wind_s;
        }
        else if (degree>180 && degree<270) {
            image = R.drawable.icon_wind_ws;
        }
        else if (degree==270) {
            image = R.drawable.icon_wind_w;
        }
        else if (degree>270 && degree<360) {
            image = R.drawable.icon_wind_wn;
        }

        return image;
    }
}
