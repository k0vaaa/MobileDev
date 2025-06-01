package ru.mirea.kovalikaa.mireaproject;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.kovalikaa.mireaproject.databinding.FragmentPhotonoteBinding;

public class PhotoNoteFragment extends Fragment {

    private FragmentPhotonoteBinding uiBinding;
    private boolean hasCameraAccess = false;
    private Uri capturedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        uiBinding = FragmentPhotonoteBinding.inflate(inflater, container, false);
        return uiBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPermissions();
        setupCamera();
        setupSaveButton();
    }

    private void setupPermissions() {
        String[] requiredPermissions = {Manifest.permission.CAMERA};
        if (arePermissionsGranted(requiredPermissions)) {
            hasCameraAccess = true;
        } else {
            ActivityResultLauncher<String[]> permissionRequest = registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    permissions -> {
                        hasCameraAccess = permissions.getOrDefault(Manifest.permission.CAMERA, false);
                        if (!hasCameraAccess) {
                            showToast("Требуется разрешение на камеру");
                        }
                    }
            );
            permissionRequest.launch(requiredPermissions);
        }
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void setupCamera() {
        ActivityResultLauncher<Uri> photoLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                isSuccess -> {
                    if (isSuccess && capturedImageUri != null) {
                        uiBinding.imageView.setImageURI(capturedImageUri);
                    } else {
                        showToast("Не удалось захватить изображение");
                    }
                }
        );

        uiBinding.buttonAddImage.setOnClickListener(v -> {
            if (hasCameraAccess) {
                capturedImageUri = generateImageUri();
                if (capturedImageUri != null) {
                    photoLauncher.launch(capturedImageUri);
                } else {
                    showToast("Ошибка создания файла изображения");
                }
            } else {
                showToast("Нет доступа к камере");
            }
        });
    }

    private void setupSaveButton() {
        uiBinding.buttonSave.setOnClickListener(v -> {
            String noteText = uiBinding.editTextContent.getText().toString().trim();
            if (capturedImageUri == null) {
                showToast("Сначала сделайте фото");
                return;
            }
            if (noteText.isEmpty()) {
                showToast("Введите текст заметки");
                return;
            }
            applyTextAndSave(capturedImageUri, noteText);
            uiBinding.textViewFullNotice.setText("Заметка: " + noteText);
        });
    }

    private Uri generateImageUri() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "NOTE_" + timestamp + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        return requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void applyTextAndSave(Uri imageUri, String noteText) {
        try {
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            Bitmap editableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(editableBitmap);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.YELLOW);
            textPaint.setTextSize(60f);
            textPaint.setAntiAlias(true);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextAlign(Paint.Align.CENTER);

            float xPos = editableBitmap.getWidth() / 2f;
            float yPos = editableBitmap.getHeight() - 80f;
            canvas.drawText(noteText, xPos, yPos, textPaint);

            ContentValues values = new ContentValues();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "annotated_note_" + timestamp + ".png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri saveUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (saveUri != null) {
                try (OutputStream output = requireContext().getContentResolver().openOutputStream(saveUri)) {
                    editableBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.flush();
                }
                showToast("Заметка сохранена в галерею");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Ошибка сохранения заметки");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiBinding = null;
    }
}