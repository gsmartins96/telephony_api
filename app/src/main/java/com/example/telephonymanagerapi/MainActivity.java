package com.example.telephonymanagerapi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int READ_PHONE_STATE_REQUEST_CODE = 1000;

    TelephonyManager telephony;
    TextView telephoneTypeView;
    TextView phoneImeiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        telephoneTypeView = findViewById(R.id.telephonyType);
        phoneImeiView = findViewById(R.id.telephonyImei);

        telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void getPermissionForImei(View view){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                READ_PHONE_STATE_REQUEST_CODE
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getTelephonyInfo(View view) {

        getImei(view);

        getType(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getImei(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            phoneImeiView.setText("IMEI: Permissão negada");
        } else {
            String telephonyImei = telephony.getImei();

            phoneImeiView.setText("IMEI: " + telephonyImei);
        }
    }

    public void getType(View view){

        int phoneType = telephony.getPhoneType();
        String telephonyType;

        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                telephonyType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                telephonyType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                telephonyType = "NONE";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + phoneType);
        }

        telephoneTypeView.setText("Type: " + telephonyType);
    }

    public void resetResult(View view){
        phoneImeiView.setText("IMEI: ");
        telephoneTypeView.setText("Type: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PHONE_STATE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Phone State Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Phone State Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}