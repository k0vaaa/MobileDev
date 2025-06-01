package ru.mirea.kovalikaa.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.kovalikaa.mireaproject.databinding.FragmentAudioNoteBinding;

public class AudioNoteFragment extends Fragment {

    private static final String TAG = AudioNoteFragment.class.getSimpleName();
    private FragmentAudioNoteBinding binding;
    private boolean hasPermissions = false;
    private String audioFilePath;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecording = false;
    private boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAudioNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audioFilePath = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "audio_note_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".mp3").getAbsolutePath();

        checkPermissions();

        binding.recordButton.setOnClickListener(v -> {
            if (hasPermissions) {
                if (!isRecording) {
                    binding.recordButton.setText("Остановить запись");
                    binding.playButton.setEnabled(false);
                    startAudioRecording();
                } else {
                    binding.recordButton.setText("Начать запись");
                    binding.playButton.setEnabled(true);
                    stopAudioRecording();
                }
                isRecording = !isRecording;
            } else {
                showToast("Требуется разрешение на запись аудио");
            }
        });

        binding.playButton.setOnClickListener(v -> {
            if (!isPlaying) {
                binding.playButton.setText("Остановить воспроизведение");
                binding.recordButton.setEnabled(false);
                startAudioPlayback();
            } else {
                binding.playButton.setText("Воспроизвести");
                binding.recordButton.setEnabled(true);
                stopAudioPlayback();
            }
            isPlaying = !isPlaying;
        });

        binding.playButton.setEnabled(false);
    }

    private void checkPermissions() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            hasPermissions = true;
        } else {
            ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> {
                        hasPermissions = result.getOrDefault(Manifest.permission.RECORD_AUDIO, false);
                        if (!hasPermissions) {
                            showToast("Разрешение на запись аудио не предоставлено");
                        }
                    }
            );
            permissionLauncher.launch(permissions);
        }
    }

    private void startAudioRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            showToast("Запись начата");
        } catch (IOException e) {
            Log.e(TAG, "Ошибка подготовки записи: " + e.getMessage());
            showToast("Ошибка при записи");
        }
    }

    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                Log.e(TAG, "Ошибка остановки записи: " + e.getMessage());
            }
            mediaRecorder.release();
            mediaRecorder = null;
            showToast("Запись сохранена: " + audioFilePath);
        }
    }

    private void startAudioPlayback() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            showToast("Воспроизведение начато");
        } catch (IOException e) {
            Log.e(TAG, "Ошибка воспроизведения: " + e.getMessage());
            showToast("Ошибка при воспроизведении");
        }
    }

    private void stopAudioPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAudioRecording();
        stopAudioPlayback();
        binding = null;
    }
}