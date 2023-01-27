package com.devgitesh.weatherApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView city, textWeatherCondition, temperature, humidity, minTemp, maxTemp, pressure, wind;
    private ImageView imageView;

    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.textCityName);
        textWeatherCondition = findViewById(R.id.textViewWeatherCondition);
        temperature = findViewById(R.id.textViewtemp);
        humidity = findViewById(R.id.textHumidity);
        minTemp = findViewById(R.id.textMinTemp);
        maxTemp = findViewById(R.id.textMaxTemp);
        pressure = findViewById(R.id.textPressure);
        wind = findViewById(R.id.textWind);
        imageView = findViewById(R.id.imageView);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

            Intent i = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(i);

        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {
            lat = location.getLatitude();
            lon = location.getLongitude();

            Log.e("lat: ", String.valueOf(lat));
            Log.e("lon: ", String.valueOf(lon));

            getWeatherData(lat, lon);
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    public void getWeatherData(double lat, double lon) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(lat, lon);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                city.setText(response.body().getName() + " " + response.body().getSys().getCountry());
                temperature.setText(response.body().getMain().getTemp() + " °C");
                textWeatherCondition.setText(response.body().getWeather().get(0).getDescription());
                humidity.setText(" : " + response.body().getMain().getHumidity() + " %");
                maxTemp.setText(" : " + response.body().getMain().getTempMax() + "  °C");
                minTemp.setText(" : " + response.body().getMain().getTempMin() + "  °C");
                pressure.setText(" : " + response.body().getMain().getPressure());
                wind.setText(" : " + response.body().getWind().getSpeed());

                String iconCode = response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });

    }
}