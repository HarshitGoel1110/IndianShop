package com.harshit.indianstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SimpleAllUserDetails extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    TextInputLayout mName , mNumber;
    String name = "" , number = "";

    Button button;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_all_user_details);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mName = findViewById(R.id.allUserName);
        mNumber = findViewById(R.id.allUserPhno);
        button = findViewById(R.id.allUserButton);
        progressBar = findViewById(R.id.simpleAllUserProgressBar);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNew();
            }
        });

    }

    public void createNew(){
        name = mName.getEditText().getText().toString();
        number = mNumber.getEditText().getText().toString();

        name = name.trim();
        number = number.trim();


        if(name.isEmpty() || number.isEmpty()){
            Toast.makeText(this, "Please fill all the entries...", Toast.LENGTH_SHORT).show();
            return;
        }

        if(number.length() != 10){
            Toast.makeText(this, "Please enter the phone number correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        setDisable();

        HashMap<String , Object> m = new HashMap<>();
        m.put("name" , name);
        m.put("mobile" , number);
        m.put("shop" , false);

        firebaseFirestore.collection("users").document(mUser.getUid()).set(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SimpleAllUserDetails.this, "Details received successfully...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SimpleAllUserDetails.this , HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setEnable();
                        Toast.makeText(SimpleAllUserDetails.this, "Some error occurred , please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setEnable(){
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void setDisable(){
        button.setEnabled(false);
        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

}