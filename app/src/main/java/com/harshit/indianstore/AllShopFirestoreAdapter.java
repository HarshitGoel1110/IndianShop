package com.harshit.indianstore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;

class AllShopFirestoreAdapter extends FirestorePagingAdapter<AllShopsModel , AllShopFirestoreAdapter.AllShopViewHolder> {

    OnListItemClick onListItemClick;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllShopFirestoreAdapter(@NonNull FirestorePagingOptions<AllShopsModel> options , OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull AllShopViewHolder holder, int position, @NonNull AllShopsModel model) {
        holder.mName.setText(model.getName());
        holder.mAddress.setText(model.getAddress());
        holder.mPhone.setText(model.getMobile());

        String name[] = holder.mName.getText().toString().split(" ");
        if(name.length == 1){
            if(name[0].length() == 1)
                holder.image.setText(Character.toUpperCase(name[0].charAt(0)));
            else{
                String a = name[0].substring(0 , 2);
                holder.image.setText(a.toUpperCase());
            }
        }
        else{
            String a = name[0].charAt(0) + "" + name[1].charAt(0);
            holder.image.setText(a.toUpperCase());
        }

    }

    @NonNull
    @Override
    public AllShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_shop_list_item , parent , false);
        return new AllShopViewHolder(view);
    }

    public class AllShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView image;
        TextView mName;
        TextView mAddress;
        TextView mPhone;

        public AllShopViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.allShopListName);
            mAddress = itemView.findViewById(R.id.allShopListAddress);
            image = itemView.findViewById(R.id.allShopImage);
            mPhone = itemView.findViewById(R.id.allShopListPhone);

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
