package ru.mirea.kovalikaa.mireaproject;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
public class MyWorker extends Worker {
    private static final String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d(TAG, "Старт фоновой задачи...");
            Thread.sleep(5000);
            Log.d(TAG, "Фоновая задача завершена успешно!");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Ошибка в работе фоновой задачи", e);
            return Result.failure();
        }
    }
}
