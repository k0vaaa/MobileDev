package ru.mirea.kovalikaa.serviceapp;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.kovalikaa.serviceapp.databinding.ActivityMainBinding;

public	class	MainActivity	extends	AppCompatActivity	{
    private ActivityMainBinding binding;
    private	int	PermissionCode	=	200;
    @Override
    protected	void	onCreate(Bundle	savedInstanceState)	{
        super.onCreate(savedInstanceState);
        binding	=	ActivityMainBinding.inflate(getLayoutInflater());
        View	view	=	binding.getRoot();
        setContentView(view);
        if	(ContextCompat.checkSelfPermission(this,	POST_NOTIFICATIONS)	==	PackageManager.PERMISSION_GRANTED)	{
            Log.d(MainActivity.class.getSimpleName().toString(),	"Разрешения получены");
        }	else	{
            Log.d(MainActivity.class.getSimpleName().toString(),	"Нет разрешений!");
            ActivityCompat.requestPermissions(this,	new	String[]{POST_NOTIFICATIONS,	FOREGROUND_SERVICE},	PermissionCode);
        }
        binding.btnPlay.setOnClickListener(new	View.OnClickListener()	{
            @Override
            public	void	onClick(View	v)	{
                Intent	serviceIntent	=	new	Intent(MainActivity.this,	PlayerService.class);
                ContextCompat.startForegroundService(MainActivity.this,	serviceIntent);
            }
        });
        binding.btnStop.setOnClickListener(new	View.OnClickListener()	{
            @Override
            public	void	onClick(View v)	{
                stopService(
                        new Intent(MainActivity.this,	PlayerService.class));
            }
        });
    }
}