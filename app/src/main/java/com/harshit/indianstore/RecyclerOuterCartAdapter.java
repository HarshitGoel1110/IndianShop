package com.harshit.indianstore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Hashtable;

class RecyclerOuterCartAdapter extends RecyclerView.Adapter<RecyclerOuterCartAdapter.CartOuterViewHolder> {

    Context context;
    HashMap<String , HashMap<String , MyCartView.Combine>> data;

    public RecyclerOuterCartAdapter(Context context , HashMap<String , HashMap<String , MyCartView.Combine>> data){
        this.context = context;
        this.data = data;
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
                nextData = data.get(i);
                break;
            }
            count++;
        }
        holder.shopName.setText(shopId);
        RecyclerInnerCartAdapter adapter = new RecyclerInnerCartAdapter(context , nextData);
        holder.outerRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CartOuterViewHolder extends RecyclerView.ViewHolder{

        TextView shopName;
        RecyclerView outerRecyclerView;
        Button button;

        public CartOuterViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.cartShopName);
            outerRecyclerView = itemView.findViewById(R.id.cartOuterRecyclerView);
            button = itemView.findViewById(R.id.cartOuterBuyNow);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            outerRecyclerView.setLayoutManager(linearLayoutManager);
            outerRecyclerView.setHasFixedSize(false);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    final String email[] = new String[1];

                    firebaseFirestore.collection("shop").document(shopName.getText().toString()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        email[0] = documentSnapshot.getString("email");
                                        String items = generateString(getAdapterPosition());
                                        sendMail(email[0] , items);
                                    }
                                    else {
                                        Toast.makeText(context, "Document is empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Internet Speed...", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

        }

        public void sendMail(String toEmail , String body){
            Hashtable<String , String> hashtable = new Hashtable<>();
            hashtable.put("from" , "indishop.original@gmail.com");
            hashtable.put("to" , toEmail);
            hashtable.put("subject" , "Order Details");
            hashtable.put("text" , body);

            new SendGridAsyncTask(new SendGridAsyncTask.AsynResponse() {
                @Override
                public void processFinish(Boolean output) {
                    // you can go here
                    Toast.makeText(context, "Order Placed successfully", Toast.LENGTH_LONG).show();
                }
            }).execute(hashtable);

        }

        private String generateString(int position) {
            String ans = "";
            int count = 0;
            for(String i : data.keySet()){
                if(count == position){
                    for(String j : data.get(i).keySet()){
                        if(data.get(i).get(j).quantity != 0){
                            ans += data.get(i).get(j).name + " " + data.get(i).get(j).quantity + "\n";
                        }
                    }
                }
                count++;
            }
            return ans;
        }
    }

}
