package com.example.mipet.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class AlarmReceiver  extends BroadcastReceiver {
    //broadcast receiver para que pueda escuchar las notificaciones del sistema
    @Override
    public void onReceive(Context context, Intent intent) {
        //creamos un intent y llamamos a la clase encargada de crear
        // la notificación en la barra de tareas
        Intent service1 = new Intent(context, NotificationService.class);
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ContextCompat.startForegroundService(context, service1 );

    }
}
