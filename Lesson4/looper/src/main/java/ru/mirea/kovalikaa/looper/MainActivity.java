package ru.mirea.kovalikaa.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.kovalikaa.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

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

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d("MainActivity", "Результат: " + result);
            }
        };

        MyLooper myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.button.setOnClickListener(v -> {
            String age = binding.editTextAge.getText().toString();
            String status = binding.editTextStatus.getText().toString();

            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("age", age);
            bundle.putString("status", status);
            msg.setData(bundle);
            myLooper.mHandler.sendMessage(msg);
        });
    }
}