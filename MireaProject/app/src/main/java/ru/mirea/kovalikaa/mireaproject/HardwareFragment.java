package ru.mirea.kovalikaa.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


public class HardwareFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hardware, container, false);

        NavController navController = NavHostFragment.findNavController(this);

        view.findViewById(R.id.sensorBtn).setOnClickListener(v -> {
            navController.navigate(R.id.nav_sensor);
        });

        view.findViewById(R.id.cameraBtn).setOnClickListener(v -> {
            navController.navigate(R.id.nav_camera);
        });

        view.findViewById(R.id.audioBtn).setOnClickListener(v -> {
            navController.navigate(R.id.nav_audio);
        });

        return view;
    }
}