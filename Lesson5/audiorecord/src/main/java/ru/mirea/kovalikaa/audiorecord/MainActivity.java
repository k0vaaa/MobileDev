package ru.mirea.kovalikaa.audiorecord;

import	androidx.annotation.NonNull;
import	androidx.appcompat.app.AppCompatActivity;
import	androidx.core.app.ActivityCompat;
import	androidx.core.content.ContextCompat;
import	android.Manifest;
import	android.content.pm.PackageManager;
import	android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import ru.mirea.kovalikaa.audiorecord.databinding.ActivityMainBinding;

public	class	MainActivity	extends	AppCompatActivity	{
    private	static	final	int	REQUEST_CODE_PERMISSION	=	200;
    private	final	String	TAG	=	MainActivity.class.getSimpleName();
    private ActivityMainBinding	binding;
    private	boolean	isWork;
    private	String	fileName	=	null;
    private	Button	recordButton	=	null;
    private Button playButton	=	null;
    private MediaRecorder recorder	=	null;
    private MediaPlayer player	=	null;
    boolean	isStartRecording	=	true;
    boolean	isStartPlaying	=	true;

    String recordFilePath;
    @Override
    protected	void	onCreate(Bundle	savedInstanceState)	{
        super.onCreate(savedInstanceState);
        binding	=	ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // инициализация кнопок записи и воспроизведения
        recordButton	=	binding.recordBtn;
        playButton	=	binding.playBtn;

        playButton.setEnabled(false);
        recordFilePath	=	(new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.mp3")).getAbsolutePath();
        int	audioRecordPermissionStatus	=	ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        int	storagePermissionStatus	=	ContextCompat.checkSelfPermission(this,	android.Manifest.permission.
                WRITE_EXTERNAL_STORAGE);
        if	(audioRecordPermissionStatus	==	PackageManager.PERMISSION_GRANTED	&&	storagePermissionStatus
                ==	PackageManager.PERMISSION_GRANTED)	{
            isWork	=	true;
        }	else	{
            //	Выполняется	запрос	к	пользователь	на	получение	необходимых	разрешений
            ActivityCompat.requestPermissions(this,	new	String[]	{Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE},	REQUEST_CODE_PERMISSION);
        }

        recordButton.setOnClickListener(v -> {
            if	(isStartRecording)	{
                recordButton.setText("Stop	recording");
                playButton.setEnabled(false);
                startRecording();
            }	else	{
                recordButton.setText("Start	recording");
                playButton.setEnabled(true);
                stopRecording();
            }
            isStartRecording	=	!isStartRecording;
        });

        playButton.setOnClickListener(v -> {
            if	(isStartPlaying)	{
                playButton.setText("Stop	playing");
                recordButton.setEnabled(false);
                startPlaying();
            }	else	{
                playButton.setText("Start	playing");
                recordButton.setEnabled(false);
                stopPlaying();
            }
            isStartPlaying	=	!isStartPlaying;
        });
    }
    @Override
    public	void	onRequestPermissionsResult(int	requestCode,	@NonNull	String[]	permissions,	@NonNull	int[]
            grantResults)	{
        //	производится	проверка	полученного	результата	от	пользователя	на	запрос	разрешения	Camera
        super.onRequestPermissionsResult(requestCode,	permissions,	grantResults);
        switch	(requestCode){
            case	REQUEST_CODE_PERMISSION:
                isWork		=	grantResults[0]	==	PackageManager.PERMISSION_GRANTED;
                break;
        }
        if	(!isWork	)	finish();
    }

    private	void	startRecording()	{
        recorder	=	new	MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try	{
            recorder.prepare();
        }	catch	(IOException e)	{
            Log.e(TAG,	"prepare()	failed");
        }
        recorder.start();
    }

    private	void	stopRecording()	{
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private	void	startPlaying()	{
        player	=	new	MediaPlayer();
        try	{
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        }	catch	(IOException	e)	{
            Log.e(TAG,	"prepare()	failed");
        }
    }

    private	void	stopPlaying()	{
        player.release();
        player	=	null;
    }
}