package ru.mirea.kovalikaa.dialog;

import android.app.TimePickerDialog;
import android.content.Context;

public class MyTimeDialogFragment extends TimePickerDialog {
    public MyTimeDialogFragment(Context context, OnTimeSetListener listener, int hour, int minute, boolean fullView){
        super(context,listener,hour, minute, fullView);
    }

}
