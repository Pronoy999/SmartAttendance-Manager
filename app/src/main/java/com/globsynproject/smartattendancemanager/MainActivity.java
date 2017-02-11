package com.globsynproject.smartattendancemanager;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**This is the first activity to be displayed.*/
public class MainActivity extends AppCompatActivity {
    private ImageView teacher;
    private ImageView student;
    private Intent intent;
    private Bundle bundle;
    private FileController fileController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent i = new Intent().setAction(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                this.startActivity(i);
            }
        }
        teacher=(ImageView) findViewById(R.id.teacher);
        student=(ImageView) findViewById(R.id.student);
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTeacher();
            }
        });
        teacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Teacher's Login!","");
                return false;
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToStudent();
            }
        });
        student.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Student's Login!","");
                return false;
            }
        });
    }

    /**
     * NOTE: This is the method to take the user to the teacher activity.
     */
    private void goToTeacher(){
        fileController=new FileController(getApplicationContext());
        String login=fileController.check_loginFile();
        if(login.equals("teacher logged in")){
            DataBaseController dataBaseController=new DataBaseController(getApplicationContext());
            bundle=dataBaseController.getPasswordAndSSID();
            intent=new Intent(MainActivity.this,TeacherActivity.class);
            intent.putExtras(bundle);
        }
        else {
            bundle=new Bundle();
            bundle.putString(Constant.LOGIN_ACCOUNT,"teacher");
            intent=new Intent(MainActivity.this,LoginActivity.class);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * NOTE: This is the method to take the user to the Student activity.
     */
    private void goToStudent(){
        fileController=new FileController(getApplicationContext());
        String login=fileController.check_loginFile();
        if(login.equals("student logged in")){
            String register=fileController.check_RegisterFle();
            if(register.equals("not registered"))
                intent=new Intent(MainActivity.this,StudentRegister.class);
            else{
                String name=register.substring(0,register.indexOf(' '));
                String pass=register.substring(register.indexOf(' ')+1);
                bundle=new Bundle();
                bundle.putString(Constant.REGISTER_NAME,name);
                bundle.putString(Constant.REGISTER_PASSWORD,pass);
                intent=new Intent(MainActivity.this,StudentActivity.class);
                intent.putExtras(bundle);
            }
        }
        else{
            bundle=new Bundle();
            bundle.putString(Constant.LOGIN_ACCOUNT,"student");
            intent=new Intent(MainActivity.this,LoginActivity.class);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
