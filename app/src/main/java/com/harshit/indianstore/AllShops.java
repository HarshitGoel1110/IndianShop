package com.harshit.indianstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AllShops extends Fragment implements AllShopFirestoreAdapter.OnListItemClick {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerView;
    AllShopFirestoreAdapter adapter;

    FragmentManager fragmentManager;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AllShops() {
        // Required empty public constructor
    }

    public static AllShops newInstance(String param1, String param2) {
        AllShops fragment = new AllShops();
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

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Query
        Query query = firebaseFirestore.collection("shop");


//        recycler Options
        FirestoreRecyclerOptions<AllShopsModel> options = new FirestoreRecyclerOptions.Builder<AllShopsModel>()
                .setQuery(query , AllShopsModel.class)
                .build();

        adapter = new AllShopFirestoreAdapter(options , this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_shops, container, false);

        recyclerView = view.findViewById(R.id.allShopsRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

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
    public void onDestroy() {
        super.onDestroy();
        fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(null , FragmentManager.POP_BACK_STACK_INCLUSIVE);
        DatabaseHelper db = new DatabaseHelper(getContext());
        System.exit(0);
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        Toast.makeText(getContext(), documentSnapshot.getId(), Toast.LENGTH_SHORT).show();
        fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("shop uid" , documentSnapshot.getId());
        DisplayProductToUser displayProductToUser = new DisplayProductToUser();
        displayProductToUser.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.homeActivityFrame , displayProductToUser , null).addToBackStack(null).commit();
        Toast.makeText(getContext(), "working", Toast.LENGTH_SHORT).show();
    }
}