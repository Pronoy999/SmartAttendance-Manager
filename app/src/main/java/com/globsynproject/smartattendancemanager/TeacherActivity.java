package com.globsynproject.smartattendancemanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    static FileController fileController;
    static String ssid[], pwd[];
    Button manualAttendance, showAttendance;
    static int timeOut = 0, position =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        fileController=new FileController(this);
        Bundle b = getIntent().getExtras();
        ssid = b.getStringArray(Constant.BUNDLE_KEY_SSID);
        pwd = b.getStringArray(Constant.BUNDLE_KEY_PASSWORD);
        WifiController.wifiManager =(WifiManager) getSystemService(WIFI_SERVICE);
        showAttendance=(Button) findViewById(R.id.showAttendance);
        manualAttendance=(Button) findViewById(R.id.manualAttendance);
        showAttendance.setVisibility(View.INVISIBLE);
        manualAttendance.setVisibility(View.INVISIBLE);

        /*takeAttendance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Take the attendance of the class!","");
                return false;
            }
        });
        manualAttendance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Add the attendance manually","");
                return false;
            }
        });
        showAttendance.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Show today's attendance","");
                return false;
            }
        });*/
        if(WifiController.getConnectionStatus()){
            Message.toastMessage(getApplicationContext(),"Before pressing the button, please switch on WiFi and remain disconnected from ALL networks.", "long");
            return;
        }
        findViewById(R.id.takeAttendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAttendance();
            }
        });
    }
    public void startAttendance(){
        Constant.CLASS_NUMBER++;
        if(!WifiController.checkWifiOn()||(WifiController.checkWifiOn()&&WifiController.wifiManager.getConnectionInfo().getSupplicantState().equals(SupplicantState.COMPLETED))){
            Message.toastMessage(this, "Please switch on WiFi and remain disconnected from ALL networks to proceed.", "long");
            return;
        }
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
            Message.toastMessage(getApplicationContext(),"Attendance taken!","");
            showAttendance.setVisibility(View.VISIBLE);
            manualAttendance.setVisibility(View.VISIBLE);
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