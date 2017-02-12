package com.globsynproject.smartattendancemanager;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

public class ViewAttendance extends TabActivity {

    static String[] presentArray;
    static String[] absentArray;

    ListView present, absent;
    static DataBaseController dataBaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseController = new DataBaseController(getApplicationContext());
        TabHost mTabHost = getTabHost();
        present = (ListView) findViewById(R.id.present);
        absent = (ListView) findViewById(R.id.absent);
        // mTabHost=(TabHost) findViewById(R.id.tabhost) ;

        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Present Students").setContent(R.id.present));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Absent Students").setContent(R.id.absent));
        mTabHost.setCurrentTab(0);

        absent.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        absent.setTextFilterEnabled(true);

        presentArray = new String[Constant.NUMBER_STUDENTS];
        absentArray = new String[Constant.NUMBER_STUDENTS];

        dataBaseController.getPresentList();
        presentArray = Constant.getList;
        dataBaseController.getAbsentList();
        absentArray = Constant.getList;

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                presentArray);
        present.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                absentArray);
        absent.setAdapter(adapter2);
    }

    public void onClick(View view) {
        //String itemsSelected = "Selected items: \n";
        for (int i = 0; i < absentArray.length; i++) {
            if (absent.isItemChecked(i)) {
                dataBaseController.putAttendanceByRoll(absentArray[i].substring(0, absentArray[i].indexOf(" ")));
            }
        }
    }
}
