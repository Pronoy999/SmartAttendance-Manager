package com.globsynproject.smartattendancemanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class TeacherActivity extends AppCompatActivity {
    static ProgressDialog progressDialog;
    static Timer timer;
    FileController fileController;
    static String ssid[], pwd[];
    Button showAbsent, showPresent;
    static int timeOut = 0, position =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        fileController=new FileController(this);

        //Message.toastMessage(getApplicationContext(),Constant.NUMBER_STUDENTS+"","");
        Bundle b = getIntent().getExtras();
        ssid = b.getStringArray(Constant.BUNDLE_KEY_SSID);
        pwd = b.getStringArray(Constant.BUNDLE_KEY_PASSWORD);
        WifiController.wifiManager =(WifiManager) getSystemService(WIFI_SERVICE);
        showAbsent=(Button) findViewById(R.id.showAbsentList);
        showPresent=(Button) findViewById(R.id.showPresentList);
        showAbsent.setVisibility(View.INVISIBLE);
        showPresent.setVisibility(View.INVISIBLE);

        if(WifiController.getConnectionStatus()){
            Message.toastMessage(getApplicationContext(),Constant.WIFI_WARNING_MESSAGE, "long");
            return;
        }
        findViewById(R.id.takeAttendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAttendance();
            }
        });
        showPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PresentList.class));
            }
        });
        showAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AbsentLIst.class));
            }
        });
    }
    public void startAttendance(){
        if(!WifiController.checkWifiOn()||(WifiController.checkWifiOn()&&WifiController.getConnectionStatus())){
            Message.toastMessage(getApplicationContext(), Constant.WIFI_NOT_READY_MESSAGE, "long");
            return;
        }
        Constant.CLASS_NUMBER++;
        showDialog(Constant.ID_PROGRESS_DIALOG);
        startNewConnection(position);
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message.logMessages("CHECK: ", "Checking status...");
                if(timeOut>=20){
                    Message.logMessages("WIFI: ","TIMED OUT");
                    timeOut=0; position++;
                    progressDialog.incrementProgressBy(1);
                    startNewConnection(position);
                    return;
                }

                if(WifiController.getConnectionStatus()){
                    Message.logMessages("WIFI: ","CONNECTED");
                    writeToFile(WifiController.wifiManager.getConnectionInfo());
                    WifiController.disbandConnection();
                    timeOut=0; position++;
                    progressDialog.incrementProgressBy(1);
                    startNewConnection(position);
                    return;
                }
                timeOut++;
            }
        }, 0, 1000);
    }

    public void startNewConnection(int pos){
        if(pos>=Constant.NUMBER_STUDENTS){
            progressDialog.setProgress(0);
            progressDialog.dismiss();
            timer.cancel();
            Message.logMessages("DONE", "COMPLETE");
            WifiController.turnWifiOff();
            position=0; timeOut=0;
            fileController.sendAttendance();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Message.toastMessage(getApplicationContext(),"Attendance taken!","");
                    showAbsent.setVisibility(View.VISIBLE);
                    showPresent.setVisibility(View.VISIBLE);
                }
            });
            return;
        }
        Message.logMessages("WIFI", "Setting up connection: "+ssid[pos]+", "+pwd[pos]);
        Message.logMessages("CHECK: ", Boolean.toString(WifiController.establishConnection(ssid[pos], pwd[pos])));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileController.delete_File();//deleting the temporary file.
    }

    public void writeToFile(WifiInfo info){
        fileController.appendData_File(info.getBSSID());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        progressDialog = new ProgressDialog(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setTitle("Taking Attendance");
        progressDialog.setIcon(R.drawable.icon);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                timer.cancel();
                position=0; timeOut=0;
                progressDialog.setProgress(0);
                progressDialog.dismiss();
            }
        });
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(Constant.NUMBER_STUDENTS);
        return progressDialog;
    }
}