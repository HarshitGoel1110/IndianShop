package com.harshit.indianstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class DisplayProductLast extends Fragment {

    DatabaseHelper databaseHelper;
    String shopId = "";
    String productId = "";
    String desc = "";
    String name = "";
    int price = 0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DisplayProductLast() {
        // Required empty public constructor
    }

    public static DisplayProductLast newInstance(String param1, String param2) {
        DisplayProductLast fragment = new DisplayProductLast();
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
        View view = inflater.inflate(R.layout.fragment_display_product_last, container, false);

        Bundle bundle = getArguments();
        shopId = bundle.getString("shopId");
        productId = bundle.getString("productId");
        desc = bundle.getString("desc");
        name = bundle.getString("name");
        price = bundle.getInt("price");

        databaseHelper = new DatabaseHelper(getContext());

        Button button = view.findViewById(R.id.displayLastButton);

        if(databaseHelper.isPresentAlready(productId)){
            button.setEnabled(false);
            Toast.makeText(getContext(), "Already present in your cart...", Toast.LENGTH_LONG).show();
        }

        else{
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCart();
                }
            });
        }

        return view;
    }

    private void addToCart() {
        boolean isInserted = databaseHelper.insertData(productId , shopId ,
                name , price);
        if(isInserted){
            Toast.makeText(getContext(), "Added Successfully \n You can adjust the quantity directly from the cart", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "Unable to add to cart...", Toast.LENGTH_SHORT).show();
        }
    }
}