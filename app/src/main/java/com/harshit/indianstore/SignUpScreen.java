package com.harshit.indianstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpScreen extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextInputEditText mEmail , mPassword;
    Button signButton;
    ProgressBar progressBar;

    String email = "" , password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.signUpEmail);
        mPassword = findViewById(R.id.signUpPassword);
        signButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.signUpProgress);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if(!verifyAndProceed())
                    return;

                setDisable();

                mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // checking for the email verification
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        setEnable();
                                        Toast.makeText(SignUpScreen.this, "Sign up Successful , Please check your email for verification", Toast.LENGTH_LONG).show();

                                        // go to login screen
                                        Intent intent = new Intent(SignUpScreen.this , LoginScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                      finish();

                                    }
                                    else{
                                        setEnable();
                                        Toast.makeText(SignUpScreen.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else{
                            setEnable();
                            Toast.makeText(SignUpScreen.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    public void setEnable(){
        signButton.setEnabled(true);
        signButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void setDisable(){
        signButton.setEnabled(false);
        signButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public boolean verifyAndProceed(){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(SignUpScreen.this , "Please fill all the given fields" , Toast.LENGTH_SHORT).show();
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