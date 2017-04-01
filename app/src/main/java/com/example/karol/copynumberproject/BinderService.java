package com.example.karol.copynumberproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by karol on 6/23/16.
 */

public class BinderService extends Service {

    //Global variables
    private NotificationManager myNotificationManager;
    private ClipboardManager clipboardManager;
    private String clipBordText;

    protected void displayNotificationOne(String number) {

        // Invoking the default notification service
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setAutoCancel(true);

        mBuilder.setContentTitle("I've found a number: " + number);
        mBuilder.setContentText("To dial it, just tap me");
        mBuilder.setTicker("Explicit: New Message Received!");
        mBuilder.setSmallIcon(R.drawable.dial_icon_dark);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.dialicon));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 21) mBuilder.setVibrate(new long[0]);

        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(1);
        /*if (MainActivity.checkTheState()) {
            Intent intentDial=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));
            intentDial.setData(Uri.parse("tel:" + number));
            //startActivity(intentDial);
            //resultIntent.putExtra("number",number);

            //This ensures that navigating backward from the Activity leads out of the app to Home page
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // Adds the back stack for the Intent
            stackBuilder.addParentStack(MainActivity.class);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intentDial);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_ONE_SHOT //can only be used once
                    );
            // start the activity when the user clicks the notification text
            mBuilder.setContentIntent(resultPendingIntent);
        } else {*/
            // Creates an explicit intent for an Activity in your app
            Intent intentDial = new Intent(Intent.ACTION_DIAL);
            intentDial.setData(Uri.parse("tel:" + number));
            //startActivity(intentDial);
            //resultIntent.putExtra("number",number);

            //This ensures that navigating backward from the Activity leads out of the app to Home page
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // Adds the back stack for the Intent
            stackBuilder.addParentStack(MainActivity.class);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intentDial);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_ONE_SHOT //can only be used once
                    );
            // start the activity when the user clicks the notification text
            mBuilder.setContentIntent(resultPendingIntent);
        //}

        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        myNotificationManager.notify(112, mBuilder.build());

    }

    @Override
    public void onCreate() {
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ClipData abc = clipboardManager.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);

        clipBordText = item.getText().toString();


        clipboardManager
                .addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

                    @Override
                    public void onPrimaryClipChanged() {
                        ClipData clipData = clipboardManager.getPrimaryClip();
                        System.out
                                .println("********** clip changed, clipData: "
                                        + clipData.getItemAt(0));
                        ClipData.Item item = clipData.getItemAt(0);
                        if(clipBordText.equals(item.getText().toString())) return;
                        else{
                            /// do something
                            String newClipBoard = item.getText().toString();
                            String number = MainActivity.ifNumberIntext(newClipBoard);
                            clipBordText = newClipBoard;
                            if(number != null){
                                displayNotificationOne(number);

                            }
                        }
                    }
                });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        MainActivity.backgroundProcessIsOff();
    }
}
