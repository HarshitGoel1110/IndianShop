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

class AllShopFirestoreAdapter extends FirestoreRecyclerAdapter<AllShopsModel , AllShopFirestoreAdapter.AllShopViewHolder> {

    OnListItemClick onListItemClick;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllShopFirestoreAdapter(@NonNull FirestoreRecyclerOptions<AllShopsModel> options , OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull AllShopViewHolder holder, int position, @NonNull AllShopsModel model) {
        holder.mName.setText(model.getName());
        holder.mAddress.setText(model.getAddress());
    }

    @NonNull
    @Override
    public AllShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_shop_list_item , parent , false);
        return new AllShopViewHolder(view);
    }

    public class AllShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName;
        TextView mAddress;

        public AllShopViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.allShopListName);
            mAddress = itemView.findViewById(R.id.allShopListAddress);

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


        @Override
        public void onClick(View view) {

        }
    }

    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }

    public void setOnListItemClick(OnListItemClick listItemClick){
        this.onListItemClick = listItemClick;
    }

}
