package com.harshit.indianstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShopView extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirestoreRecyclerAdapter adapter;

    Toolbar toolbar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        toolbar = findViewById(R.id.viewShopToolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.viewShopRecyclerView);

        //Query
        Query query = firebaseFirestore.collection("shop")
                .document(mUser.getUid()).collection("shop_items");


//        recycler Options
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query , ProductsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, ProductViewHolder>(options) {
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single , parent , false);
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductsModel model) {
                holder.listName.setText(model.getName());
                holder.listDescription.setText(model.getDescription());
                holder.listPrice.setText(model.getPrice());
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.viewShopAddNew:{
                startActivity(new Intent(ShopView.this , AddNewProduct.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_shop_items , menu);
        return true;

    }

    private class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView listName;
        TextView listDescription;
        TextView listPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_item_name);
            listDescription = itemView.findViewById(R.id.list_item_description);
            listPrice = itemView.findViewById(R.id.list_item_price);
        }
    }

}