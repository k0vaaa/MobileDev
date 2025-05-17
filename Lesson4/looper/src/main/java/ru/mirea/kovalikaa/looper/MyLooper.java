package ru.mirea.kovalikaa.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                String ageStr = msg.getData().getString("age");
                int age = Integer.parseInt(ageStr);
                String statusStr = msg.getData().getString("status");

                mHandler.postDelayed(() -> {
                    String result = String.format("Ваш возраст - %s, вы - %s", ageStr, statusStr);

                    Log.d("MyLooper", "Данные успешно получены!");

                    Message message = Message.obtain();
                    Bundle b = new Bundle();
                    b.putString("result", result);
                    message.setData(b);
                    mainHandler.sendMessage(message);
                }, (age*1000));
            }
        };
        Looper.loop();
    }
}