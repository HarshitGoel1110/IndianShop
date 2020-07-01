package com.harshit.indianstore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DisplayProductLast extends Fragment {

    DatabaseHelper databaseHelper;
    String shopId = "";
    String shopName = "";
    String productId = "";
    String desc = "";
    String name = "";
    String image = "";
    int price = 0;

    Button button;
    TextView mProductName;
    TextView mProductDescription;
    TextView mProductPrice;
    ImageView mProductImage;

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
        shopName = bundle.getString("shopName");

        productId = bundle.getString("productId");
        desc = bundle.getString("desc");
        name = bundle.getString("name");
        price = bundle.getInt("price");
        image = bundle.getString("image");

        databaseHelper = new DatabaseHelper(getContext());

        button = view.findViewById(R.id.displayLastButton);
        mProductImage = view.findViewById(R.id.displayLastImage);
        mProductName = view.findViewById(R.id.displayLastName);
        mProductDescription = view.findViewById(R.id.displayLastDesc);
        mProductPrice = view.findViewById(R.id.displayLastPrice);

        Picasso.get().load(image).fit().centerCrop().into(mProductImage);
        mProductName.setText(name);
        mProductDescription.setText(desc);
        mProductPrice.setText("Rs " + price);

        if(databaseHelper.isPresentAlready(productId)){
//            button.setEnabled(false);
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
        DatabaseHelper db = new DatabaseHelper(getContext());
        if(!db.getCount()){
            Toast.makeText(getContext(), "Can insert at most 20 items in the cart", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isInserted = databaseHelper.insertData(productId , shopId , shopName);
        if(isInserted){
//            button.setEnabled(false);
            Toast.makeText(getContext(), "Added Successfully \n You can adjust the quantity directly from the cart", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "Unable to add to cart...", Toast.LENGTH_SHORT).show();
        }
    }
}