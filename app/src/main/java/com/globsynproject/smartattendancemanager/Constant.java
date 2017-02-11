package com.globsynproject.smartattendancemanager;

/**
 * This is the abstract class to declare all the constants of the Data bases and file and other classes.
 */

public abstract class Constant {

    /**
     * CLASS_NUMBER is the total number of class occured> This shall be increased by one each time attendance is taken.
     */
    public static int CLASS_NUMBER=0;
/**The value of STUDENTS_NUMBER is the total number of students in the class.This shall be initialized during registration*/
    public static  int NUMBER_STUDENTS=0;
    public static final String DATABASE_NAME="StudentDataBase.db";
    public static final String TABLE_NAME="Student";
    public static final String FILE_NAME="temp.dat";
    public static final int DATABASE_VERSION = 1;
    public static final String UID = "_id";
    public static final String NAME = "Name";
    public static final String BSSID = "BSSID";
    public static final String ROLL_NUMBER = "ROLL NUMBER";
    public static final String ATTENDANCE = "ATTENDANCE";
    public static final String SSID = "SSID";
    public static final String PASSWORD = "PASSWORD";
    public static final String FLAG="FLAG";
    public static final String LOGIN_FILE="Login.dat";
    public static final String LOGIN_ACCOUNT="ACCOUNT";
    public static final String BUNDLE_KEY_SSID = "SSID";
    public static final String BUNDLE_KEY_PASSWORD = "KEYS";

}
