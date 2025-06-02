package ru.mirea.kovalikaa.employeedb;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    private ListView listView;

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
        listView = findViewById(R.id.dbView);
        AppDatabase db = App.getInstance().getDatabase();
        if (db.employeeDao().getAll().isEmpty()) {
            db.employeeDao().insert(new Employee("Халк", "сила"));
            db.employeeDao().insert(new Employee("Дэдпул", "регенерация"));
            db.employeeDao().insert(new Employee("Супермен", "полет"));}
        List<Employee> heroes = db.employeeDao().getAll();
        List<String> heroDescriptions = new ArrayList<>();
        for (Employee h : heroes) {
            heroDescriptions.add(h.name + " — " + h.element);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                heroDescriptions
        );
        listView.setAdapter(adapter);
    }
}