package com.example.a1183243_1192364_courseproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("due_date");

        long snoozeTime = System.currentTimeMillis() + 1 * 60 * 1000; // Snooze for 10 minutes

        Intent snoozeIntent = new Intent(context, NotificationReceiver.class);
        snoozeIntent.putExtra("title", title);
        snoozeIntent.putExtra("due_date", date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, snoozeTime, pendingIntent);
    }
}

