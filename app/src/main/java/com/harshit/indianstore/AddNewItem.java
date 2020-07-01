package com.harshit.indianstore;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class AddNewItem extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;


    TextInputLayout mPrice , mDesc , mName;
    ImageButton mImage;

    Button button;
    ProgressBar progressBar;

    Uri imageLocalUri;
    String imageFirestoreUri;
    StorageReference storageReference;
    final int IMAGE_REQUEST_CODE = 7;


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("images");

        mName = view.findViewById(R.id.addNewname);
        mDesc = view.findViewById(R.id.addNewdesc);
        mPrice = view.findViewById(R.id.addNewprice);
        mImage = view.findViewById(R.id.addNewItemsImage);

        progressBar = view.findViewById(R.id.addItemProgress);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        button = view.findViewById(R.id.addNewbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent , "Select the product image") , IMAGE_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageLocalUri = data.getData();
            Picasso.get().load(imageLocalUri).into(mImage);
            mImage.setImageURI(imageLocalUri);
        }
    }


    public String fileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(){
        if(imageLocalUri == null){
            Toast.makeText(getContext(), "Please select the image...", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = "" , desc = "" , price = "";


        name = mName.getEditText().getText().toString().trim();
        desc = mDesc.getEditText().getText().toString().trim();
        price = mPrice.getEditText().getText().toString().trim();

        if(name.isEmpty() || desc.isEmpty() || price.isEmpty()){
            Toast.makeText(getContext(), "please fill all the entries", Toast.LENGTH_SHORT).show();
            return;
        }

        setDisable();

        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + fileExtension(imageLocalUri));

        reference.putFile(imageLocalUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageFirestoreUri = uri.toString();
                        addToFirebase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setEnable();
                        Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setEnable();
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addToFirebase() {

        String name = "" , desc = "" , price = "";


        name = mName.getEditText().getText().toString().trim();
        desc = mDesc.getEditText().getText().toString().trim();
        price = mPrice.getEditText().getText().toString().trim();

        // this is already done in the upload image function
//        if(name.isEmpty() || desc.isEmpty() || price.isEmpty()){
//            Toast.makeText(getContext(), "please fill all the entries", Toast.LENGTH_SHORT).show();
//            return;
//        }

        HashMap<String , Object> m = new HashMap<>();
        m.put("name" , name);
        m.put("description",  desc);
        m.put("price" , price);
        m.put("image" , imageFirestoreUri);

        firebaseFirestore.collection("shop").document(mUser.getUid()).collection("product").add(m)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(getContext(), "added successfully", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.homeActivityFrame , new ViewShop() , null).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setEnable();
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
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