package com.andyshon.weather_forecast;

import java.util.Arrays;
import java.util.List;

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
    public static String CURRENT_CITY_UA;

    public static boolean IsLocationDetected = false;


    /*
    * Convert city name from ukrainian to english
    * */
    public static void setCurrentCity (String city) {
        CURRENT_CITY_UA = city;

        if (city.equals(citiesRU[0])) {
            CURRENT_CITY_EN = citiesEN[0];
        }
        else if (city.equals(citiesRU[1])) {
            CURRENT_CITY_EN = citiesEN[1];
        }
        else if (city.equals(citiesRU[2])) {
            CURRENT_CITY_EN = citiesEN[2];
        }
        else if (city.equals(citiesRU[3])) {
            CURRENT_CITY_EN = citiesEN[3];
        }
        else if (city.equals(citiesRU[4])) {
            CURRENT_CITY_EN = citiesEN[4];
        }
        else if (city.equals(citiesRU[5])) {
            CURRENT_CITY_EN = citiesEN[5];
        }
        else if (city.equals(citiesRU[6])) {
            CURRENT_CITY_EN = citiesEN[6];
        }
        else if (city.equals(citiesRU[7])) {
            CURRENT_CITY_EN = citiesEN[7];
        }
        else if (city.equals(citiesRU[8])) {
            CURRENT_CITY_EN = citiesEN[8];
        }
        else if (city.equals(citiesRU[9])) {
            CURRENT_CITY_EN = citiesEN[9];
        }
        else if (city.equals(citiesRU[10])) {
            CURRENT_CITY_EN = citiesEN[10];
        }
        else if (city.equals(citiesRU[11])) {
            CURRENT_CITY_EN = citiesEN[11];
        }
        else if (city.equals(citiesRU[12])) {
            CURRENT_CITY_EN = citiesEN[12];
        }
        else if (city.equals(citiesRU[13])) {
            CURRENT_CITY_EN = citiesEN[13];
        }
        else if (city.equals(citiesRU[14])) {
            CURRENT_CITY_EN = citiesEN[14];
        }
        else if (city.equals(citiesRU[15])) {
            CURRENT_CITY_EN = citiesEN[15];
        }
        else if (city.equals(citiesRU[16])) {
            CURRENT_CITY_EN = citiesEN[16];
        }
        else if (city.equals(citiesRU[17])) {
            CURRENT_CITY_EN = citiesEN[17];
        }
        else if (city.equals(citiesRU[18])) {
            CURRENT_CITY_EN = citiesEN[18];
        }
        else if (city.equals(citiesRU[19])) {
            CURRENT_CITY_EN = citiesEN[19];
        }
        else if (city.equals(citiesRU[20])) {
            CURRENT_CITY_EN = citiesEN[20];
        }
        else if (city.equals(citiesRU[21])) {
            CURRENT_CITY_EN = citiesEN[21];
        }
        else if (city.equals(citiesRU[22])) {
            CURRENT_CITY_EN = citiesEN[22];
        }
        else if (city.equals(citiesRU[23])) {
            CURRENT_CITY_EN = citiesEN[23];
        }
        else if (city.equals(citiesRU[24])) {
            CURRENT_CITY_EN = citiesEN[24];
        }
        else if (city.equals(citiesRU[25])) {
            CURRENT_CITY_EN = citiesEN[25];
        }
        else if (city.equals(citiesRU[26])) {
            CURRENT_CITY_EN = citiesEN[26];
        }
        else if (city.equals(citiesRU[27])) {
            CURRENT_CITY_EN = citiesEN[27];
        }
        else CURRENT_CITY_EN = citiesEN[0];
    }

    private static String[] citiesEN = {
            "Zaporizhzhya", "Lviv", "Chernivtsi", "Nikopol", "Uzhhorod", "Kiev", "Vinnytsya", "Berdyansk", "Dnipropetrovsk", "Donetsk", "Zhytomyr", "Ivano-Frankivsk",
            "Kirovohrad", "Luhansk", "Lutsk", "Mykolaiv", "Odesa", "Poltava", "Rivne", "Sevastopol", "Simferopol", "Sumy", "Ternopil", "Khmelnytskyi",
            "Kharkiv", "Kherson", "Cherkasy", "Chernihiv"
    };

    private static String[] citiesRU = {
            "Запорожье", "Львов", "Черновцы", "Никополь", "Ужгород", "Киев", "Винница", "Бердянск", "Днепр", "Донецк", "Житомир", "Ивано-Франковск",
            "Кировоград", "Луганск", "Луцк", "Николаев", "Одесса", "Полтава", "Ровно", "Севастополь", "Симферополь", "Суммы", "Тернополь", "Хмельницк",
            "Харков", "Херсон", "Черкасы", "Чернигов"
    };

    public static List<String> getCitiesRuList() {
        return Arrays.asList(citiesRU);
    }

    public static List<String> getCitiesEnList() {
        return Arrays.asList(citiesEN);
    }
}
