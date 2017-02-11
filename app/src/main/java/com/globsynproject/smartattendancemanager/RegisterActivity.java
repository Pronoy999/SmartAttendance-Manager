package com.globsynproject.smartattendancemanager;

import android.content.ContentValues;
import android.net.wifi.WifiInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**This is the activity to register the new student database.*/
public class RegisterActivity extends AppCompatActivity {

    EditText name, roll;
    Button register;
    Timer timer;
    TimerTask task;
    WifiController controller;
    int timeOut=0;
    DataBaseController dc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        roll = (EditText) findViewById(R.id.roll);
        register = (Button) findViewById(R.id.register);
        controller = new WifiController(this);
        dc = new DataBaseController(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!controller.getConnectionStatus()) {
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
    }

    public void registerStudent(final String nameS, final String rollS){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message.logMessages("CHECK: ", "Checking status...");
                if (timeOut >= 20) {
                    Message.logMessages("WIFI: ", "TIMED OUT");
                    Message.toastMessage(getApplicationContext(), "Registration failed. Please try again", "long");
                    timeOut = 0;
                }
                if (controller.getConnectionStatus()) {
                    Message.logMessages("WIFI: ", "CONNECTED");
                    ContentValues values = new ContentValues();
                    values.put(Constant.NAME, nameS);
                    values.put(Constant.ROLL_NUMBER, rollS);
                    WifiInfo info = controller.wifiManager.getConnectionInfo();
                    values.put(Constant.SSID, nameS);
                    values.put(Constant.BSSID, info.getSSID());
                    controller.disbandConnection();
                    values.put(Constant.PASSWORD, rollS);
                    values.put(Constant.ATTENDANCE, 0);
                    values.put(Constant.FLAG, 0);
                    dc.inputData(values);
                    Message.toastMessage(getApplicationContext(), "Registered Successfully!", "");
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
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    public void reset(){
        name.setText("");
        roll.setText("");
        timeOut=0;
        Message.logMessages("RESET", "TIMER CANCELLED, TIMEOUT =0");
        timer.cancel();
    }
}