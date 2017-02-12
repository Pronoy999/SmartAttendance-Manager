package com.globsynproject.smartattendancemanager;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


/**This is the activity for the teacher to take the attendance.*/
public class StudentActivity extends AppCompatActivity {

    private static WifiConfiguration wifiConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        /*button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Give today's attendance!","");
                return false;
            }
        });*/
        WifiController.wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        findViewById(R.id.present).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveAttendance();
            }
        });
    }

    /**
     * NOTE: This is the method to give the attendance.
     */
    private void giveAttendance(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String name=bundle.getString(Constant.REGISTER_NAME);
        String password=bundle.getString(Constant.REGISTER_PASSWORD);
        //wifiController=new WifiController(getApplicationContext());
        wifiConfiguration=WifiController.turnAccessPointOn(name,password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wifiConfiguration!=null)
            WifiController.turnAccessPointOff(wifiConfiguration);
    }
}
