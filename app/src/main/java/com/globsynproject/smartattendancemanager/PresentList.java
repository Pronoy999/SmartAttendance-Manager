package com.globsynproject.smartattendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PresentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_list);
        ListView listView=(ListView)findViewById(R.id.presentList);

        DataBaseController dataBaseController=new DataBaseController(getApplicationContext());
        ArrayList<String>arrayList=dataBaseController.getPresentList();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                arrayList);
        listView.setAdapter(adapter1);
    }
}
