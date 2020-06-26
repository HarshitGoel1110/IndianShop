package com.harshit.indianstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddNewItem extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;


    TextInputLayout mPrice , mDesc , mName;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddNewItem() {
        // Required empty public constructor
    }

    public static AddNewItem newInstance(String param1, String param2) {
        AddNewItem fragment = new AddNewItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                add();
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mName = view.findViewById(R.id.addNewname);
        mDesc = view.findViewById(R.id.addNewdesc);
        mPrice = view.findViewById(R.id.addNewprice);

        Button button = view.findViewById(R.id.addNewbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFirebase();
            }
        });

        return view;
    }
    public void addToFirebase() {
        String name = "" , desc = "" , price = "";


        name = mName.getEditText().getText().toString();
        desc = mDesc.getEditText().getText().toString();
        price = mPrice.getEditText().getText().toString();

        if(name.isEmpty() || desc.isEmpty() || price.isEmpty()){
            Toast.makeText(getContext(), "please fill all the entries", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String , Object> m = new HashMap<>();
        m.put("name" , name);
        m.put("description",  desc);
        m.put("price" , price);

        firebaseFirestore.collection("shop").document(mUser.getUid()).collection("product").add(m)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(getContext(), "added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}