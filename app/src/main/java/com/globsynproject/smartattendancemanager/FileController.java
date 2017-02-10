package com.globsynproject.smartattendancemanager;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * Created for Pronoy.
 */

public class FileController {
    private Context context;
    private static OutputStreamWriter outputStreamWriter;
    /**
     * NOTE: Pass the context as the parameter for creating the objects of this class
     * @param context: The current Application context
     */
    public FileController(Context context){
        this.context=context;
    }

    /**
     * NOTE: This is the method to insert the data to the file temporarily after taking the attendance.
     * @param BSSID: The String BSSID that will be scanned from the WIFI Controller method for attendance.
     */
    public void appendData_File(String BSSID){
       try {
           outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Constant.FILE_NAME, Context.MODE_APPEND));
           BSSID+=",";
           outputStreamWriter.write(BSSID);
       }
       catch (Exception e){
           Message.logMessages("ERROR: ",e.toString());
       }
        finally {
           try{
               if(outputStreamWriter!=null)
                   outputStreamWriter.close();
           }
           catch (Exception e){
               Message.logMessages("ERROR: ",e.toString());
           }
       }
    }
    /**
     * NOTE: This method is used to send the list of the BSSID's to the database.
     */
    public void sendAttendance(){
        DataBaseController dataBaseController=new DataBaseController(context);
        try{
            FileInputStream fileInputStream=context.openFileInput(Constant.FILE_NAME);
            Scanner obj=new Scanner(fileInputStream);
            obj.useDelimiter(",");
            while(obj.hasNext()){
                dataBaseController.putAttendance(obj.next());
            }
            obj.close();
            fileInputStream.close();
        }
        catch (Exception e){
            Message.logMessages("ERROR: ",e.toString());
        }
    }

    /**
     * NOTE: This is the method to delete the file.
     */
    public void delete_File(){
        context.deleteFile(Constant.FILE_NAME);
    }

    /**
     * NOTE: This is the method to create the file for the first time.
     * @param account: This is string to save whether the teacher logs in or student.
     */
    public void create_loginFile(String account){
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Constant.LOGIN_FILE, Context.MODE_PRIVATE));
            outputStreamWriter.write(account+" logged in");
            outputStreamWriter.close();
        }
        catch (Exception e){
            Message.logMessages("ERROR: ",e.toString());
        }
    }

    /**
     * NOTE: This the method to check whether the account is logged in or not.
     * @return: the data from the file, "student logged in" or "teacher logged in"
     */
    public String check_loginFile(){
        String text="";
        try{
            FileInputStream fileInputStream=context.openFileInput(Constant.LOGIN_FILE);
            Scanner obj=new Scanner(fileInputStream);
            while(obj.hasNext())
                text=obj.next();
            obj.close();
            fileInputStream.close();
        }
        catch (FileNotFoundException e){
            return "not logged in";
        }
        catch (Exception e){
            Message.logMessages("ERROR: ",e.toString());
        }
        return text;
    }
}
