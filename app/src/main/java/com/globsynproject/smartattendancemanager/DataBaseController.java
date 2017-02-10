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
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

/** This is the database controller class
 */

public class DataBaseController{


    MyHelper helper;

    /**
     * This is the constructor of the class DataBaseAdapter
     * @param context: The current context of the activity
     */

    public DataBaseController(Context context) {

        helper = new MyHelper(context);
    }

    /**
     * This method creates the database and updates the data with student information
     * @param contentValues: It contains NAME,ROLL_NUMBER,SSID,BSSID,PASSWORD for each student
     * @return id of the row where the data is inserted
     */
    public long inputData(ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        //contentValues.put(Constant.ATTENDANCE,0);
        ////id will be -ve if something went wrong;
        // otherwise it indicates the row id of the column that was successfully inserted
        return  sqLiteDatabase.insert(Constant.TABLE_NAME, null, contentValues);
    }

    /**
     * Thsi method stores the SSID and Passwords of the students from the datdabase and stores them in String Arrays of keys :ssid and password and puts it in a Bundle
     * @return b:Bundle b contains the String Arrays ssid and password with their keys same as their names.
     */
    public Bundle getPasswordAndSSID(){

        int i=1;
        Bundle b=new Bundle();
        String ssid[]=new String [Constant.NUMBER_STUDENTS];
        String password[]=new String[Constant.NUMBER_STUDENTS];
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String columns[] = {Constant.SSID, Constant.PASSWORD};
        Cursor cursor = sqLiteDatabase.query(Constant.TABLE_NAME, columns,
                null, null, null, null, null);
        while (cursor.moveToNext()){
            ssid[i++]=cursor.getString(cursor.getColumnIndex(Constant.SSID));
            password[++i]=cursor.getString(cursor.getColumnIndex(Constant.PASSWORD));
        }
        cursor.close();
        b.putStringArray("SSID",ssid);
        b.putStringArray("KEYS",password);
        return  b;
    }

    /**This method updates the Database attendance
     *
     * @param bssid: bssid of the current student
     * @return id of the row where the update is made
     */
      public long putAttendance(String bssid){
          int attendance=0,flag=0;
             SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
            String columns[]={Constant.ATTENDANCE};
            String selectionArgs[]={bssid};
            Cursor cursor=sqLiteDatabase.query(Constant.TABLE_NAME,columns,Constant.BSSID+"=?",selectionArgs,null,null,null,null);
            if(cursor==null) {
              Message.logMessages("Error: ", "Wrong BSSID sent");
                return -1;
            }
            while (cursor.moveToNext()) {
                attendance=cursor.getInt(cursor.getColumnIndex(Constant.ATTENDANCE));
            }
            Log.d("TAG", "BSSID found\n Attendance being registered");
            ContentValues contentValues=new ContentValues();
            contentValues.put(Constant.ATTENDANCE,attendance+1);
            contentValues.put(Constant.FLAG,1);
            cursor.close();
            String[] whereArgs = {bssid};
            return sqLiteDatabase.update(Constant.TABLE_NAME,contentValues,Constant.BSSID+"=?",whereArgs);
    }

    /**
     * This function is called after the Database has been updated with current attendance.
     * @return b: b is a Bundle type data which contains String arrays with keys name,roll AND Integer array with key attendance.
     * The Bundle shall be extracted to from the present list of students for the current day
     */

    public Bundle  getPresentList() {
        Bundle b=new Bundle();
        int i=1;
        String names[]=new String[Constant.NUMBER_STUDENTS];
        String rolls[]=new String[Constant.NUMBER_STUDENTS];
        int atten[]=new int[Constant.NUMBER_STUDENTS];
        ContentValues contentValues =new ContentValues();
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String columns[] = {Constant.NAME, Constant.ROLL_NUMBER, Constant.ATTENDANCE};
        String selectionArgs[] = {"1"};
        Cursor cursor = sqLiteDatabase.query(Constant.TABLE_NAME, columns, Constant.FLAG + "=?", selectionArgs, null, null, null, null);
        while (cursor.moveToNext()) {
             names [i++]=cursor.getString(cursor.getColumnIndex(Constant.NAME));
            rolls[i++]=cursor.getString(cursor.getColumnIndex(Constant.ROLL_NUMBER));
            atten[i++]=cursor.getInt(cursor.getColumnIndex(Constant.ATTENDANCE));
        }
        cursor.close();
        b.putStringArray("name",names);
        b.putStringArray("roll",rolls);
        b.putIntArray("attendance",atten);
        b.putInt("ArrayIndex",i);
        return b;

    }
    public Bundle getAbsentList(){
        Bundle b=new Bundle();
        int i=1;
        String names[]=new String[Constant.NUMBER_STUDENTS];
        String rolls[]=new String[Constant.NUMBER_STUDENTS];
        int atten[]=new int[Constant.NUMBER_STUDENTS];
        ContentValues contentValues =new ContentValues();
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String columns[] = {Constant.NAME, Constant.ROLL_NUMBER, Constant.ATTENDANCE};
        String selectionArgs[] = {"0"};
        Cursor cursor = sqLiteDatabase.query(Constant.TABLE_NAME, columns, Constant.FLAG + "=?", selectionArgs, null, null, null, null);
        while (cursor.moveToNext()) {
            names [i++]=cursor.getString(cursor.getColumnIndex(Constant.NAME));
            rolls[i++]=cursor.getString(cursor.getColumnIndex(Constant.ROLL_NUMBER));
            atten[i++]=cursor.getInt(cursor.getColumnIndex(Constant.ATTENDANCE));
        }
        cursor.close();
        b.putStringArray("name",names);
        b.putStringArray("roll",rolls);
        b.putIntArray("attendance",atten);
        b.putInt("ArrayIndex",i);
        return b;

    }

    private static class MyHelper extends SQLiteOpenHelper {

        private static final String CREATE_TABLE = "CREATE TABLE " + Constant.TABLE_NAME + "("
                +Constant.UID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Constant.NAME + " VARCHAR(255), "
                +Constant.ROLL_NUMBER +" VARCHAR(255),"
                +Constant.SSID +" VARCHAR(255),"
                +Constant.BSSID + " VARCHAR(255),"
                +Constant.PASSWORD +" VARCHAR(255),"
                +Constant.ATTENDANCE +" INT,"
                +Constant.FLAG + " INT);";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constant.TABLE_NAME;

        //private Context context;

        /**
         * This is the constructor of the class MyHelper which creates suitable environment for Database creation
         * @param context: The current application context
         */

        private MyHelper(Context context) {
            super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
            //this.context = context;
            Message.logMessages("MyHelper:","Database created");
        }

        /**
         * This method is called first time when data is about to be written in the Database
         * @param db: The Database reference
         */

        @Override
        //Called for the 1st time when the database is about to be created
        public void onCreate(SQLiteDatabase db) {
            try {
                //Execute a single SQL statement that is NOT a SELECT or
                // any other SQL statement that returns data.
                db.execSQL(CREATE_TABLE);
                Message.logMessages("onCreate :","onCreate() called");
            } catch (SQLException e) {
                Message.logMessages("Error in onCreate :",""+e);
            }
        }

        /**
         * This method is called when the database is about to be ugraded
         * @param db: The current database reference
         * @param oldVersion: The old version of the database
         * @param newVersion: The new version of the database
         */

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.logMessages("onUpgrade :","onUpgrade() called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                Message.logMessages("Error in onUpgrade :",""+e);
            }
        }
    }
}