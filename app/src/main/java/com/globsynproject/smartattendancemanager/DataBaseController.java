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

                 int indexAttendance = cursor.getColumnIndex(Constant.ATTENDANCE);
                attendance=cursor.getInt(indexAttendance);
            }
            Log.d("TAG", "BSSID found\n Attendance being registered")
            ContentValues contentValues=new ContentValues();
            contentValues.put(Constant.ATTENDANCE,attendance+1);
            contentValues.put(Constant.FLAG,1);
            cursor.close();
            String[] whereArgs = {bssid};
            return sqLiteDatabase.update(Constant.TABLE_NAME,contentValues,Constant.BSSID+"=?",whereArgs);
    }

    /**
     *
     * @return
     */

    public ContentValues  getPresentList(){
        SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
        String columns[]={Constant.NAME,Constant.ROLL_NUMBER,Constant.ATTENDANCE};
        String selectionArgs[]={"1"};
        Cursor cursor=sqLiteDatabase.query(Constant.TABLE_NAME,columns,Constant.FLAG+"=?",selectionArgs,null,null,null,null);
        
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