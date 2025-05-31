package ru.mirea.kovalikaa.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.time.LocalTime;

public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView lightLevelTextView;
    private TextView lightTipsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        lightLevelTextView = view.findViewById(R.id.lightLevelTextView);
        lightTipsTextView = view.findViewById(R.id.lightTipsTextView);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            lightLevelTextView.setText(R.string.no_light_sensor);
            lightTipsTextView.setText("");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightLevel = event.values[0];
            lightLevelTextView.setText(getString(R.string.light_level_value, lightLevel));
            updateLightTips(lightLevel);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            lightTipsTextView.setText(R.string.sensor_unreliable);
        }
    }

    private void updateLightTips(float lightLevel) {
        StringBuilder tips = new StringBuilder();
        tips.append("Текущий уровень освещённости: ").append((int) lightLevel).append(" люкс\n\n");
        tips.append("Рекомендации по освещению:\n");

        if (lightLevel < 50) {
            tips.append("- Очень низкий уровень света: не подходит для чтения или работы.\n");
            tips.append("- Включите яркий свет или переместитесь в более освещённое место.\n");
        } else if (lightLevel >= 50 && lightLevel < 200) {
            tips.append("- Низкий уровень света: может вызвать напряжение глаз.\n");
            tips.append("- Рекомендуется включить дополнительное освещение (например, настольную лампу).\n");
        } else if (lightLevel >= 200 && lightLevel <= 500) {
            tips.append("- Оптимальный уровень света для чтения и работы.\n");
            tips.append("- Условия комфортны, продолжайте в том же духе!\n");
        } else if (lightLevel > 500 && lightLevel <= 1000) {
            tips.append("- Высокий уровень света: может быть слишком ярко.\n");
            tips.append("- Если чувствуете дискомфорт, используйте шторы или уменьшите яркость искусственного света.\n");
        } else {
            tips.append("- Очень высокий уровень света: может вызывать блики и утомление глаз.\n");
            tips.append("- Закройте шторы, выключите лишний свет или используйте солнцезащитные очки.\n");
        }

        lightTipsTextView.setText(tips.toString());
    }

    public static SensorFragment newInstance() {
        return new SensorFragment();
    }
}