package com.harshit.indianstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextInputEditText mEmail;
    TextInputEditText mPassword;
    Button loginButton;
    ProgressBar progressBar;

    String email = "" , password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.loginEmail);
        mPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgress);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if(!verifyAndProceed())
                    return;

                setDisable();

                mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(mAuth.getCurrentUser().isEmailVerified()){
//                                setEnable();
                                Toast.makeText(LoginScreen.this, "Sign in successfully", Toast.LENGTH_SHORT).show();

                                // go to login screen
                                Intent intent = new Intent(LoginScreen.this , HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                setEnable();
                                Toast.makeText(LoginScreen.this, "Please verify your email first...", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            setEnable();
                            Toast.makeText(LoginScreen.this, "Error in Sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    public void setEnable(){
        loginButton.setEnabled(true);
        loginButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void setDisable(){
        loginButton.setEnabled(false);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public boolean verifyAndProceed(){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(LoginScreen.this , "Please fill all the given fields" , Toast.LENGTH_SHORT).show();
            return false;
        }
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        boolean result = email.matches(regex);
        if(result)
            return true;
        else{
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}