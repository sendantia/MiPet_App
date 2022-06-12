package com.example.mipet.app.utils;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.mipet.app.service.AlarmReceiver;

public class Utils {
    //en esta clase creamos el metedo que crea la notificación a una hora determinada
    public static void setAlarm(int i, Long timestamp, Context ctx) {
        //creamos la clase con alamrManager
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        //creamos el intent , que cuando reciba la alarma llama a la clase AlarmReceiver
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);


    }
}
