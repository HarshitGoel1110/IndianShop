package com.harshit.indianstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class PurchasedThroughOurShop extends Fragment implements ShopProductHistoryFirestoreAdapter.OnListItemClick {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser mUser;

    RecyclerView recyclerView;

    FirestorePagingAdapter adapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PurchasedThroughOurShop() {
        // Required empty public constructor
    }

    public static PurchasedThroughOurShop newInstance(String param1, String param2) {
        PurchasedThroughOurShop fragment = new PurchasedThroughOurShop();
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

        View view = inflater.inflate(R.layout.fragment_purchased_through_our_shop , container , false);

        recyclerView = view.findViewById(R.id.purchasedThroughOurShopRecyclerView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Query
        Query query = firebaseFirestore.collection("shop")
                .document(mUser.getUid()).collection("purchased").orderBy("timestamp" , Query.Direction.DESCENDING);


        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(5).build();

//        recycler Options

        FirestorePagingOptions<ShopHistoryModel> options = new FirestorePagingOptions.Builder<ShopHistoryModel>()
                .setQuery(query , config , ShopHistoryModel.class)
                .build();

        adapter = new ShopProductHistoryFirestoreAdapter(options , this , getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        HashMap<String , ArrayList<Object>> product = (HashMap<String , ArrayList<Object>>)snapshot.get("product");
        DisplayBillToTheShop displayBillToTheShop = new DisplayBillToTheShop();
        Bundle bundle = new Bundle();
        bundle.putBoolean("byShop" , true);
        bundle.putSerializable("bill" , product);
        bundle.putString("shopId" , mUser.getUid());
        bundle.putString("userId" , snapshot.getString("user"));
        bundle.putString("documentId" , snapshot.getId());
        bundle.putBoolean("isDelivered" , snapshot.getBoolean("delivered"));
        displayBillToTheShop.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.homeActivityFrame , displayBillToTheShop , null).addToBackStack(null).commit();
    }
}