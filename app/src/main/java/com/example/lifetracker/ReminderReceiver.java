package com.example.lifetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReceiver extends BroadcastReceiver {
    String GROUP_KEY_TO_DO = "com.example.lifetracker";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyNiso")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Reminder To-Do NiSo")
                .setContentText("Don't forget to finish your To-Do:"+intent.getExtras().getString("name"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //specify which group this notification belongs to
                .setGroup(GROUP_KEY_TO_DO)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                ;

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(( int ) System. currentTimeMillis () , builder.build());
        Log.d("tttt",intent.getExtras().getString("name") );
    }
}
