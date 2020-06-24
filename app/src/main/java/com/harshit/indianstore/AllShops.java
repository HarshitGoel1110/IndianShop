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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AllShops extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;

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

        adapter = new FirestoreRecyclerAdapter<AllShopsModel, AllShopViewHolder>(options) {
            @Override
            public AllShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_shop_list_item , parent , false);
                return new AllShopViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllShopViewHolder holder, int position, @NonNull AllShopsModel model) {
                holder.mName.setText(model.getName());
                holder.mAddress.setText(model.getAddress());
//                holder.listName.setText(model.getName());
//                holder.listDescription.setText(model.getDescription());
//                holder.listPrice.setText(model.getPrice());
            }
        };

    }

    private class AllShopViewHolder extends RecyclerView.ViewHolder{

        TextView mName;
        TextView mAddress;

        public AllShopViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.allShopListName);
            mAddress = itemView.findViewById(R.id.allShopListAddress);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_shops, container, false);

        recyclerView = view.findViewById(R.id.allShopsRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

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
        System.exit(0);
    }
}