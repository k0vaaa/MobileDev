package ru.mirea.kovalikaa.data_thread;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

import ru.mirea.kovalikaa.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.textView.setText("runn1");
            }
        };
        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.textView.setText("runn2");
            }
        };
        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.textView.setText("runn3\n" +
                        "Различия:\n" +
                        "runOnUiThread - Если вызывается из UI-потока, Runnable выполняется сразу (синхронно). Если вызывается из фонового потока, задача добавляется в конец очереди главного потока.\n" +
                        "\n" +
                        "postDelayed - Задача добавляется в очередь только после истечения указанной задержки. После этого она выполняется в порядке очереди.\n" +
                        "\n" +
                        "post - Всегда добавляет задачу в очередь главного потока, даже если вызов происходит из UI-потока. Выполнение происходит в следующем цикле обработки сообщений (асинхронно).\n"+
                        "\n" +
                        "Последовательность:\n" +
                                "runn1 ( ~ 2 сек.)\n" +
                                "runn2 (~ 3 сек. )\n" +
                                "runn2 (~ 5 сек. )\n");

            }
        };

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    runOnUiThread(runn1);
                    TimeUnit.SECONDS.sleep(1);
                    binding.textView.postDelayed(runn3, 2000);
                    binding.textView.post(runn2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}