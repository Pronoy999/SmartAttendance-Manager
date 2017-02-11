package com.globsynproject.smartattendancemanager;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**This is the activity for the teacher to take the attendance.*/
public class StudentActivity extends AppCompatActivity {
    private Button button;
    private WifiConfiguration wifiConfiguration;
    private WifiController wifiController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        button=(Button) findViewById(R.id.present);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Give today's attendance!","");
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
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
        String password=bundle.getString(Constant.REGSITER_PASSWORD);
        wifiController=new WifiController(getApplicationContext());
        wifiConfiguration=wifiController.turnAccessPointOn(name,password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiController.turnAccessPointOff(wifiConfiguration);
    }
}
