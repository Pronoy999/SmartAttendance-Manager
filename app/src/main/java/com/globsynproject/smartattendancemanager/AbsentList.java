package com.globsynproject.smartattendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AbsentList extends AppCompatActivity {

    ListView listView;
    DataBaseController dataBaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_list);

        listView=(ListView)findViewById(R.id.absentList);

        dataBaseController=new DataBaseController(getApplicationContext());
        dataBaseController.getPresentList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                Constant.absentListArray);
        listView.setAdapter(adapter);
    }
    public void addManually(View view){
        int i=0;

        for( String s :Constant.absentListArray){
            if(listView.isItemChecked(i)){
                dataBaseController.putAttendanceByRoll(s.substring(0,s.indexOf(" ")));
            }
        }
    }
}
