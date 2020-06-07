package ru.sibsutis.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    private TextView TimeDisplay;
    private TimePicker mTimePicker;
    private Button SetTime;

    private int hours;
    private int minutes;

    static final int TIME_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCurrentTime();
        ButtonListener();
    }

    //При запуске отображаем текущее время
    public void setCurrentTime() {
        TimeDisplay = (TextView) findViewById(R.id.CurTime);
        mTimePicker = (TimePicker) findViewById(R.id.TimePick);

        final Calendar calendar = Calendar.getInstance();
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        //Настраиваем текущее время в TextView:
        TimeDisplay.setText(
                new StringBuilder().append(pad(hours))
                        .append(":").append(pad(minutes)));

        //Настраиваем текущее время в TimePicker:
        mTimePicker.setCurrentHour(hours);
        mTimePicker.setCurrentMinute(minutes);
    }

    //Добавляем слушателя нажатий кнопки:
    public void ButtonListener() {
        SetTime = (Button) findViewById(R.id.button);
        SetTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //При нажатии кнопки запускается диалог TimePickerDialog для выбора времени:
                showDialog(TIME_DIALOG_ID);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                //Задаем в TimePicker текущее время:
                return new TimePickerDialog(this,
                        timePickerListener, hours, minutes, false);
        }
        return null;
    }

    //Настраиваем диалоговое окно TimePickerDialog:
    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    hours = selectedHour;
                    minutes = selectedMinute;

                    //Настраиваем выбранное время в TextView:
                    TimeDisplay.setText(new StringBuilder().append(pad(hours))
                            .append(":").append(pad(minutes)));

                    //Настраиваем выбранное время в TimePicker:
                    mTimePicker.setCurrentHour(hours);
                    mTimePicker.setCurrentMinute(minutes);

                }
            };

    //Для показания минут настраиваем отображение 0 впереди чисел со значением меньше 10:
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
