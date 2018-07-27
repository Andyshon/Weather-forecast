package com.andyshon.weather_forecast.utils;

import com.andyshon.weather_forecast.db.entity.WeatherDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andyshon on 27.07.18.
 */

public class WeatherUtils {


    public static String getMaxOrMinTemp (int numberOfTheDay, List<WeatherDay> weatherDays, int max1_min2) {

        List<WeatherDay> day = new ArrayList<>();
        int startInd=1, count=8;
        if (numberOfTheDay == 2) {
            startInd=9;
        }
        else if (numberOfTheDay == 3) {
            startInd=17;
        }
        else if (numberOfTheDay == 4) {
            startInd=25;
        }
        if (numberOfTheDay == 5) {
            startInd=33;
            count=7;
        }
        int[] arr = new int[count];
        int tempMaxOrMin;

        for (int i=startInd,k=0; i<(startInd+count); i++,k++) {
            day.add(weatherDays.get(i));
            arr[k]= Integer.parseInt(weatherDays.get(i).getTempInteger());
        }
        Arrays.sort(arr);
        if(max1_min2==1) {
            tempMaxOrMin = arr[arr.length - 1];
        }
        else {
            tempMaxOrMin = arr[0];
        }

        return String.valueOf(tempMaxOrMin);
    }


}
