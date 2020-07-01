package com.harshit.indianstore;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayBillToTheShop extends Fragment {

    HashMap<String , ArrayList<Object>> product;
    Boolean shop = false;

    Button button;
    String userId = "";
    String shopId = "";
    Boolean isDelivered = false;
    String documentId = "";

    TableLayout tableLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DisplayBillToTheShop() {
        // Required empty public constructor
    }

    public static DisplayBillToTheShop newInstance(String param1, String param2) {
        DisplayBillToTheShop fragment = new DisplayBillToTheShop();
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
        View view = inflater.inflate(R.layout.fragment_display_bill_to_the_shop, container, false);

//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tableLayout = view.findViewById(R.id.displayBillTableLayout);

        Bundle bundle = getArguments();
        if(bundle.getSerializable("bill") != null)
            product = (HashMap<String, ArrayList<Object>>)bundle.getSerializable("bill");

        shop = bundle.getBoolean("byShop");
        userId = bundle.getString("userId");
        shopId = bundle.getString("shopId");
        documentId = bundle.getString("documentId");
        isDelivered = bundle.getBoolean("isDelivered");

        if(shop){
            button = view.findViewById(R.id.updateDelivered);
            button.setVisibility(View.VISIBLE);
            if(isDelivered){
                button.setText("Delivered");
                button.setEnabled(false);
            }
            else{
                button.setText("Change To deliver");
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    update();
                }
            });
        }


        display();

        // Inflate the layout for this fragment
        return view;
    }

    private void update() {

        final HashMap<String , Object> m = new HashMap<>();
        m.put("delivered" , true);

        FirebaseFirestore.getInstance().collection("shop").document(shopId).collection("purchased")
                .document(documentId).update(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseFirestore.getInstance().collection("users").document(userId).collection("bought")
                        .document(documentId).update(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "updated Successfully", Toast.LENGTH_SHORT).show();
                        button.setText("Delivered");
                        button.setEnabled(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ResourceType")
    private void display() {
        if(product == null){
            Toast.makeText(getContext(), "Unable to process please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        TableRow tr_head = new TableRow(getContext());
        tr_head.setId(1);    // part1
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));

        TextView label_hello = new TextView(getContext());
        label_hello.setId(2);
        label_hello.setText("Name");
        label_hello.setTextColor(Color.BLACK);
        label_hello.setGravity(View.TEXT_ALIGNMENT_CENTER);
        label_hello.setPadding(20 , 20 , 20 , 0);
        label_hello.setTextSize(15);
        tr_head.addView(label_hello);

        TextView label_android = new TextView(getContext());
        label_android.setId(3);
        label_android.setText("Quantity");
        label_android.setTextColor(Color.BLACK);
        label_android.setGravity(View.TEXT_ALIGNMENT_CENTER);
        label_android.setPadding(20 , 20 , 20 , 0);
        label_android.setTextSize(15);
        tr_head.addView(label_android);

        TextView label_Price = new TextView(getContext());
        label_Price.setId(4);
        label_Price.setText("Price");
        label_Price.setTextColor(Color.BLACK);
        label_Price.setGravity(View.TEXT_ALIGNMENT_CENTER);
        label_Price.setPadding(20 , 20 , 20 , 0);
        label_Price.setTextSize(15);
        tr_head.addView(label_Price);

        TextView label_Total = new TextView(getContext());
        label_Total.setId(5);
        label_Total.setText("Total");
        label_Total.setTextColor(Color.BLACK);
        label_Total.setGravity(View.TEXT_ALIGNMENT_CENTER);
        label_Total.setPadding(20 , 20 , 20 , 20);
        label_Total.setTextSize(15);
        tr_head.addView(label_Total);

        tableLayout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));

        int count = 11;
        long totalPrice = 0;

        for(String i:product.keySet()){
            tr_head = new TableRow(getContext());
            tr_head.setId(++count);
            tr_head.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));

            TextView name = new TextView(getContext());
            name.setId(++count);
            name.setText(product.get(i).get(0).toString());
            name.setTextColor(R.color.black);
            name.setGravity(View.TEXT_ALIGNMENT_CENTER);
            name.setTextSize(10);
            name.setPadding(20 , 20 , 20 , 0);
            tr_head.addView(name);

            TextView quantity = new TextView(getContext());
            quantity.setId(++count);
            quantity.setText(product.get(i).get(1).toString());
            quantity.setTextColor(R.color.black);
            quantity.setGravity(View.TEXT_ALIGNMENT_CENTER);
            quantity.setTextSize(10);
            quantity.setPadding(20 , 20 , 20 , 0);
            tr_head.addView(quantity);

            TextView price = new TextView(getContext());
            price.setId(++count);
            price.setText(product.get(i).get(2).toString());
            price.setTextColor(R.color.black);
            price.setGravity(View.TEXT_ALIGNMENT_CENTER);
            price.setTextSize(10);
            price.setPadding(20 , 20 , 20 , 0);
            tr_head.addView(price);

            TextView total = new TextView(getContext());
            total.setId(++count);
            total.setText(Long.toString((long)product.get(i).get(1)*(long)product.get(i).get(2)));
            total.setTextColor(R.color.black);
            total.setGravity(View.TEXT_ALIGNMENT_CENTER);
            total.setTextSize(10);
            total.setPadding(20 , 20 , 20 , 20);
            tr_head.addView(total);

            totalPrice += (long)product.get(i).get(1)*(long)product.get(i).get(2);

            tableLayout.addView(tr_head, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                    TableLayout.LayoutParams.MATCH_PARENT));
        }

        tr_head = new TableRow(getContext());
        tr_head.setId(++count);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));

        TextView total = new TextView(getContext());
        total.setId(++count);
        total.setText("Grand Total");
        total.setTextColor(R.color.black);
        total.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        total.setTextSize(20);
        total.setPadding(20 , 20 , 20 , 20);
        tr_head.addView(total);

        TextView totalPricetext = new TextView(getContext());
        totalPricetext.setId(++count);
        totalPricetext.setText(Long.toString(totalPrice));
        totalPricetext.setTextColor(R.color.black);
        totalPricetext.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        totalPricetext.setTextSize(20);
        totalPricetext.setPadding(20 , 20 , 20 , 20);
        tr_head.addView(totalPricetext);

        tableLayout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                TableLayout.LayoutParams.MATCH_PARENT));

    }
}