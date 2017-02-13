package com.globsynproject.smartattendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    static String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final FileController fileController=new FileController(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        final EditText userN=(EditText) findViewById(R.id.username);
        final EditText pass=(EditText) findViewById(R.id.password);
        account=bundle.getString(Constant.LOGIN_ACCOUNT);
        findViewById(R.id.signup).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Enter the details!","");
                return false;
            }
        });
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userN.getText().toString().length()!=0 && pass.getText().toString().length()!=0) {
                    fileController.create_loginFile(account);
                    Message.toastMessage(getApplicationContext(), "Logged in successfully!", "");
                    goToActivity();
                }
                else Message.toastMessage(getApplicationContext(),"Please provide the details first.","");
            }
        });
    }
    private void goToActivity(){
        Intent intent=null;
        if(account.equals("teacher")){
            intent=new Intent(LoginActivity.this,RegisterActivity.class);
        }
        else if(account.equals("student")){
            intent=new Intent(LoginActivity.this,StudentRegister.class);
        }
        if(intent!=null)
            startActivity(intent);
        finish();
    }
}
