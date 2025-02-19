package com.groupx.simplenote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.groupx.simplenote.activity.CreateReminderActivity;
import com.groupx.simplenote.common.Const;
import com.groupx.simplenote.database.NoteDatabase;
import com.groupx.simplenote.entity.Note;

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        int noteId = bundle.getInt("noteId");
        Note note = NoteDatabase.getSNoteDatabase(context.getApplicationContext())
                .noteDao().getNoteById(noteId);
        String date = bundle.getString("date") + " " + bundle.getString("time");
        //Click on Notification
        Intent intent1 = new Intent(context, CreateReminderActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("note", note);
        intent1.putExtra("mode", Const.NoteDetailActivityMode.EDIT);
        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent1, PendingIntent.FLAG_MUTABLE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        //here we set all the properties for the notification
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.layout, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, note.getTitle());
        contentView.setTextViewText(R.id.date, date);
        mBuilder.setSmallIcon(R.drawable.ic_alarm);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_AUTO_CANCEL | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);
    }
}