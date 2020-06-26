package com.harshit.indianstore.waste;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harshit.indianstore.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

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