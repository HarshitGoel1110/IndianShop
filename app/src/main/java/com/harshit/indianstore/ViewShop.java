package com.harshit.indianstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewShop extends Fragment implements AllProductFirestoreAdapter.OnListItemClick {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirestorePagingAdapter adapter;
    FloatingActionButton button;

    RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ViewShop() {
        // Required empty public constructor
    }

    public static ViewShop newInstance(String param1, String param2) {
        ViewShop fragment = new ViewShop();
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
        Query query = firebaseFirestore.collection("shop")
                .document(mUser.getUid()).collection("product");

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(8)
                .setPageSize(3)
                .build();

//        recycler Options
        FirestorePagingOptions<ProductsModel> options = new FirestorePagingOptions.Builder<ProductsModel>()
                .setQuery(query , config , ProductsModel.class)
                .build();

        adapter = new AllProductFirestoreAdapter(options , this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_shop, container, false);

        // Inflate the layout for this fragment
        button = view.findViewById(R.id.viewShopfloatingActionButton);

        recyclerView = view.findViewById(R.id.viewShopRecyclerView);
        recyclerView.setAdapter(adapter);

//        if(adapter.getItemCount() == 0){
//            Toast.makeText(getContext(), "Please add some items into your shop...", Toast.LENGTH_SHORT).show();
//        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        button.setImageResource(R.drawable.ic_baseline_add_24);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNew();
            }
        });

        return view;
    }


    public void addNew(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.homeActivityFrame , new AddNewItem() , null).addToBackStack(null).commit();
        Toast.makeText(getContext(), "working", Toast.LENGTH_SHORT).show();
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

    }

    //the holder class for viewing the products in our shop
//    private class ProductViewHolder extends RecyclerView.ViewHolder {
//
//        TextView listName;
//        TextView listDescription;
//        TextView listPrice;
//
//        public ProductViewHolder(@NonNull View itemView) {
//            super(itemView);
//            listName = itemView.findViewById(R.id.list_item_name);
//            listDescription = itemView.findViewById(R.id.list_item_description);
//            listPrice = itemView.findViewById(R.id.list_item_price);
//        }
//    }

}