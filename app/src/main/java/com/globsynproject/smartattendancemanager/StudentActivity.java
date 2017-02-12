package com.globsynproject.smartattendancemanager;

import android.net.wifi.WifiConfiguration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**This is the activity for the teacher to take the attendance.*/
public class StudentActivity extends AppCompatActivity {
    private static WifiConfiguration wifiConfiguration;
    private static WifiController wifiController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        findViewById(R.id.present).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Give today's attendance!","");
                return false;
            }
        });
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
        Bundle bundle=getIntent().getExtras();
        String name=bundle.getString(Constant.REGISTER_NAME);
        String password=bundle.getString(Constant.REGISTER_PASSWORD);
        wifiController=new WifiController(getApplicationContext());
        wifiConfiguration=wifiController.turnAccessPointOn(name,password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wifiConfiguration!=null)
            wifiController.turnAccessPointOff(wifiConfiguration);
    }
}
