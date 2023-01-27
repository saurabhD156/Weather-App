package com.devgitesh.weatherApp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=b481d4e4ca07807f3a75884d14fdd305&units=metric")
    Call<OpenWeatherMap> getWeatherWithLocation(@Query("lat") double lat, @Query("lon") double lon);

    @GET("weather?appid=b481d4e4ca07807f3a75884d14fdd305&units=metric")
    Call<OpenWeatherMap> getWeatherWithCityName(@Query("q") String name);

}
