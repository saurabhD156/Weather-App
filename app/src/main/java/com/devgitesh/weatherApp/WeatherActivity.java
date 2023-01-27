package com.devgitesh.weatherApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView weather_city, weather_Condition, weather_temperature, weather_humidity, weather_minTemp,
            weather_maxTemp, weather_pressure, weather_wind;
    private EditText editTextCityName;
    private ImageView weather_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weather_city = findViewById(R.id.weather_CityName);
        editTextCityName = findViewById(R.id.editTextCityName);
        weather_Condition = findViewById(R.id.weather_Condition);
        weather_temperature = findViewById(R.id.weather_temp);
        weather_humidity = findViewById(R.id.weather_Humidity);
        weather_maxTemp = findViewById(R.id.weather_MaxTemp);
        weather_minTemp = findViewById(R.id.weather_MinTemp);
        weather_pressure = findViewById(R.id.weather_Pressure);
        weather_wind = findViewById(R.id.weather_Wind);
        weather_imageView = findViewById(R.id.weather_imageView);


        Button weather_Search = findViewById(R.id.search);
        weather_Search.setOnClickListener(v -> {

            String cityName = editTextCityName.getText().toString();
            getWeatherData(cityName);

            editTextCityName.setText("");
        });

    }

    public void getWeatherData(String name) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if (response.isSuccessful()) {
                    weather_city.setText(response.body().getName() + " " + response.body().getSys().getCountry());
                    weather_temperature.setText(response.body().getMain().getTemp() + " °C");
                    weather_Condition.setText(response.body().getWeather().get(0).getDescription());
                    weather_humidity.setText(" : " + response.body().getMain().getHumidity() + " %");
                    weather_maxTemp.setText(" : " + response.body().getMain().getTempMax() + "  °C");
                    weather_minTemp.setText(" : " + response.body().getMain().getTempMin() + "  °C");
                    weather_pressure.setText(" : " + response.body().getMain().getPressure());
                    weather_wind.setText(" : " + response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(weather_imageView);
                } else {
                    Toast.makeText(WeatherActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });

    }
}