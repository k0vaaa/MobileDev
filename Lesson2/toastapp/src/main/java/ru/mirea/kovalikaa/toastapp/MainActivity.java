package ru.mirea.kovalikaa.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void AddEventOnClick(View view){
        EditText editText = findViewById(R.id.editTextToCount);
        Integer num = editText.getText().length();
        Toast toast = Toast.makeText(getApplicationContext(),
                "СТУДЕНТ №14 ГРУППА БСБО-09-22\n" +
                        "Количество символов - " + num,

                Toast.LENGTH_SHORT);
        toast.show();
    }
}