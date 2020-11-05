package com.example.recipe_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView Info;
    private int counter = 5;
    private TextView mTvUserReg;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText) findViewById(R.id.edt_name);
        Password = (EditText) findViewById(R.id.edt_password);
        Login = (Button) findViewById(R.id.btn_login);
        Info = (TextView) findViewById(R.id.tv_info);
        mTvUserReg = (TextView)findViewById(R.id.tv_register);
        forgotPassword = (TextView) findViewById(R.id.tv_forgotpswd);

        Info.setText("No of atempts remaining: 5");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user =  firebaseAuth.getCurrentUser();

        if (user != null){
            finish();
            startActivity( new Intent(MainActivity.this, SecondActivity.class));
        }

        Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        mTvUserReg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PasswordActivity.class));
            }
        });

    }

    private void validate(String userName, String userPassword){

        progressDialog.setMessage("You can leave a comment about my app.Enjoy your meal!!");
        progressDialog.show();
//        if ((userName.equals("Futuristic")) && (userPassword.equals("12345678"))){
//            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
//            startActivity(intent);
//        } else {
//            counter --;
//
//            Info.setText("No of attempts remaining: " + String.valueOf(counter));
//
//            if (counter == 0){
//                Login.setEnabled(false);
//            }
//
//        }
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   progressDialog.dismiss();
                   //Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                   //startActivity( new Intent(MainActivity.this, SecondActivity.class));
                   checkEmailVerification();
               } else {
                   Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                   counter--;
                   Info.setText("No of attempts remaining: " + counter);
                   progressDialog.dismiss();
                   if (counter == 0){
                       Login.setEnabled(false);
                   }
               }
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if (emailflag){
            finish();
            startActivity( new Intent(getApplicationContext(),SecondActivity.class));
        } else {
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


}