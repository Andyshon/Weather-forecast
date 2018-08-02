package com.andyshon.weather_forecast;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andyshon on 26.07.18.
 */

public class GlobalConstants {

    public static class ApiConstants {
        public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
        public static final String KEY = "ad27ce4738673725d73b862bbf80aabf";
        public static final String UNITS = "metric";
    }

    public static String CURRENT_CITY_EN;
    public static String CURRENT_CITY_RU;

    public static String CURRENT_LOCATION_CITY_RU;
    public static String CURRENT_LOCATION_CITY_EN;

    public static boolean IsLocationDetected = false;


    /*
    * Updating and converting location/city name to appropriate language
    * */

    public static void updateCurrentLocationCity (String city) {
        CURRENT_LOCATION_CITY_RU = city;
        fromRuToENLocation(city);
    }

    public static void updateCurrentCity(String city, int toRu_1_toEn_2) {
        if (toRu_1_toEn_2 == 2) { // from RU to EN
            fromRuToEN(city);
        }
        else {} // from EN to RU - no usage needed
    }

    private static void fromRuToENLocation(String city) {
        boolean isCityFound = false;
        for (int i=0; i<citiesRU.length; i++) {
            if (city.equals(citiesRU[i])) {
                isCityFound = true;
                CURRENT_CITY_EN = citiesEN[i];
                CURRENT_LOCATION_CITY_EN = citiesEN[i];
            }
        }
        if (!isCityFound) {
            CURRENT_CITY_EN = citiesEN[0];  // Zaporizhzhya
            CURRENT_LOCATION_CITY_EN = citiesEN[0];
        }
    }

    private static void fromRuToEN (String city) {
        CURRENT_CITY_RU = city;

        System.out.println("fromRuToEN = " + city);
        System.out.println("last location = " + CURRENT_LOCATION_CITY_EN);

        boolean isCityFound = false;
        for (int i=0; i<citiesRU.length; i++) {
            if (city.equals(citiesRU[i])) {
                isCityFound = true;
                CURRENT_CITY_EN = citiesEN[i];
            }
        }
        if (!isCityFound) {
            CURRENT_CITY_EN = CURRENT_LOCATION_CITY_EN;  // last found location
        }
    }

    public static void setCurrentCityRU(String city) {
        CURRENT_CITY_RU = city;
    }

    public static void setCurrentCityEN(String city) {
        CURRENT_CITY_EN = city;
    }


    private static String[] citiesEN = {
            "Zaporizhzhya", "Lviv", "Chernivtsi", "Nikopol", "Uzhhorod", "Kiev", "Vinnytsya", "Berdyansk", "Dnipropetrovsk", "Donetsk", "Zhytomyr", "Ivano-Frankivsk",
            "Kirovohrad", "Luhansk", "Lutsk", "Mykolaiv", "Odesa", "Poltava", "Rivne", "Sevastopol", "Simferopol", "Sumy", "Ternopil", "Khmelnytskyi",
            "Kharkiv", "Kherson", "Cherkasy", "Chernihiv", "Abkhazia", "Afghanistan", "Tirana", "Argentina", "Armenia", "Yerevan", "Australia", "Baku", "Vienna",
            "Bangladesh", "Azerbaijan", "Minsk", "Belarus"
    };

    private static String[] citiesRU = {
            "Запорожье", "Львов", "Черновцы", "Никополь", "Ужгород", "Киев", "Винница", "Бердянск", "Днепр", "Донецк", "Житомир", "Ивано-Франковск",
            "Кировоград", "Луганск", "Луцк", "Николаев", "Одесса", "Полтава", "Ровно", "Севастополь", "Симферополь", "Суммы", "Тернополь", "Хмельницк",
            "Харков", "Херсон", "Черкасы", "Чернигов", "Абхазия", "Афганистан", "Тирана", "Аргентина", "Армения", "Эреван", "Австралия", "Баку", "Венна", "Бангладеш",
            "Азербайджан", "Минск", "Беларусь"
    };

    public static List<String> getCitiesRuList() {
        return Arrays.asList(citiesRU);
    }

    public static List<String> getCitiesEnList() {
        return Arrays.asList(citiesEN);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static class Preferences {

        private static final String SAVED_LOCATION_EN = "SPL_en";
        private static final String SAVED_LOCATION_RU = "SPL_ru";

        public static void saveLastUserLocation(Context context) {
            GlobalConstants.IsLocationDetected = true;
            SharedPreferences sPref = ((Activity)context).getSharedPreferences("MyWeatherPref", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SAVED_LOCATION_EN, GlobalConstants.CURRENT_LOCATION_CITY_EN);
            ed.putString(SAVED_LOCATION_RU, GlobalConstants.CURRENT_LOCATION_CITY_RU);
            ed.apply();
        }

        public static void loadLastUserLocation(Context context) {
            SharedPreferences sPref = ((Activity)context).getSharedPreferences("MyWeatherPref", MODE_PRIVATE);
            String savedLocation_en = sPref.getString(SAVED_LOCATION_EN, "");
            String savedLocation_ru = sPref.getString(SAVED_LOCATION_RU, "");
            if (!savedLocation_en.isEmpty() && !savedLocation_ru.isEmpty()) {
                GlobalConstants.IsLocationDetected = true;
            }
            GlobalConstants.CURRENT_LOCATION_CITY_EN = savedLocation_en;
            GlobalConstants.CURRENT_LOCATION_CITY_RU = savedLocation_ru;
            GlobalConstants.CURRENT_CITY_EN = savedLocation_en;
            GlobalConstants.CURRENT_CITY_RU = savedLocation_ru;
        }

    }
}
