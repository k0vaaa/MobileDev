package ru.mirea.kovalikaa.lesson4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.kovalikaa.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding	= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        binding.textViewMirea.setText("Мой	номер	по	списку	№14");
//        binding.buttonMirea.setOnClickListener(new	View.OnClickListener()	{
//            @Override
//            public	void	onClick(View	v)	{
//                Log.d(MainActivity.class.getSimpleName(),"onClickListener");
//            }
//        });
        String tag = MainActivity.class.getSimpleName();
        binding.songTitle.setText("Feel Good Inc.");
        binding.atristTitle.setText("Gorillaz");

        binding.btnPause.setOnClickListener(v -> {
            if (binding.btnPause.getText() == "Pause"){
                binding.btnPause.setText("Play");
                Log.d(tag, "Paused");
            }
            else {
                binding.btnPause.setText("Pause");
                Log.d(tag, "Play");
            }
        });

        binding.btnPrev.setOnClickListener(v -> {
            binding.songTitle.setText("Previous Song");
            binding.atristTitle.setText("Unnamed Artist 1");
            Log.d(tag, "Previous song is playing");
        });

        binding.btnNext.setOnClickListener(v -> {
            binding.songTitle.setText("Next Song");
            binding.atristTitle.setText("Unnamed Artist 2");
            Log.d(tag, "Next song is playing");
        });
    }
}

