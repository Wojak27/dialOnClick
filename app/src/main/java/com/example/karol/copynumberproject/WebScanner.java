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
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

/**
 * Created by Karol on 2/2/2017.
 */

public class WebScanner extends Service {

    private NotificationManager myNotificationManager;
    private String currentPost = "";
    private String eventNumber = "";
    private ClipboardManager clipboardManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("log", "lol");
        if (intent !=null && intent.getExtras()!=null)
            eventNumber = intent.getExtras().getString("event");
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("s", "started");
        eventNumber = intent.getExtras().getString("event");
        Log.d("s", eventNumber);
    }

    @Override
    public void onCreate() {

        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    SystemClock.sleep(1000);
                    new GetContacts().execute();
                    //REST OF CODE HERE//
                }

            }
        }).start();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        Log.d("trying","");
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    protected void displayNotificationOne(String number, String post) {

        // Invoking the default notification service
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        playNotificationSound();
        mBuilder.setAutoCancel(true);
        if(number == null){
            mBuilder.setContentTitle("No number");

            clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            clipboardManager.setText("Köper, PM");
            Intent facebookIntent = getOpenFacebookIntent(this);
            //This ensures that navigating backward from the Activity leads out of the app to Home page
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // Adds the back stack for the Intent
            stackBuilder.addParentStack(MainActivity.class);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(facebookIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_ONE_SHOT //can only be used once
                    );
            // start the activity when the user clicks the notification text
            mBuilder.setContentIntent(resultPendingIntent);
        }else {
            mBuilder.setContentTitle(number);
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
        }
        mBuilder.setContentText(post);
        mBuilder.setTicker("Explicit: New Message Received!");
        mBuilder.setSmallIcon(R.drawable.dial_icon_dark);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.dialicon));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(post));


        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(1);
        // Creates an explicit intent for an Activity in your app

        //}

        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        myNotificationManager.notify(112, mBuilder.build());

    }

    private void playNotificationSound(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            Log.w("Failed", "sound played");
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Failed", "to play notification sound");
        }
    }

    private boolean isForSale(String currentPost){
        ArrayList<String> listWithWordsForSale = new ArrayList<>();
        listWithWordsForSale.add("säljer");
        listWithWordsForSale.add("salu");
        listWithWordsForSale.add("sälja");
        listWithWordsForSale.add("söker");

        for(String currentWord : listWithWordsForSale) {
            if ( currentPost.toLowerCase().indexOf(currentWord.toLowerCase()) != -1 ) {

                System.out.println("I found the keyword");
                return true;
            }
        }
        return false;
    }

    public  Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://event/"+eventNumber));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tvn24pl/?hc_ref=ARRpzpSI4mWR3d7GjOiZdzdQB1ISVWMDJcJCaNlqeM0gUjJ5GFPMd01f_2n3bm16nEg&fref=nf"));
        }
    }

    private void print(JSONObject json) throws JSONException{
        if(!currentPost.equals(json.getString("message"))){
            Log.d("log ", json.getString("message"));
            currentPost = json.getString("message");
            String number = MainActivity.ifNumberIntext(currentPost);
            if(number != null){
                displayNotificationOne(number, currentPost);

            }else
                // Displays new notification if the kaywords are found in the text
                if(isForSale(currentPost)){
                displayNotificationOne(null, currentPost);
            }
        }else {

        }

    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        private JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("executs","");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://graph.facebook.com/"+eventNumber+"/feed?access_token=611636522345383|d832d238684ea6d9da977575f1a5de91");

            Log.d("executs","");
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("data");
                } catch (JSONException e) {
                    //System.out.println("DIDNT work");

                }
            } else {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                print((JSONObject)jsonArray.get(0));
            }catch (Exception e){

            }


        }


    }
}
