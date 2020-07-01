package com.harshit.indianstore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;


class AllProductFirestoreAdapter extends FirestorePagingAdapter<ProductsModel , AllProductFirestoreAdapter.ProductViewHolder> {

    OnListItemClick onListItemClick;


    public AllProductFirestoreAdapter(@NonNull FirestorePagingOptions<ProductsModel> options , OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductsModel model) {
        holder.listName.setText(model.getName());

        if(model.getDescription().length() >= 9)
            holder.listDescription.setText(model.getDescription().substring(0 , 8) + "...");
        else
            holder.listDescription.setText(model.getDescription());

        holder.listPrice.setText(model.getPrice());
        Picasso.get().load(model.getImage()).into(holder.listImage);
//        Log.d("my code" , model.getImage());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single , parent , false);
        return new ProductViewHolder(view);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView listName;
        TextView listDescription;
        TextView listPrice;
        ImageView listImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_item_name);
            listDescription = itemView.findViewById(R.id.list_item_description);
            listPrice = itemView.findViewById(R.id.list_item_price);
            listImage = itemView.findViewById(R.id.list_item_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onListItemClick != null){
                        onListItemClick.onItemClick(getCurrentList().get(position) , position);
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
