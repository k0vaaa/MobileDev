package ru.mirea.kovalikaa.mireaproject;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraCollageFragment extends Fragment {
    private Uri imageUri;
    private LinearLayout collageLayout;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_collage, container, false);

        collageLayout = view.findViewById(R.id.photoLayout);

        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(requireContext(), "Разрешение на камеру отклонено", Toast.LENGTH_SHORT).show();
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        ImageView imageView = new ImageView(requireContext());
                        imageView.setImageURI(imageUri);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                200, 200);
                        imageView.setLayoutParams(params);
                        collageLayout.addView(imageView);
                    }
                });

        view.findViewById(R.id.photoBtn).setOnClickListener(v -> {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                File photoFile = createImageFile();
                String authorities = requireContext().getPackageName() + ".fileprovider";
                imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraActivityResultLauncher.launch(cameraIntent);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }
}