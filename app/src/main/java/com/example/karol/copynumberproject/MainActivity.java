package com.example.karol.copynumberproject;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static ClipboardManager clipboardManager;
    private static Context mContext;
    private static Switch mySwitch;
    public static String eventNumber = "1692180824159566";


    public static void backgroundProcessIsOff(){
        mySwitch.setChecked(false);
        Toast.makeText(mContext, "Background processing is off",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        final EditText eventField = new EditText(this);
        eventField.setText(eventNumber);
        linearLayout.addView(eventField);

        mContext = this.getApplicationContext();

        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        ClipData abc = clipboardManager.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);

        String clipBordText = item.getText().toString();

        String number = ifNumberIntext(clipBordText);

        Switch mySwitch = new Switch(this);
        linearLayout.addView(mySwitch);

        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                Toast.makeText(getApplication(), "Background processing is on",
                        Toast.LENGTH_LONG).show();

                Intent webService = new Intent(getApplicationContext(),WebScanner.class);
                webService.putExtra("event",eventField.getText().toString());
                startService(webService);
                startService(new Intent(getApplicationContext(), BinderService.class));


            }
        });

        setContentView(linearLayout);
    }

    private void launchBackgroundService() {
        Intent newIntent = new Intent(this, BinderService.class);
        startService(newIntent);

        finish();
    }

    public static String ifNumberIntext(String text){

        //creating number patterns
        String pattern1 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern2 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern3 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern4 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern5 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern6 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern7 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern8 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern9 = "(([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020])([\\d\\-\\u0020]))";
        String pattern10 = "(\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d)";
        Pattern r1 = Pattern.compile(pattern1);
        Pattern r2 = Pattern.compile(pattern2);
        Pattern r3 = Pattern.compile(pattern3);
        Pattern r4 = Pattern.compile(pattern4);
        Pattern r5 = Pattern.compile(pattern5);
        Pattern r6 = Pattern.compile(pattern6);
        Pattern r7 = Pattern.compile(pattern7);
        Pattern r8 = Pattern.compile(pattern8);
        Pattern r9 = Pattern.compile(pattern9);
        Pattern r10 = Pattern.compile(pattern10);
        Matcher m1 = r1.matcher(text);
        Matcher m2 = r2.matcher(text);
        Matcher m3 = r3.matcher(text);
        Matcher m4 = r4.matcher(text);
        Matcher m5 = r5.matcher(text);
        Matcher m6 = r6.matcher(text);
        Matcher m7 = r7.matcher(text);
        Matcher m8 = r8.matcher(text);
        Matcher m9 = r9.matcher(text);
        Matcher m10 = r10.matcher(text);
        ArrayList<Matcher> matchers = new ArrayList<>();
        matchers.add(m1);
        matchers.add(m2);
        matchers.add(m3);
        matchers.add(m4);
        matchers.add(m5);
        matchers.add(m6);
        matchers.add(m7);
        matchers.add(m8);
        matchers.add(m9);
        matchers.add(m10);

        String number = null;

        //checking patterns
        for(Iterator<Matcher> matcherIterator = matchers.iterator(); matcherIterator.hasNext();) {
            Matcher currentMatcher = matcherIterator.next();
            if (currentMatcher.find()) {
                String newGroup = currentMatcher.group();
                String newGroup1 = newGroup.replaceAll("\\s","");
                String newGroup2 = newGroup1.replaceAll("-","");
                System.out.println("returned number");
                number = newGroup2;
                return number;
            }
        }
        System.out.println("NO NUMBER FOUND");
        return null;
    }

}



