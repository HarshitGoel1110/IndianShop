package com.harshit.indianstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;

public class UserBought extends Fragment implements UserProductHistoryFirestoreAdapter.OnListItemClick {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirestorePagingAdapter adapter;

    RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UserBought() {
        // Required empty public constructor
    }

    public static UserBought newInstance(String param1, String param2) {
        UserBought fragment = new UserBought();
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

        View view = inflater.inflate(R.layout.fragment_user_bought, container, false);

        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.userBoughtRecyclerView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Query
        Query query = firebaseFirestore.collection("users")
                .document(mUser.getUid()).collection("bought").orderBy("timestamp" , Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .build();



//        recycler Options
        FirestorePagingOptions<ProductHistoryModel> options = new FirestorePagingOptions.Builder<ProductHistoryModel>()
                .setQuery(query , config , ProductHistoryModel.class)
                .build();

        adapter = new UserProductHistoryFirestoreAdapter(options , this , getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
        HashMap<String , ArrayList<Object>> product = (HashMap<String , ArrayList<Object>>)snapshot.get("product");
        DisplayBillToTheShop displayBillToTheShop = new DisplayBillToTheShop();
        Bundle bundle = new Bundle();
        bundle.putBoolean("byShop" , false);
        bundle.putSerializable("bill" , product);
        bundle.putString("userId" , mUser.getUid());
        bundle.putString("shopId" , snapshot.getString("shop"));
        bundle.putString("documentId" , snapshot.getId());
        bundle.putBoolean("isDelivered" , snapshot.getBoolean("delivered"));
        displayBillToTheShop.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.homeActivityFrame , displayBillToTheShop , null).addToBackStack(null).commit();
    }
}