package com.harshit.indianstore;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

class RecyclerInnerCartAdapter extends RecyclerView.Adapter<RecyclerInnerCartAdapter.CartInnerViewHolder> {

    Context context;
    HashMap<String , MyCartView.Combine> innerData;

    public RecyclerInnerCartAdapter(Context context, HashMap<String, MyCartView.Combine> innerData) {
        this.context = context;
        this.innerData = innerData;
    }

    @NonNull
    @Override
    public CartInnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_inner , parent , false);
        return new CartInnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartInnerViewHolder holder, int position) {
        String name = "";
        int price = 0;
        int count = 0;
        for(String i:innerData.keySet()){
            if(count == position){
                name = innerData.get(i).name;
                price = innerData.get(i).price;
                break;
            }
            count++;
        }
        holder.productName.setText(name);
        holder.price.setText(Integer.toString(price));
    }

    @Override
    public int getItemCount() {
        return innerData.size();
    }

    public class CartInnerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView productName;
        TextView price;
        EditText quantity;

        public CartInnerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cartInnerImage);
            productName = itemView.findViewById(R.id.cartInnerProductName);
            price = itemView.findViewById(R.id.cartInnerPrice);
            quantity = itemView.findViewById(R.id.cartInnerQuantity);

            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String text = charSequence.toString();
                    int value = 0;
                    if(!text.isEmpty()){
                        value = Integer.parseInt(text);
                    }
                    updateValues(getAdapterPosition() , value);
                    Toast.makeText(context, "value changed to " + value, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "inner clicked", Toast.LENGTH_SHORT).show();
//                    updateValues(getAdapterPosition());
                }
            });

        }

        private void updateValues(int position, int value) {
            // this will update the quantity for the inner data variable and
            // due to reference the value of data in the outer class will also change automatically
            int count = 0;
            for(String i:innerData.keySet()){
                if(count == position){
                    innerData.get(i).quantity = value;
                    break;
                }
            }

        }
    }

}
