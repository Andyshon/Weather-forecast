package com.andyshon.weather_forecast.widget;

/**
 * Created by andyshon on 01.08.18.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.data.remote.RestClient;
import com.andyshon.weather_forecast.ui.activity.MainActivity;
import com.andyshon.weather_forecast.utils.WeatherUtils;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.andyshon.weather_forecast.GlobalConstants.ApiConstants.*;

public class MyWidget extends AppWidgetProvider {

    private static final String _ACTION_GOTOMAINAPP = "ACTION_GOTOMAINAPP";
    private static final String _ACTION_REFRESHTEMP = "ACTION_REFRESHTEMP";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        refreshData(views, appWidgetManager, new ComponentName(context, MyWidget.class));


        // Construct an Intent which is pointing this class.

        Intent goToAppIntent = new Intent(context, MyWidget.class);
        goToAppIntent.setAction(_ACTION_GOTOMAINAPP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, goToAppIntent, 0);
        views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);

        Intent refreshTempIntent = new Intent(context, MyWidget.class);
        refreshTempIntent.setAction(_ACTION_REFRESHTEMP);
        pendingIntent = PendingIntent.getBroadcast(context, 0, refreshTempIntent, 0);
        views.setOnClickPendingIntent(R.id.btnRefreshWidget, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (_ACTION_REFRESHTEMP.equals(intent.getAction())) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            // This time we dont have widgetId. Reaching our widget with that way.
            ComponentName appWidget = new ComponentName(context, MyWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);


            refreshData(views, appWidgetManager, appWidget);


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidget, views);
        }
        else if (_ACTION_GOTOMAINAPP.equals(intent.getAction())) {
            Intent mainApp = new Intent(context, MainActivity.class);
            mainApp.putExtra("key", "widget");
            context.startActivity(mainApp);
        }
    }


    public static void refreshData(RemoteViews views, AppWidgetManager appWidgetManager, ComponentName appWidget) {
        views.setViewVisibility(R.id.progressBar, View.VISIBLE);
        views.setViewVisibility(R.id.btnRefreshWidget, View.INVISIBLE);
        if (GlobalConstants.CURRENT_CITY_EN != null) {
            RestClient.getService().getTodayByCityName(GlobalConstants.CURRENT_CITY_EN, UNITS, KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weatherToday -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        views.setTextViewText(R.id.tvTempCur, String.valueOf(weatherToday.getTemp().getTemp().intValue()).concat("˚"));
                        views.setImageViewResource(R.id.ivWeatherState, WeatherUtils.getIconByWeatherState(weatherToday.getDescription().get(0).getDescription(), true));
                        views.setTextViewText(R.id.tvCityName, weatherToday.getCity());
                        views.setViewVisibility(R.id.progressBar, View.INVISIBLE);
                        views.setViewVisibility(R.id.btnRefreshWidget, View.VISIBLE);
                        appWidgetManager.updateAppWidget(appWidget, views);
                    },throwable -> Log.d("Widget", "Error while refreshing widget"));
        }
    }


}
