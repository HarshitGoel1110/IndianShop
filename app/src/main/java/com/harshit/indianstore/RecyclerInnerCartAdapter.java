package com.harshit.indianstore;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

class RecyclerInnerCartAdapter extends RecyclerView.Adapter<RecyclerInnerCartAdapter.CartInnerViewHolder> {

    Context context;
    RecyclerView outerRecyclerView;
    String shopId;
    HashMap<String , HashMap<String , MyCartView.Combine>> data;
    HashMap<String , String> shopIdToShopName;

    public RecyclerInnerCartAdapter(Context context, RecyclerView outerRecyclerView
            , String shopId , HashMap<String , HashMap<String , MyCartView.Combine>> data , HashMap<String , String> shopIdToShopName) {
        this.context = context;
        this.outerRecyclerView = outerRecyclerView;
        this.shopId = shopId;
        this.data = data;
        this.shopIdToShopName = shopIdToShopName;
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
        String image = "";

        int count = 0;

        for(String i:data.get(shopId).keySet()){
            if(count == position){
                name = data.get(shopId).get(i).name;
                price = data.get(shopId).get(i).price;
                image = data.get(shopId).get(i).image;
                break;
            }
            count++;
        }
        holder.productName.setText(name);
        holder.price.setText(Integer.toString(price));
        Picasso.get().load(image).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.get(shopId).size();
    }

    public class CartInnerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView productName;
        TextView price;
        EditText quantity;
        ImageButton delete;

        public CartInnerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cartInnerImage);
            productName = itemView.findViewById(R.id.cartInnerProductName);
            price = itemView.findViewById(R.id.cartInnerPrice);
            quantity = itemView.findViewById(R.id.cartInnerQuantity);
            delete = itemView.findViewById(R.id.cartInnerDelete);

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

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteIt(getAdapterPosition());
                }
            });

        }

        private void deleteIt(int position) {
            int count = 0;
            DatabaseHelper db = new DatabaseHelper(context);
            for(String i:data.get(shopId).keySet()){
                if(count == position){
                    if(data.get(shopId).size() == 1){
                        Toast.makeText(context, "Please click the delete button available under shop to delete", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(db.deleteRowByProductId(i)) {
                        data.get(shopId).remove(i);
                        notifyItemRemoved(position);
                    }
                    else{
                        Toast.makeText(context, "Some Error occurred while deleting", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                }
                count++;
            }
            Toast.makeText(context, "Item removed Successfully", Toast.LENGTH_SHORT).show();
        }

        private void updateValues(int position, int value) {
            // this will update the quantity for the inner data variable and
            // due to reference the value of data in the outer class will also change automatically
            int count = 0;
            for(String i:data.get(shopId).keySet()){
                if(count == position){
                    data.get(shopId).get(i).quantity = value;
                    break;
                }
                count++;
            }

        }
    }

}