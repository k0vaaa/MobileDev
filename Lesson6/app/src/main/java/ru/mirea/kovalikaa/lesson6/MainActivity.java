package ru.mirea.kovalikaa.lesson6;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.kovalikaa.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        var group = binding.editTextText;
        var number = binding.editTextText2;
        var serial = binding.editTextText3;
        SharedPreferences preferences	=
                getSharedPreferences("mirea_settings",	Context.MODE_PRIVATE);
        group.setText(preferences.getString("GROUP", "unknown"));
        number.setText(String.valueOf(preferences.getInt("NUMBER", 0)));
        serial.setText(preferences.getString("SERIAL", "unknown"));
        binding.saveBtn.setOnClickListener(v->{
            SharedPreferences.Editor	editor	=	preferences.edit();

            editor.putString("GROUP", group.getText().toString());
            editor.putInt("NUMBER", Integer.parseInt(number.getText().toString()));
            editor.putString("SERIAL", serial.getText().toString());
            editor.apply();
        });
    }
}