package com.globsynproject.smartattendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**This is the activity to register the new student database.*/
public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText roll;
    private static Timer timer;

    private static int timeOut=0;
    private DataBaseController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        roll = (EditText) findViewById(R.id.roll);
        WifiController.wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        dc = new DataBaseController(this);
        (findViewById(R.id.addStudent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WifiController.getConnectionStatus()) {
                    Message.toastMessage(getApplicationContext(), "Please switch on WiFi and remain disconnected from ALL networks to proceed.", "long");
                    return;
                }
                String nameS = name.getText().toString();
                String rollS = roll.getText().toString();
                if(nameS.equals("")||rollS.equals("")) {
                    Message.toastMessage(getApplicationContext(), "Please fill in ALL fields", "");
                    return;
                }
                if(rollS.length()<8){
                    Message.toastMessage(getApplicationContext(), "The Roll Number MUST be at least 8 digits long", "");
                    return;
                }
                registerStudent(nameS, rollS);
            }
        });
        /*register.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Add students one after another!","");
                return false;
            }
        });
        completeRegister.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Registraton of all students completed!","");
                return false;
            }
        });*/
        findViewById(R.id.register_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity();
            }
        });

    }

    private void registerStudent(final String nameS, final String rollS){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message.logMessages("CHECK: ", "Checking status...");
                if (timeOut >= 20) {
                    Message.logMessages("WIFI: ", "TIMED OUT");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message.toastMessage(RegisterActivity.this, "Registration failed. Please try again", "long");
                        }
                    });
                    timeOut = 0;
                    timer.cancel();
                }
                if (WifiController.getConnectionStatus()) {
                    Message.logMessages("WIFI: ", "CONNECTED");
                    ContentValues values = new ContentValues();
                    values.put(Constant.NAME, nameS);
                    values.put(Constant.ROLL_NUMBER, rollS);
                    WifiInfo info = WifiController.wifiManager.getConnectionInfo();
                    values.put(Constant.SSID, nameS);
                    values.put(Constant.BSSID, info.getSSID());
                    WifiController.disbandConnection();
                    Message.logMessages("WIFI: ", "DISCONNECTED");
                    values.put(Constant.PASSWORD, rollS);
                    values.put(Constant.ATTENDANCE, 0);
                    values.put(Constant.FLAG, 0);
                    dc.inputData(values);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message.toastMessage(RegisterActivity.this, "REGISTERED SUCCESSFULLY!", "long");
                        }
                    });
                    Message.logMessages("REGISTER", "REGISTERED");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reset();
                        }
                    });
                    return;
                }
                timeOut++;
            }
        }, 0, 1000);
        WifiController.establishConnection(nameS, rollS);
    }
    private void reset(){
        name.setText("");
        roll.setText("");
        timeOut=0;
        Message.logMessages("RESET", "TIMER CANCELLED, TIMEOUT =0");
        timer.cancel();
    }
    private void goToActivity(){
        Bundle bundle;
        FileController fileController=new FileController(getApplicationContext());
        DataBaseController dataBaseController=new DataBaseController(getApplicationContext());
        fileController.backup_StudentNumber(Constant.NUMBER_STUDENTS);
        bundle=dataBaseController.getPasswordAndSSID();
        Intent intent=new Intent(RegisterActivity.this,TeacherActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}