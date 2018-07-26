package com.andyshon.weather_forecast;

/**
 * Created by andyshon on 26.07.18.
 */

public class GlobalConstants {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String KEY = "ad27ce4738673725d73b862bbf80aabf";


    public static String CURRENT_CITY_EN;
    public static String CURRENT_CITY_UA;


    /*
    * Convert city name from ukrainian to english
    * */
    public static void setCurrentCity (String city) {
        CURRENT_CITY_UA = city;
        switch (city) {
            case "Запоріжжя":
                CURRENT_CITY_EN = cities[1];
                break;
                // next cities down here
                default: CURRENT_CITY_EN = cities[0];
        }
    }


    public static String[] cities = {
            "Lviv", "Zaporizhzhya", "Chernivtsi", "Nikopol", "Uzhhorod", "Kiev", "Vinnytsya", "Berdyansk", "Dnipropetrovsk", "Donetsk", "Zhytomyr", "Ivano-Frankivsk",
            "Kirovohrad", "Luhansk", "Lutsk", "Mykolaiv", "Odesa", "Poltava", "Rivne", "Sevastopol", "Simferopol", "Sumy", "Ternopil", "Uzhhorod", "Khmelnytskyi",
            "Kharkiv", "Kherson", "Cherkasy", "Chernihiv"
    };
}
