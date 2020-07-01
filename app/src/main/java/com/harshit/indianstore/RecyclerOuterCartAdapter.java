package com.harshit.indianstore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

class RecyclerOuterCartAdapter extends RecyclerView.Adapter<RecyclerOuterCartAdapter.CartOuterViewHolder> {

    Context context;
    HashMap<String , HashMap<String , MyCartView.Combine>> data;
    HashMap<String , String> shopIdToShopName;

    public RecyclerOuterCartAdapter(Context context , HashMap<String , HashMap<String , MyCartView.Combine>> data , HashMap<String , String> shopIdToShopName){
        this.context = context;
        this.data = data;
        this.shopIdToShopName = shopIdToShopName;
    }

    @NonNull
    @Override
    public CartOuterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_outer , parent , false);
        return new CartOuterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartOuterViewHolder holder, int position) {
        String shopId = "";
        HashMap<String , MyCartView.Combine> nextData = new HashMap<>();
        int count = 0;
        for(String i:data.keySet()){
            if(count == position){
                shopId = i;
                break;
            }
            count++;
        }
        holder.shopName.setText(shopIdToShopName.get(shopId));
        RecyclerInnerCartAdapter adapter = new RecyclerInnerCartAdapter(context , holder.outerRecyclerView , shopId , data , shopIdToShopName);
        holder.outerRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class CartOuterViewHolder extends RecyclerView.ViewHolder{

        TextView shopName;
        RecyclerView outerRecyclerView;
        Button buyNow;
        Button delete;
        ProgressBar progressBar;

        public CartOuterViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.cartShopName);
            outerRecyclerView = itemView.findViewById(R.id.cartOuterRecyclerView);
            buyNow = itemView.findViewById(R.id.cartOuterBuyNow);
            delete = itemView.findViewById(R.id.cartOuterDelete);
            progressBar = itemView.findViewById(R.id.cartOuterProgress);

            Sprite wave = new Wave();
            progressBar.setIndeterminateDrawable(wave);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            outerRecyclerView.setLayoutManager(linearLayoutManager);
            outerRecyclerView.setHasFixedSize(false);

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generateAlertDialogBox(getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteIt(getAdapterPosition());
                }
            });

        }

        public void generateAlertDialogBox(int position){

            Toast.makeText(context, "position : " + position, Toast.LENGTH_SHORT).show();

            int amount = 0;
            int count = 0;
            for(String i:data.keySet()){
                if(count == position){
                    for(String j : data.get(i).keySet()){
                        if(data.get(i).get(j).quantity != 0){
                            amount += data.get(i).get(j).quantity * data.get(i).get(j).price;
                        }
                    }
                }
                count++;
            }

            if(amount == 0){
                Toast.makeText(context, "Please make quantity of at least 1 item greater than 0 in your cart shop", Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setCancelable(true);
            builder.setTitle("Confirm your order");
            builder.setMessage("Your total bill is Rs " + amount);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setDisable();
                    buyNowClicked();
                }
            });

            builder.show();

        }

        public void buyNowClicked(){

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final String email[] = new String[1];

            final String shopId[] = new String[1];
            int count = 0;
            for(String i:data.keySet()){
                if(count == getAdapterPosition()){
                    shopId[0] = i;
                    break;
                }
                count++;
            }

            firebaseFirestore.collection("shop").document(shopId[0]).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                email[0] = documentSnapshot.getString("email");
                                String items = generateString(getAdapterPosition());
                                sendMail(email[0] , items , shopId[0] , getAdapterPosition());
                            }
                            else {
                                Toast.makeText(context, "shop has been removed", Toast.LENGTH_SHORT).show();
                                setEnable();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                            setEnable();
                        }
                    });
        }

        public void deleteIt(int position){
            notifyItemRemoved(position);
            int count = 0;
            for(String i:data.keySet()){
                if(count == position){
                    DatabaseHelper db = new DatabaseHelper(context);
                    if(db.deleteRowByShopId(i)) {
                        data.remove(i);
                        Toast.makeText(context, "Item deleted Successfully", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                count++;
            }
            Toast.makeText(context, "Unable to delete the item from cart", Toast.LENGTH_SHORT).show();
        }

        public void sendMail(String toEmail , String body , final String shopId , final int position){
            Hashtable<String , String> hashtable = new Hashtable<>();
            hashtable.put("from" , "indishop.original@gmail.com");
            hashtable.put("to" , toEmail);
            hashtable.put("subject" , "Order Details");
            hashtable.put("text" , body);

            if(toEmail.equalsIgnoreCase("")){
                Toast.makeText(context, "Please select something to buy", Toast.LENGTH_SHORT).show();
                setEnable();
                return;
            }

            new SendGridAsyncTask(new SendGridAsyncTask.AsynResponse() {
                @Override
                public void processFinish(Boolean output) {
                    // you can go here
                    boolean result = placeOrder(shopId , position);
                }
            }).execute(hashtable);

//            Toast.makeText(context, body, Toast.LENGTH_SHORT).show();

        }

        public boolean placeOrder(final String shopId , final int position){

            final boolean[] done = {false};

            // name and quantity and then price

            final HashMap<String , Object> forShop = new HashMap<>();
            HashMap<String , Object> forUser = new HashMap<>();

            HashMap<String , ArrayList<Object>> order = new HashMap<>();
            for(String i:data.get(shopId).keySet()){
                if(data.get(shopId).get(i).quantity != 0){
                    ArrayList<Object> a = new ArrayList<>();
                    a.add(data.get(shopId).get(i).name);
                    a.add(data.get(shopId).get(i).quantity);
                    a.add(data.get(shopId).get(i).price);
                    a.add(data.get(shopId).get(i).image);
                    order.put(i , a);
                }
            }

            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

            final String timeStamp = Long.toString(System.currentTimeMillis());

            forShop.put("product" , order);
            forShop.put("user" , mUser.getUid());
            forShop.put("delivered" , false);
            forShop.put("timestamp" , timeStamp);

            forUser.put("product" , order);
            forUser.put("shop" , shopId);
            forUser.put("delivered" , false);
            forUser.put("timestamp" , timeStamp);

            firebaseFirestore.collection("users").document(mUser.getUid()).collection("bought").document(timeStamp).set(forUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            firebaseFirestore.collection("shop").document(shopId).collection("purchased").document(timeStamp).set(forShop)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Item placed Successfully", Toast.LENGTH_SHORT).show();
                                            done[0] = false;
                                            data.remove(shopId);
                                            DatabaseHelper db = new DatabaseHelper(context);
                                            db.deleteRowByShopId(shopId);
                                            notifyItemRemoved(position);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                            setEnable();
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            setEnable();
                        }
                    });

            return done[0];
        }

        private String generateString(int position) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_details) , Context.MODE_PRIVATE);
            String ans = "By " + sharedPreferences.getString(context.getString(R.string.user_number) , "Not specified") + "\n\n";
            int count = 0;
            int totalPrice = 0;
            for(String i : data.keySet()){
                if(count == position){
                    for(String j : data.get(i).keySet()){
                        if(data.get(i).get(j).quantity != 0){
                            ans += data.get(i).get(j).name + " " + data.get(i).get(j).quantity + " * " + data.get(i).get(j).price + "\n";
                            totalPrice += data.get(i).get(j).quantity * data.get(i).get(j).price;
                        }
                    }
                }
                count++;
            }
            ans += "\n\n Total Bill : " + totalPrice;
            return ans;
        }


        public void setEnable(){
            buyNow.setEnabled(true);
            buyNow.setVisibility(View.VISIBLE);

            delete.setEnabled(true);
            delete.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.INVISIBLE);
        }

        public void setDisable(){
            buyNow.setEnabled(false);
            buyNow.setVisibility(View.INVISIBLE);

            delete.setEnabled(false);
            delete.setVisibility(View.INVISIBLE);

            progressBar.setVisibility(View.VISIBLE);
        }

    }

}
