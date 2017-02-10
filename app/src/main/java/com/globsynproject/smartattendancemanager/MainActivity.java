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
    private void goToTeacher(){
        fileController=new FileController(getApplicationContext());
        String login=fileController.check_loginFile();
        if(login.equals("teacher logged in")){
            intent=new Intent(MainActivity.this,TeacherActivity.class);
        }
        else {
            bundle=new Bundle();
            bundle.putString(Constant.LOGIN_ACCOUNT,"teacher");
            intent=new Intent(MainActivity.this,LoginActivity.class);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    private void goToStudent(){
        fileController=new FileController(getApplicationContext());
        String login=fileController.check_loginFile();
        if(login.equals("student logged in")){
            intent=new Intent(MainActivity.this,StudentActivity.class);
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
