package com.example.lifetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//create notifications to remind user of their to do
public class ReminderReceiver extends BroadcastReceiver {
    String GROUP_KEY_TO_DO = "com.example.lifetracker";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyNiso")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Reminder To-Do")
                .setContentText("Don't forget to finish your To-Do:"+intent.getExtras().getString("name"))//get to do name
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //specify which group this notification belongs to(ability to have more than one reminder)
                .setGroup(GROUP_KEY_TO_DO)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                ;

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(( int ) System.currentTimeMillis() , builder.build());
    }
}
