package com.harshit.indianstore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;



class AllProductFirestoreAdapter extends FirestoreRecyclerAdapter<ProductsModel , AllProductFirestoreAdapter.ProductViewHolder> {

    OnListItemClick onListItemClick;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public AllProductFirestoreAdapter(@NonNull FirestoreRecyclerOptions<ProductsModel> options , OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductsModel model) {
        holder.listName.setText(model.getName());
        holder.listDescription.setText(model.getDescription());
        holder.listPrice.setText(model.getPrice());
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

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_item_name);
            listDescription = itemView.findViewById(R.id.list_item_description);
            listPrice = itemView.findViewById(R.id.list_item_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onListItemClick != null){
                        onListItemClick.onItemClick(getSnapshots().getSnapshot(position) , position);
                    }
                }
            });

        }

//        @Override
//        public void onClick(View view) {
//
//        }

    }

    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }

    public void setOnListItemClick(OnListItemClick listItemClick){
        this.onListItemClick = listItemClick;
    }

}
