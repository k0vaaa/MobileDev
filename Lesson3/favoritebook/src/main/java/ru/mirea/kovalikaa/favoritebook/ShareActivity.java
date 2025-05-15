package ru.mirea.kovalikaa.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShareActivity extends AppCompatActivity {
    private EditText book;
    private EditText quote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        book = findViewById(R.id.editTextText);
        quote = findViewById(R.id.editTextText2);
    }

    public void SendData(View view) {
        Intent data = new Intent();
        data.putExtra(MainActivity.QUOTES_KEY, quote.getText().toString());
        data.putExtra(MainActivity.BOOK_NAME_KEY, book.getText().toString());
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}