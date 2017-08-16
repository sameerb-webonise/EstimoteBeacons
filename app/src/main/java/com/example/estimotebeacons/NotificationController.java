package com.example.estimotebeacons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by webonise on 16/8/17.
 */

public class NotificationController {
    private static final String GROUP_KEY_BUNDLED = "Estimote";
    private final String TAG = NotificationController.class.getName();
    public static final int NOTIFICATION = 100;
    private static final long SLEEP_TIME = 2 * 1000; // 2 seconds

    private Boolean isDirty = false;
    private boolean IS_RUN = true;
    //private NotificationThread mNotificationThread = null;
    private NotificationData mNotificationData = null;

    private static NotificationController mNotificationController = new NotificationController();

    class NotificationData {
        public String content;
        public Intent extras;
    }

    public static NotificationController getInstance() {

        return mNotificationController;
    }

    private NotificationController() {

        /*if (mNotificationThread == null) {
            mNotificationThread = new NotificationThread();
            mNotificationThread.start();
        }*/
    }

    public synchronized void showNotification(String tag, String content) {

        String tone = null;
        boolean isVibrateOn = false;

        if (mNotificationData == null) {
            mNotificationData = new NotificationData();
        }

        mNotificationData.content = content;
        //mNotificationData.extras = extras;
        //mNotificationData.extras.putExtra(Constants.NOTIFICATION_LAUNCH_FROM_BUNDLE, Constants.NOTIFICATION_BUNDLE_VALUE);

        //setDirty(true);
        optimizeNotification(tag, false);
        //mNotificationThread.sendNotify(tag, tone, isVibrateOn);
    }

    public void stopNotification() {
        /*if (mNotificationThread != null) {
            IS_RUN = false;
            setDirty(false);

            //mNotificationThread.sendNotify(null, null, false);

            mNotificationThread = null;
        }*/
    }

    /*private class NotificationThread extends Thread {
        private String mTone;
        private boolean mIsVibrateOn;
        private String mTag;

        public synchronized void sendNotify(String tag, String tone, boolean isVibrateOn) {
            mTag = tag;
            mTone = tone;
            mIsVibrateOn = isVibrateOn;
            notify();
        }

        @Override
        public void run() {
            Thread.currentThread().setName(this.getClass().getSimpleName());
            while (IS_RUN) {
                try {
                    while (isDirty()) {
                        optimizeNotification(mTag, mTone, mIsVibrateOn);
                        setDirty(false);
                        sleep(SLEEP_TIME);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    private void optimizeNotification(String tag, boolean mIsVibrateOn) {
        try {
            Context context = MyApplication.getAppContext();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            String title = context.getResources().getString(R.string
                    .app_name);
            builder.setContentTitle(title);
            builder.setContentText(mNotificationData.content);
            builder.setTicker(mNotificationData.content);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setGroupSummary(true);
            builder.setGroup(GROUP_KEY_BUNDLED);
            //builder.setLargeIcon( BitmapFactory.decodeResource( SeqriteChatApplication.mContext
            // .getResources(), R.drawable.ic_notification_large ) );
            builder.setColor(context.getResources().getColor(R.color.colorPrimary));
            builder.setDefaults(Notification.DEFAULT_LIGHTS);


            //If vibrate is allowed, use default vibrate mode.
            if (mIsVibrateOn) {
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
            }

            //use selected tone OR default tone.
            /*if (!TextUtils.isEmpty(tone)) {
                File toneFile = new File(tone);
                if (toneFile != null && toneFile.exists()) {
                    builder.setSound(Uri.parse(tone));
                    //if tone is Silent
                } else if (tone.equals(context.getString(R.string.device_separation))) {

                } else {
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(uri);
                }
            } else {
                *//*Settings.System.DEFAULT_RINGTONE_URI*//*
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uri);
            }*/
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(uri);
            builder.setAutoCancel(true);


            Intent resultIntent = new Intent(context, EstimoteActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            builder.setContentText(mNotificationData.content);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            builder.setStyle(inboxStyle.setSummaryText(mNotificationData.content));
            //resultIntent.putExtras(mNotificationData.extras);
            //below line is not significant but if not used then intent extras are dropped from
            // 1st notification and no new extras are added from second
            //resultIntent.setAction( Long.toString( System.currentTimeMillis( ) ) );

            //int type = mNotificationData.extras.getIntExtra(Constants.NOTIFICATION_TYPE_BUNDLE, FIND_ME_NOTIFICATION);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack
//            stackBuilder.addParentStack( HomeActivity.class );
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            // Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent
                    .FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);


                /* Notify */
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(tag, NOTIFICATION, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    private synchronized boolean isDirty() {
        return isDirty;
    }

    public void cancelNotification(String tag, int type) {
        Context context = MyApplication.getAppContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(tag, type);
    }
}
