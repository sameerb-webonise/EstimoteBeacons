package com.example.estimotebeacons;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.LinkedList;
import java.util.List;

public class NotificationUtil {
    private final static String GROUP_KEY_BUNDLED = "group_key_bundled";

    private static final int NOTIFICATION_BUNDLED_BASE_ID = 1000;

    //simple way to keep track of the number of bundled notifications
    //Simple way to track text for notifications that have already been issued
    private static List<CharSequence> issuedMessages = new LinkedList<>();

    public static void bundledNotification(String title, String message) {
        Context context = MyApplication.getAppContext();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        /*String message = "This is message # " + ++numberOfBundled
                + ". This text is generally too long to fit on a single line in a notification";*/
        issuedMessages.add(title);

        //Build and issue the group summary. Use inbox style so that all messages are displayed
        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getResources().getString(R.string
                        .app_name))
                .setContentText(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroupSummary(true)
                .setGroup(GROUP_KEY_BUNDLED);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(context.getResources().getString(R.string
                .app_name));
        for (CharSequence cs : issuedMessages) {
            inboxStyle.addLine(cs);
        }
        summaryBuilder.setStyle(inboxStyle);

        notificationManager.notify(NOTIFICATION_BUNDLED_BASE_ID, summaryBuilder.build());



        //issue the Bundled notification. Since there is a summary notification, this will only display
        //on systems with Nougat or later
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(title))
                .setGroup(GROUP_KEY_BUNDLED);

        //Add an action that simply starts the main activity. This is not very useful it is mainly for demonstration
        Intent intent = new Intent(context, EstimoteActivity.class);
        //Each notification needs a unique request code, so that each pending intent is unique. It does not matter
        //in this simple case, but is important if we need to take action on a specific notification, such as
        //deleting a message
        int requestCode = NOTIFICATION_BUNDLED_BASE_ID;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(android.R.drawable.ic_input_get,
                        "OK", pendingIntent)
                        .build();
        builder.addAction(action);

        notificationManager.notify(NOTIFICATION_BUNDLED_BASE_ID, builder.build());


    }

}
