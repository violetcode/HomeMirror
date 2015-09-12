package com.morristaedt.mirror.modules;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.morristaedt.mirror.R;
import com.morristaedt.mirror.requests.ForecastRequest;
import com.morristaedt.mirror.requests.ForecastResponse;
import com.morristaedt.mirror.utils.WeekUtil;

import java.util.Calendar;
import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class ForecastModule {

    public interface ForecastListener {
        void onWeatherToday(String weatherToday);
    }

    public static void getHourlyForecast(final Resources resources, final double lat, final double lon, final String units, final ForecastListener listener) {
        new AsyncTask<Void, Void, ForecastResponse>() {

            @Override
            protected ForecastResponse doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("https://api.forecast.io")
                        .setErrorHandler(new ErrorHandler() {
                            @Override
                            public Throwable handleError(RetrofitError cause) {
                                Log.w("mirror", "Forecast error: " + cause);
                                return null;
                            }
                        })
                        .build();

                ForecastRequest service = restAdapter.create(ForecastRequest.class);
                String excludes = "minutely,daily,flags";
                Log.d("mirror", "backgrounddd");
                Log.d("mirror", "Latitude : " + lat);
                Log.d("mirror", "Longitude : "+ lon);
                return service.getHourlyForecast(resources.getString(R.string.dark_sky_api_key), lat, lon, excludes, units);
            }

            @Override
            protected void onPostExecute(ForecastResponse forecastResponse) {
                if (forecastResponse != null) {
                    if (forecastResponse.currently != null) {
                        listener.onWeatherToday(forecastResponse.currently.getDisplayTemperature() + " " + forecastResponse.currently.summary);
                    }
                }
            }
        }.execute();

    }
}
