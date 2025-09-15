package com.ascentya.AsgriV2.my_farm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.model.WeatherForecast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WeatherForecastFragment extends Fragment {

   private WeatherForecast weatherForecast;

   private ImageView image, windDirIv;
   private TextView dateTv, summaryTv, tempHighTv, tempLowTv, humidityTv,
           windTv, windDirTv, rainTv, rainLowTv, rainHighTv;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      if (getArguments() != null){
         weatherForecast = GsonUtils.fromJson(
                 getArguments().getString("weather_forecast"), WeatherForecast.class);
      }
   }

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_weather_forecast, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      image = view.findViewById(R.id.image);

      dateTv = view.findViewById(R.id.date);
      summaryTv = view.findViewById(R.id.summary);
      tempHighTv = view.findViewById(R.id.tempHigh);
      tempLowTv = view.findViewById(R.id.tempLow);
      humidityTv = view.findViewById(R.id.humidity);
      windTv = view.findViewById(R.id.wind);
      windDirTv = view.findViewById(R.id.windDir);
      rainLowTv = view.findViewById(R.id.rainLow);
      rainHighTv = view.findViewById(R.id.rainHigh);

      windDirIv = view.findViewById(R.id.windDirIcon);

      try {
         updateUi();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private String formatBearing(double bearing) {
      if (bearing < 0 && bearing > -180) {
         // Normalize to [0,360]
         bearing = 360.0 + bearing;
      }
      if (bearing > 360 || bearing < -180) {
         return "Unknown";
      }

      String directions[] = {
              "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
              "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW",
              "N"};
      String cardinal = directions[(int) Math.floor(((bearing + 11.25) % 360) / 22.5)];
      return cardinal + " (" + formatBearing(bearing) + " deg)";
   }

   public void updateUi() throws Exception{

      String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
      String windDir = weatherForecast.getWindBearing() + Constants.DEGREE + " " + directions[ (int)Math.round((  ((double) Float.parseFloat(weatherForecast.getWindBearing()) % 360) / 45)) % 8 ];

      //String windDir = formatBearing(Double.parseDouble(weatherForecast.getWindBearing()));

      String tempHigh = String.format("%.1f ", Float.parseFloat(weatherForecast.getTemperatureHigh())) + Constants.TEMPERATURE_SYMBOL;
      String tempLow = String.format("%.1f ", Float.parseFloat(weatherForecast.getTemperatureLow())) + Constants.TEMPERATURE_SYMBOL;
      String humidity = String.format("%d", (int) (Float.parseFloat(weatherForecast.getHumidity()) * 100f)) + "%";
      String wind = String.format("%.1f miles/hr", Float.parseFloat(weatherForecast.getWindSpeed()));
      //String windDir =  + Constants.DEGREE;
      String rainLow = String.format("%.3f mm/hr", Float.parseFloat(weatherForecast.getPrecipIntensity()));
      String rainHigh = String.format("%.3f mm/hr", Float.parseFloat(weatherForecast.getPrecipIntensityMax()));

      dateTv.setText(DateUtils.displayDayAndMonth(weatherForecast.getDate()));
      summaryTv.setText(weatherForecast.getSummary());

      //windDirIv.setRotation(Float.parseFloat(weatherForecast.getWindBearing()));

      if(weatherForecast.getIcon().contains("day")){
         image.setImageResource(R.drawable.ic_weather_sun);
      }else if (weatherForecast.getIcon().contains("rain")){
         image.setImageResource(R.drawable.ic_weather_rain);
      }else {
         image.setImageResource(R.drawable.ic_weather_cloud);
      }

      tempHighTv.setText(tempLow + " - " + tempHigh);
      //tempLowTv.setText(tempLow);
      humidityTv.setText(humidity);
      windTv.setText(wind);
      windDirTv.setText(windDir);
      //rainLowTv.setText(rainLow);
      rainHighTv.setText(rainLow + " - " + rainHigh);
   }

   public static WeatherForecastFragment getInstance(WeatherForecast weatherForecast){
      WeatherForecastFragment fragment = new WeatherForecastFragment();
      Bundle args = new Bundle();
      args.putString("weather_forecast", GsonUtils.toJson(weatherForecast));
      fragment.setArguments(args);
      return fragment;
   }
}
