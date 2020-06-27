package com.harshit.indianstore;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;

public class MyCartView extends Fragment {

    DatabaseHelper db;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

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

        String a = "";

        Cursor cursor = db.getAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Your cart is empty...", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        HashMap<String , HashMap<String , Combine>> data = new HashMap<>();
        while(cursor.moveToNext()){
            String shopId = cursor.getString(2);
            String productId = cursor.getString(1);
            String name = cursor.getString(3);
            int quantity = 1;
            int price = cursor.getInt(4);

            if(!data.containsKey(shopId)) {
                HashMap<String , Combine> h = new HashMap<>();
                h.put(productId , new Combine(name , price , quantity));
                data.put(shopId , h);
            }

            else if(!(data.get(shopId)).containsKey(productId)){
                data.get(shopId).put(productId , new Combine(name , price , quantity));
            }

            a = a + cursor.getString(3) + " " + cursor.getString(1) + "\n";


        }

        RecyclerOuterCartAdapter adapter = new RecyclerOuterCartAdapter(getContext() , data);
        recyclerView.setAdapter(adapter);

        Toast.makeText(getContext(), a, Toast.LENGTH_LONG).show();

    }

    class Combine{
        String name;
        int quantity;
        int price;

        public Combine(String name, int price , int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }

}