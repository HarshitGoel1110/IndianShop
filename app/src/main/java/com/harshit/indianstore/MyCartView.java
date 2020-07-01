package com.harshit.indianstore;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;

public class MyCartView extends Fragment {

    DatabaseHelper db;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    final HashMap<String , String> shopIdToShopName = new HashMap<>();

    final HashMap<String , HashMap<String , Combine>> data = new HashMap<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyCartView() {
        // Required empty public constructor
    }

    public static MyCartView newInstance(String param1, String param2) {
        MyCartView fragment = new MyCartView();
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
        View view = inflater.inflate(R.layout.fragment_my_cart_view, container, false);

        db = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.myCartRecyclerFirst);
        linearLayoutManager = new LinearLayoutManager(getContext());
        display();

        return view;
    }

    private void display() {

        Cursor cursor = db.getAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Your cart is empty...", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        final RecyclerOuterCartAdapter adapter = new RecyclerOuterCartAdapter(getContext() , data , shopIdToShopName);
        recyclerView.setAdapter(adapter);

        while(cursor.moveToNext()){
            final String shopId = cursor.getString(2);
            final String productId = cursor.getString(1);
            final String shopName = cursor.getString(3);

            final String[] name = new String[1];
            final int quantity = 1;
            final int[] price = {0};
            final String[] image = {""};

            firebaseFirestore.collection("shop").document(shopId).collection("product").document(productId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()) {
                                Toast.makeText(getContext(), shopId, Toast.LENGTH_SHORT).show();
                                name[0] = documentSnapshot.getString("name");
                                price[0] = Integer.parseInt(documentSnapshot.getString("price"));
                                image[0] = documentSnapshot.getString("image");

                                shopIdToShopName.put(shopId , shopName);

                                if(!data.containsKey(shopId)) {
                                    HashMap<String , Combine> h = new HashMap<>();
                                    h.put(productId , new Combine(name[0], price[0], quantity , image[0], shopId));
                                    data.put(shopId , h);
                                }

                                else if(!(data.get(shopId)).containsKey(productId)){
                                    data.get(shopId).put(productId , new Combine(name[0], price[0], quantity , image[0], shopId));
                                }
                                adapter.notifyDataSetChanged();

                            }
                            else{
                                Toast.makeText(getContext(), "Document is empty", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });

//            String name = cursor.getString(3);
//            int quantity = 1;
//            int price = cursor.getInt(4);
//            String image = cursor.getString(6);


//            a = a + cursor.getString(3) + " " + cursor.getString(1) + "\n";



        }

    }

    class task extends AsyncTask<Cursor , Void , Boolean>{


        @Override
        protected Boolean doInBackground(Cursor... cursors) {

            Cursor cursor = cursors[0];

            final HashMap<String , String> shopIdToShopName = new HashMap<>();

            final HashMap<String , HashMap<String , Combine>> data = new HashMap<>();
            while(cursor.moveToNext()){
                final String shopId = cursor.getString(2);
                final String productId = cursor.getString(1);
                final String shopName = cursor.getString(3);

                final String[] name = new String[1];
                final int quantity = 1;
                final int[] price = {0};
                final String[] image = {""};

                firebaseFirestore.collection("shop").document(shopId).collection("product").document(productId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()) {
                                    Toast.makeText(getContext(), shopId, Toast.LENGTH_SHORT).show();
                                    name[0] = documentSnapshot.getString("name");
                                    price[0] = Integer.parseInt(documentSnapshot.getString("price"));
                                    image[0] = documentSnapshot.getString("image");

                                    shopIdToShopName.put(shopId , shopName);

                                    if(!data.containsKey(shopId)) {
                                        HashMap<String , Combine> h = new HashMap<>();
                                        h.put(productId , new Combine(name[0], price[0], quantity , image[0], shopId));
                                        data.put(shopId , h);
                                    }

                                    else if(!(data.get(shopId)).containsKey(productId)){
                                        data.get(shopId).put(productId , new Combine(name[0], price[0], quantity , image[0], shopId));
                                    }

                                }
                                else{
                                    Toast.makeText(getContext(), "Document is empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
            return null;
        }
    }

    class Combine{
        String name;
        int quantity;
        int price;
        String image;
        String shopId = "";

        public Combine(String name, int price , int quantity , String image , String shopId) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.image = image;
            this.shopId = shopId;
        }
    }

}