package com.example.recipe_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPassword;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userName = (EditText) findViewById(R.id.edt_username);
        userEmail = (EditText) findViewById(R.id.edt_useremail);
        userPassword = (EditText)findViewById(R.id.edt_password);
        regButton = (Button) findViewById(R.id.btn_register);
        userLogin = (TextView)findViewById(R.id.tv_userlogin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        regButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){

                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                sendEmaailVerification();
                                //Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });




    }

    private Boolean validate(){
        Boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "please enter all the deatils", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    private void sendEmaailVerification(){
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegistrationActivity.this, "Successfully Registered, Verification email is sent!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity( new Intent(RegistrationActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Verification email is not sent! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}