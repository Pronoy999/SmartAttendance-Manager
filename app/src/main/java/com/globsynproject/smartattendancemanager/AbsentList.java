package com.globsynproject.smartattendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AbsentList extends AppCompatActivity {

    ListView listView;
    static DataBaseController dataBaseController;
    ArrayList<String>arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_list);

        listView=(ListView)findViewById(R.id.absentList);

        dataBaseController=new DataBaseController(getApplicationContext());
        arrayList=dataBaseController.getAbsentList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                arrayList);
        listView.setAdapter(adapter);
    }
    public void addManually(View view){
        int i=0;
        for(String l:arrayList){
            if(listView.isItemChecked(i)){
                dataBaseController.putAttendanceByRoll(l.substring(0,l.indexOf(" ")));
            }
            i++;
        }
    }
}
