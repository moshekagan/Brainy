package com.example.first.kaganmoshe.brainy.DailyPractice.DPReminder;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.AppActivities.ActionBarActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;
import Utils.AppCheckBox;

/**
 * Created by tamirkash on 9/4/15.
 */
public class DPNotification extends ActionBarAppActivity {

    // This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;
    private LinearLayout mCheckBoxesLayout;
    // This is the date picker used to select the date for our notification
    private DatePicker picker;

    private static final String CHECKED_DAYS_PREFERENCES = "CHECKED_DAYS";
    private static final int REMINDER_HOUR = 6;
    private static final int REMINDER_MINUTE = 0;
    private static final int REMINDER_SECOND = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp_day_picker);

        mCheckBoxesLayout = (LinearLayout) findViewById(R.id.checkBoxesLayout);

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this.getApplicationContext());
        scheduleClient.doBindService();

//        setTouchNClick(R.id.dpScheduleDoneButton);

        findViewById(R.id.dpScheduleDoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        initCheckBoxes();

        // Get a reference to our date picker
//        picker = (DatePicker) findViewById(R.id.scheduleTimePicker);
    }

    /**
     * This is the onClick called from the XML to set a new notification
     */
    public void onDateSelectedButtonClick(View v) {
        // Get the date from our datepicker
//        int day = picker.getDayOfMonth();
//        int month = picker.getMonth();
//        int year = picker.getYear();
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        Calendar c = Calendar.getInstance();

//        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, 18);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
        // Notify the user what they just did
        Toast.makeText(this, "Notification set for: " + c.getTime().getDay() + "/" + (c.getTime().getMonth() + 1) + "/" + c.getTime().getYear(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if (scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    public void onCancelScheduleButtonClick(View view) {
        Intent intent = new Intent(getApplicationContext(), NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    private void initCheckBoxes() {
        for (int i = 0; i < mCheckBoxesLayout.getChildCount(); i++) {
            CheckBox dayCheckBox = (CheckBox) mCheckBoxesLayout.getChildAt(i);
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CHECKED_DAYS_PREFERENCES,
                    Context.MODE_PRIVATE);
            boolean defaultValue = getResources().getBoolean(R.bool.default_day_checked);
            boolean isDayChecked = sharedPref.getBoolean(dayCheckBox.getText().toString(), defaultValue);

            dayCheckBox.setChecked(isDayChecked);
        }
    }

    @Override
    public void onBackPressed() {
        checkChanges();
        Utils.startNewActivity(this, MainActivity.class);
    }

    private void checkChanges() {
        for (int i = 0; i < mCheckBoxesLayout.getChildCount(); i++) {
            CheckBox dayCheckBox = (CheckBox) mCheckBoxesLayout.getChildAt(i);
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CHECKED_DAYS_PREFERENCES,
                    Context.MODE_PRIVATE);
            boolean defaultValue = getResources().getBoolean(R.bool.default_day_checked);
            boolean isDaySavedAsChecked = sharedPref.getBoolean(dayCheckBox.getText().toString(), defaultValue);

            if (isDaySavedAsChecked != dayCheckBox.isChecked()) {
                updateCheckedDay(dayCheckBox);
            }
        }
    }

    private void updateCheckedDay(CheckBox dayCheckBox) {
        updateSharePreferences(dayCheckBox);

        if (dayCheckBox.isChecked()) {
            setDayReminder(dayCheckBox);
        } else {
            cancelDayReminder(dayCheckBox);
        }
    }

    private void updateSharePreferences(CheckBox dayCheckBox) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CHECKED_DAYS_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(dayCheckBox.getText().toString(), dayCheckBox.isChecked());
        editor.commit();
    }

    private void cancelDayReminder(CheckBox dayCheckBox) {
        Intent intent = new Intent(getApplicationContext(), NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(AlarmTask.DAY, ((AppCheckBox)dayCheckBox).getDayOfWeek());
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    private void setDayReminder(CheckBox dayCheckBox) {
        Calendar c = Calendar.getInstance();
        int dayNum = ((AppCheckBox)dayCheckBox).getDayOfWeek();
        int thisDayNum = c.get(Calendar.DAY_OF_WEEK);

        c.set(Calendar.HOUR_OF_DAY, REMINDER_HOUR);
        c.set(Calendar.MINUTE, REMINDER_MINUTE);
        c.set(Calendar.SECOND, REMINDER_SECOND);
        c.set(Calendar.DAY_OF_WEEK, dayNum);

        if(dayNum < thisDayNum || (dayNum == thisDayNum && c.get(Calendar.HOUR_OF_DAY) >= REMINDER_HOUR)){
            int i = c.get(Calendar.WEEK_OF_MONTH);
            c.set(Calendar.WEEK_OF_MONTH, ++i);
        }

        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
    }
}

