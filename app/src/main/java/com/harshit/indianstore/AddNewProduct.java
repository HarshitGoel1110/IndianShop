package com.harshit.indianstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddNewProduct extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser mUser;

    EditText mName , mPrice , mDescription;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        mName = findViewById(R.id.addNewProductName);
        mPrice = findViewById(R.id.addNewProductPrice);
        mDescription = findViewById(R.id.addNewProductDescription);
        button = findViewById(R.id.addNewProductButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

    }

    private void add() {
        String name = mName.getText().toString();
        String price = mPrice.getText().toString();
        String description = mDescription.getText().toString();

        if(name.isEmpty() || price.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please fill all the entries", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String , String> m = new HashMap<>();
        m.put("description" , description);
        m.put("price" , price);
        m.put("name" , name);

        firebaseFirestore.collection("shop")
                .document(mUser.getUid()).collection("shop_items")
                .add(m)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddNewProduct.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewProduct.this, "Some error has Occurred", Toast.LENGTH_SHORT).show();
            }
        });


    }
}