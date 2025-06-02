package ru.mirea.kovalikaa.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText nameEditText;
    private EditText ageEditText;
    private EditText bookEditText;
    private EditText filmEditText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.nameText);
        ageEditText = view.findViewById(R.id.ageText);
        bookEditText = view.findViewById(R.id.bookText);
        filmEditText = view.findViewById(R.id.filmText);
        saveButton = view.findViewById(R.id.saveBtn);

        loadSavedData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return view;
    }

    private void loadSavedData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        nameEditText.setText(sharedPreferences.getString("name", ""));
        ageEditText.setText(sharedPreferences.getString("age", ""));
        bookEditText.setText(sharedPreferences.getString("book", ""));
        filmEditText.setText(sharedPreferences.getString("film", ""));
    }

    private void saveData() {
        String name = nameEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String book = bookEditText.getText().toString().trim();
        String film = filmEditText.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || book.isEmpty() || film.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("age", age);
        editor.putString("book", book);
        editor.putString("film", film);
        editor.apply();

        Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
    }
}