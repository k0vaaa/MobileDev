package ru.mirea.kovalikaa.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.kovalikaa.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private	static	final	int	REQUEST_CODE_PERMISSION	=	100;
    private	static	final	int	CAMERA_REQUEST	=	0;
    private	boolean	isWork	=	false;
    private Uri imageUri;
    private ActivityMainBinding binding;


    @Override
    protected	void	onCreate(Bundle	savedInstanceState)	{
        super.onCreate(savedInstanceState);
        //	ДОБАВИТЬ	ФАЙЛ	РАЗМЕТКИ
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //	ДОБАВИТЬ	ПРОВЕРКУ	НА	НАЛИЧИЕ	РАЗРЕШЕНИЙ
        //	НА	ИСПОЛЬЗОВАНИЕ	КАМЕРЫ
        permissionCheck();
        //	Создание	функции	обработки	результата	от	системного	приложения	«камера»
        ActivityResultCallback<ActivityResult>	callback	=	new	ActivityResultCallback<ActivityResult>()	{
            @Override
            public	void	onActivityResult(ActivityResult	result)	{
                if	(result.getResultCode()	==	Activity.RESULT_OK)	{
                    Intent data	=	result.getData();
                    binding.imageView.setImageURI(imageUri);
                }
            }
        };
        ActivityResultLauncher<Intent> cameraActivityResultLauncher	=		registerForActivityResult(
                new	ActivityResultContracts.StartActivityForResult(),
                callback);
        //	Обработчик	нажатия	на	компонент	«imageView»
        binding.imageView.setOnClickListener(new	View.OnClickListener()	{
            @Override
            public	void	onClick(View	v)	{
                Intent	cameraIntent	=	new	Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //	проверка на наличие разрешений для камеры и записи
                if	(isWork)	{
                    try	{
                        File photoFile	=	createImageFile();
                        //	генерирование пути к файлу на основе authorities
                        String	authorities	=	getApplicationContext().getPackageName()	+	".fileprovider";
                        imageUri	=	FileProvider.getUriForFile(MainActivity.this,	authorities,	photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,	imageUri);
                        cameraActivityResultLauncher.launch(cameraIntent);
                    }	catch	(IOException e)	{
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void permissionCheck() {
        String[] permissions = { Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE};
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        if (allPermissionsGranted) {
            isWork = true;
        } else {
            ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> {
                        isWork = result.getOrDefault(Manifest.permission.CAMERA, false);
                        if (!isWork) {
                            Toast.makeText(this, "Required permissions not granted", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            permissionLauncher.launch(permissions);
        }
    }

    private	File	createImageFile()	throws	IOException	{
        String	timeStamp	=	new SimpleDateFormat("yyyyMMdd_HHmmss",	Locale.ENGLISH).format(new Date());
        String	imageFileName	=	"IMAGE_"	+	timeStamp	+	"_";
        File	storageDirectory	=	getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return	File.createTempFile(imageFileName,	".jpg",	storageDirectory);
    }

}