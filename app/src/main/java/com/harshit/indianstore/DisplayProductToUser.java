package com.harshit.indianstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DisplayProductToUser extends Fragment implements AllProductFirestoreAdapter.OnListItemClick {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirestoreRecyclerAdapter adapter;

    RecyclerView recyclerView;


    String shopId = "";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DisplayProductToUser() {
        // Required empty public constructor
    }

    public static DisplayProductToUser newInstance(String param1, String param2) {
        DisplayProductToUser fragment = new DisplayProductToUser();
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

        View view = inflater.inflate(R.layout.fragment_display_product_to_user, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Bundle bundle = this.getArguments();
        shopId = bundle.getString("shop uid");

        //Query
        Query query = firebaseFirestore.collection("shop")
                .document(shopId).collection("product");


//        recycler Options
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query , ProductsModel.class)
                .build();

        adapter = new AllProductFirestoreAdapter(options , this);

        recyclerView = view.findViewById(R.id.displayToUserRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Toast.makeText(getContext(), "stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Toast.makeText(getContext(), "selected", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("shopId" , shopId);
        bundle.putString("productId" , snapshot.getId());
        bundle.putString("desc" , snapshot.getString("description"));
        bundle.putString("name" , snapshot.getString("name"));
        bundle.putInt("price" , Integer.parseInt(snapshot.getString("price")));

        DisplayProductLast displayProductLast = new DisplayProductLast();
        displayProductLast.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.homeActivityFrame , displayProductLast , null).addToBackStack(null).commit();


    }
}