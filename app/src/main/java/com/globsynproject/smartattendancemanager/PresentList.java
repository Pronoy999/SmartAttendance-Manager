package com.globsynproject.smartattendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PresentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_list);
        ListView listView=(ListView)findViewById(R.id.presentList);

        ArrayList<String>arrayList=Constant.dataBaseController.getPresentList();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                arrayList);
        listView.setAdapter(adapter1);
        /*(findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.dataBaseController.getAllData();
            }
        });*/
    }
}
