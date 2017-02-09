package com.globsynproject.smartattendancemanager;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created for Shubham and Indranil.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.MutableBoolean;

public class DataBaseController{


    MyHelper helper;

    public DataBaseController(Context context) {
        helper = new MyHelper(context);
    }

    public long inputData(String name,String roll,String ssid,String bssid,String password) {
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyHelper.NAME, name);
        contentValues.put(MyHelper.ROLL_NUMBER,roll);
        contentValues.put(MyHelper.SSID,ssid);
        contentValues.put(MyHelper.BSSID,bssid);
        contentValues.put(MyHelper.PASSWORD,password);
        contentValues.put(MyHelper.ATTENDANCE,0);
        ////id will be -ve if something went wrong;
        // otherwise it indicates the row id of the column that was successfully inserted
        long id = sqLiteDatabase.insert(Constant.TABLE_NAME, null, contentValues);
        return id;
    }
    public long putAttendance(String bssid){
        SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
        String columns[]={MyHelper.ATTENDANCE,MyHelper.NAME};
        String selectionArgs[]={MyHelper.BSSID};
        Cursor cursor=sqLiteDatabase.query(Constant.TABLE_NAME,columns,MyHelper.BSSID+"=?",selectionArgs,null,null,null,null);
        if(cursor==null) {
            Log.d("TAG", "Wrong BSSID sent");
            return -1;
        }
        //cursor.getColumnIndex(MyHelper.ATTENDANCE);
        Log.d("TAG", "BSSID found\n Attendance being registered");
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyHelper.ATTENDANCE,MyHelper.ATTENDANCE+1);
        String[] whereArgs = {bssid};
        return sqLiteDatabase.update(Constant.TABLE_NAME,contentValues,MyHelper.BSSID+"=?",whereArgs);
    }
    static class MyHelper extends SQLiteOpenHelper {
        //private static final String DATABASE_NAME = "mydatabase.db";

        //private static final String TABLE_NAME = "MYTABLE";

        private static final int DATABASE_VERSION = 1;

        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String BSSID = "BSSID";
        private static final String ROLL_NUMBER = "ROLL NUMBER";
        private static final String ATTENDANCE = "ATTENDANCE";
        private static final String SSID = "SSID";
        private static final String PASSWORD = "PASSWORD";

        private static final String CREATE_TABLE = "CREATE TABLE " + Constant.TABLE_NAME + "("
                +UID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NAME + " VARCHAR(255), "
                +ROLL_NUMBER +"VARCHAR(255),"
                +SSID +"VARCHAR(255),"
                +BSSID + " VARCHAR(255),"
                +PASSWORD +"VARCHAR(255),"
                +ATTENDANCE +"INTEGER);";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constant.TABLE_NAME;

        //private Context context;

        public MyHelper(Context context) {
            super(context, Constant.DATABASE_NAME, null, DATABASE_VERSION);
            //this.context = context;
            Message.logMessages("TAG","Database created");
        }

        @Override
        //Called for the 1st time when the database is about to be created
        public void onCreate(SQLiteDatabase db) {
            try {
                //Execute a single SQL statement that is NOT a SELECT or
                // any other SQL statement that returns data.
                db.execSQL(CREATE_TABLE);
                Message.logMessages("TAG","onCreate() called");
            } catch (SQLException e) {
                Message.logMessages("TAG",""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.logMessages("TAG","onUpgrade() called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                Message.logMessages("TAG",""+e);
            }
        }
    }
}