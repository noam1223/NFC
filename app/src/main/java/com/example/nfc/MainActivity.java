package com.example.nfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akaita.android.circularseekbar.CircularSeekBar;
import com.example.nfc.Helpers.DatabaseHelperSQLite;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button historyTempBtn, contactDoctorBtn;
    ImageView logoImageView;


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    CircularSeekBar circularSeekBar;
    DatabaseHelperSQLite databaseHelperSQLite;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoImageView = findViewById(R.id.imageLogo);
        logoImageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));

        relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bttom));

        databaseHelperSQLite = new DatabaseHelperSQLite(this);
        circularSeekBar = findViewById(R.id.seekbar);

        circularSeekBar.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {

                if (progress >= 33.0f){
                    seekBar.setRingColor(getResources().getColor(R.color.colorlightBlue));
                }if (progress >= 35.0f){
                    seekBar.setRingColor(getResources().getColor(R.color.colorOrange));
                }if (progress >= 37.0f){
                    seekBar.setRingColor(getResources().getColor(R.color.colorRed));

                }

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }
        });




        contactDoctorBtn = findViewById(R.id.contactYourDoctorBtn);
        contactDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: UPDATE DOCTOR

                Toast.makeText(MainActivity.this, "בעזרת כפתור זה תהיינה אפשרות ליצור קשר עם הרופא בגרסא הבאה", Toast.LENGTH_SHORT).show();

            }
        });



        historyTempBtn = findViewById(R.id.historyOfTempBtn);
        historyTempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: SAVE THE TEMPERATUR VALUE IN SQLITE AND SHOW IT IN TEMPERATUR HISTORY ACTIVITY
                Intent intent = new Intent(getApplicationContext(), TemperaturHistoryActivity.class);
                startActivity(intent);

            }
        });



        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null){
            Toast.makeText(this, "No NFC Connection option on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }






    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            circularSeekBar.setProgressText(tagContent + (char) 0x00B0);
            circularSeekBar.setProgress(Float.valueOf(tagContent));

            SimpleDateFormat currentTime = new SimpleDateFormat("EEEE, MMMM dd,yyyy hh:mm");
            String time = currentTime.format(Calendar.getInstance().getTime());

            addDataToDatabase(tagContent, time);


        }else
        {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }

    }





    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }





    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();

    }




    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();

    }




    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }





    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }





    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NFC Tag Detected!", Toast.LENGTH_SHORT).show();

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if(parcelables != null && parcelables.length > 0)
                {
                    readTextFromMessage((NdefMessage) parcelables[0]);
                }else{
                    Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show();
                }


        }
    }




    private void addDataToDatabase(String temp, String time){

        boolean insertData = databaseHelperSQLite.addData(temp, time);

        if (insertData){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }
}
