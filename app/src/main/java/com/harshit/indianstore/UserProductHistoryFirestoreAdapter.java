package com.harshit.indianstore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

class UserProductHistoryFirestoreAdapter extends FirestorePagingAdapter<ProductHistoryModel , UserProductHistoryFirestoreAdapter.UserProductHistoryViewHolder> {

    OnListItemClick onListItemClick;
    Context context;

    public UserProductHistoryFirestoreAdapter(@NonNull FirestorePagingOptions<ProductHistoryModel> options , OnListItemClick onListItemClick , Context context) {
        super(options);
        this.onListItemClick = onListItemClick;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final UserProductHistoryViewHolder holder, int position, @NonNull ProductHistoryModel model) {
        String shopId = model.getShop();

        FirebaseFirestore.getInstance().collection("shop").document(shopId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.shopName.setText(documentSnapshot.getString("name") + " " + documentSnapshot.getString("mobile"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        if(model.getDelivered())
            holder.delivered.setText("Delivered");
        else
            holder.delivered.setText("Not Delivered");
        holder.product = model.product;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(model.timestamp)));
        holder.date.setText(dateString);

    }

    @NonNull
    @Override
    public UserProductHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_bought_purchased_list_view , parent , false);
        return new UserProductHistoryViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state){
            case LOADED:{
                break;
            }
            case ERROR:{
                Log.d("error" , "error");
                break;
            }
            case FINISHED:{
                Log.d("finished" , "all data finished");
                break;
            }
            case LOADING_MORE:{
                Log.d("loading next page" , "loading next page");
                break;
            }
            case LOADING_INITIAL:{
                Log.d("initial" , "first time load");
                break;
            }
        }
    }

    class UserProductHistoryViewHolder extends RecyclerView.ViewHolder{

        TextView shopName;
        HashMap<String , ArrayList<Object>> product;
        TextView delivered;
        TextView date;

        public UserProductHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.productBoughtListViewName);
            date = itemView.findViewById(R.id.productBoughtListViewDate);
            delivered = itemView.findViewById(R.id.productBoughtListViewDelivered);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onListItemClick != null){
                        onListItemClick.onItemClick(getCurrentList().get(getAdapterPosition()) , getAdapterPosition());
                    }
                }
            });

        }
    }

    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }

    public void setOnListItemClick(OnListItemClick listItemClick){
        this.onListItemClick = listItemClick;
    }

}
