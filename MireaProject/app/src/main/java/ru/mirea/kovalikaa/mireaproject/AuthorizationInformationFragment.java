package ru.mirea.kovalikaa.mireaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthorizationInformationFragment extends Fragment {

    private TextView emailTextView, cityTextView, regionTextView, temperatureTextView, windSpeedTextView;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization_information, container, false);

        emailTextView = view.findViewById(R.id.emailTextView);
        cityTextView = view.findViewById(R.id.cityTextView);
        regionTextView = view.findViewById(R.id.regionTextView);
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailTextView.setText("Email: " + currentUser.getEmail());
        }
        new DownloadWeatherTask().execute("https://ipinfo.io/json");

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
    private class DownloadWeatherTask extends AsyncTask<String, Void, String[]> {
        private String latitude, longitude;

        @Override
        protected String[] doInBackground(String... urls) {
            try {
                String ipData = downloadIpInfo(urls[0]);
                JSONObject ipJson = new JSONObject(ipData);
                latitude = ipJson.getString("loc").split(",")[0];
                longitude = ipJson.getString("loc").split(",")[1];
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                        "&longitude=" + longitude + "&current_weather=true";
                String weatherData = downloadIpInfo(weatherUrl);
                return new String[]{ipData, weatherData};
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return new String[]{"error", "error"};
            }
        }

        @Override
        protected void onPostExecute(String[] results) {
            try {
                if (!results[0].equals("error")) {
                    JSONObject ipJson = new JSONObject(results[0]);
                    cityTextView.setText("Город: " + (ipJson.has("city") ? ipJson.getString("city") : "Неизвестно"));
                    regionTextView.setText("Регион: " + (ipJson.has("region") ? ipJson.getString("region") : "Неизвестно"));
                    JSONObject weatherJson = new JSONObject(results[1]);
                    if (weatherJson.has("current_weather")) {
                        JSONObject currentWeather = weatherJson.getJSONObject("current_weather");
                        if (currentWeather.has("temperature")) {
                            double temperature = currentWeather.getDouble("temperature");
                            temperatureTextView.setText("Температура: " + String.format("%.1f", temperature) + " °C");
                        } else {
                            temperatureTextView.setText("Температура: Нет данных");
                        }
                        if (currentWeather.has("windspeed")) {
                            double windSpeed = currentWeather.getDouble("windspeed");
                            windSpeedTextView.setText("Скорость ветра: " + String.format("%.1f", windSpeed) + " км/ч");
                        } else {
                            windSpeedTextView.setText("Скорость ветра: Нет данных");
                        }
                    } else {
                        temperatureTextView.setText("Температура: Нет данных");
                        windSpeedTextView.setText("Скорость ветра: Нет данных");
                    }
                } else {
                    Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Ошибка обработки данных: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    bos.close();
                    data = new String(bos.toByteArray(), "UTF-8");
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }
}