package com.globsynproject.smartattendancemanager;

import android.content.Context;

import java.io.FileInputStream;
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
    public void appenData_File(String BSSID){
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
}
