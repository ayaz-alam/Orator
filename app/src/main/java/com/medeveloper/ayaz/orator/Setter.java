package com.medeveloper.ayaz.orator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Setter extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    TextView SessionTime;
    TextView SessionDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setter);
        final Calendar mCurrentTime = Calendar.getInstance();
        int year = mCurrentTime.get(Calendar.YEAR);
        int month = mCurrentTime.get(Calendar.MONTH);
        int day = mCurrentTime.get(Calendar.DAY_OF_MONTH);

        SessionTime = findViewById(R.id.time);
        SessionDate = findViewById(R.id.date);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, Setter.this, year, month, day);
        (findViewById(R.id.date))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();
                    }
                });

        (findViewById(R.id.time))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(Setter.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String time = selectedHour + ":" + selectedMinute;
                                SessionTime.setText(getTimeInAMPM(time));
                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select SessionTime");
                        mTimePicker.show();
                    }
                });

        (findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = SessionTime.getText().toString();
                String date = SessionDate.getText().toString();
                String word = ((EditText) findViewById(R.id.word_of_the_day)).getText().toString();
                String topic = ((EditText) findViewById(R.id.topic_of_the_day)).getText().toString();
                String speaker = ((EditText) findViewById(R.id.speaker_of_the_day)).getText().toString();
                String meaning = ((EditText) findViewById(R.id.word_of_the_day_explanation)).getText().toString();
                if (date.equals("Select Date") || time.equals("Select Time") || word.equals("") || topic.equals("") || speaker.equals("") || meaning.equals(""))
                    Toast.makeText(getBaseContext(), "Please fill the details Correctly", Toast.LENGTH_SHORT).show();
                else {

                    SessionDetail detail = new SessionDetail(date, time, word, meaning, topic, speaker);
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("NextSession");
                    myRef.setValue(detail);
                }


            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;

        String selectedDate = dayOfMonth + " " + getMonthForInt(month) + " " + year;

        SessionDate.setText(selectedDate);


    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    String getTimeInAMPM(final String time) {

        String AMPM="wrong";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            System.out.println(dateObj);
            AMPM=new SimpleDateFormat("hh:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return AMPM;
    }


}
