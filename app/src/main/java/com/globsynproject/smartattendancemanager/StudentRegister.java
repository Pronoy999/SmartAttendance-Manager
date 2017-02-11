package com.globsynproject.smartattendancemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StudentRegister extends AppCompatActivity {
    private EditText name;
    EditText password;
    private Button register;
    FileController fileController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        fileController=new FileController(getApplicationContext());
        name=(EditText) findViewById(R.id.name);
        password=(EditText) findViewById(R.id.roll);
        register=(Button) findViewById(R.id.register);
        register.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message.toastMessage(getApplicationContext(),"Register","");
                return false;
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_File();
            }
        });
    }
    private void register_File(){
        String n=name.getText().toString();
        String r=password.getText().toString();
        if(r.length()>=8) {
            fileController.create_registerFile(n, r);
            Message.toastMessage(getApplicationContext(), "Successfully Registered!", "");
            Intent intent = new Intent(StudentRegister.this, StudentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.REGISTER_NAME, n);
            bundle.putString(Constant.REGISTER_PASSWORD, r);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Message.toastMessage(getApplicationContext(),"Enter correct roll number!","long");
        }
    }
}
