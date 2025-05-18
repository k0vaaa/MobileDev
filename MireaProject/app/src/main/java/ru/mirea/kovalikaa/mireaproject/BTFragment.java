package ru.mirea.kovalikaa.mireaproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkInfo;


public class BTFragment extends Fragment {

    private Button startTaskButton;
    private TextView statusTextView;
    private Handler handler;
    private Runnable counterRunnable;
    private int secondsElapsed = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bt, container, false);

        startTaskButton = view.findViewById(R.id.btnRunTask);
        statusTextView = view.findViewById(R.id.textViewStatus);

        startTaskButton.setOnClickListener(v -> startBackgroundTask());

        return view;
    }

    private void startBackgroundTask() {
        handler = new Handler(Looper.getMainLooper());
        secondsElapsed = 0;
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();

        WorkManager.getInstance(requireContext()).enqueue(workRequest);

        counterRunnable = new Runnable() {
            @Override
            public void run() {
                secondsElapsed++;
                statusTextView.setText("Прошло секунд: " + secondsElapsed);
                handler.postDelayed(this, 1000);
            }
        };

        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null) {
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            handler.removeCallbacks(counterRunnable);
                            statusTextView.setText("Задача успешно завершена!");
                        } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                            handler.removeCallbacks(counterRunnable);
                            statusTextView.setText("Ошибка выполнения задачи.");
                        } else if (workInfo.getState() == WorkInfo.State.RUNNING) {
                            handler.post(counterRunnable);
                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(counterRunnable);
    }
}
