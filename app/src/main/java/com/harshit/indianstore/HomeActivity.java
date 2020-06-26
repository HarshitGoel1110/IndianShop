package com.harshit.indianstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    DocumentReference user;

    FragmentManager fragmentManager;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    boolean hasShop = false;
    MenuItem shop;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        // for doing change in navigation drawer shop display :- create or have
        setShop();

        setUpDrawerContext(navigationView);

    }

    public void selectIemDrawer(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.drawerHomeShop : {
                if(hasShop) {
                    fragmentManager.beginTransaction().replace(R.id.homeActivityFrame , new ViewShop() , null).addToBackStack(null).commit();
//                    Toast.makeText(HomeActivity.this, "working", Toast.LENGTH_SHORT).show();
                    break;
//                    fragmentClass = ViewShop.class;
//                    break;
                }
                else{
                    fragmentManager.beginTransaction().replace(R.id.homeActivityFrame , new CreateNewShop() , null).addToBackStack(null).commit();
//                    Toast.makeText(HomeActivity.this, "working", Toast.LENGTH_SHORT).show();
                    break;
//                    fragmentClass = CreateNewShop.class;
//                    break;
                }
            }
            case R.id.drawerHomeBuy : {
                fragmentManager.beginTransaction().replace(R.id.homeActivityFrame , new AllShops() , null).addToBackStack(null).commit();
//                Toast.makeText(HomeActivity.this, "working", Toast.LENGTH_SHORT).show();
                break;
//                fragmentClass = AllShops.class;
//                break;
            }
            case R.id.drawerHomeMyCart : {
                fragmentManager.beginTransaction().replace(R.id.homeActivityFrame , new MyCartView() , null).addToBackStack(null).commit();
                break;
            }
            default:{

            }
        }
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
    }

    public void setUpDrawerContext(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectIemDrawer(menuItem);
                return false;
            }
        });
    }



    private void setShop() {
        String uid = mUser.getUid();
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        user = db.collection("users").document(uid);
        user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    hasShop = documentSnapshot.getBoolean("shop");
                    NavigationView navigationView = findViewById(R.id.homeNavigation);
                    Menu menu = navigationView.getMenu();
                    shop = menu.findItem(R.id.drawerHomeShop);

                    if(hasShop) {
                        shop.setTitle("View Your Shop");
                    }
                    else
                        shop.setTitle("Create a new Shop");
                }
                else{
                    Toast.makeText(HomeActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Some error occur while fetching", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // for quickly exiting the activity
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this , WelcomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        if (item.getItemId() == R.id.homeActivityLogout) {
            logout();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // for customising th toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu , menu);
        return true;

    }


    public void init(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.homeNavigation);
        drawerLayout = findViewById(R.id.homeDrawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this , drawerLayout , R.string.open , R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.homeActivityFrame , new AllShops() , null).addToBackStack(null).commit();
        Toast.makeText(HomeActivity.this, "working", Toast.LENGTH_SHORT).show();

    }

    // --------------------------------------------------the last 2 functions are of create new shop fragment class ------------------------------------

    public void createNewShop(View v){
        TextInputLayout mName = findViewById(R.id.createNewShopName);
        TextInputLayout mAddress = findViewById(R.id.createNewAddress);
        TextInputLayout mCity = findViewById(R.id.createNewCity);
        TextInputLayout mEmail = findViewById(R.id.createNewEmail);
        TextInputLayout mNumber = findViewById(R.id.createNewPhno);
        TextInputLayout mPincode = findViewById(R.id.createNewPincode);

        String name = mName.getEditText().getText().toString();
        String address = mAddress.getEditText().getText().toString();
        String city = mCity.getEditText().getText().toString();
        String email = mEmail.getEditText().getText().toString();
        String number = mNumber.getEditText().getText().toString();
        String pincode = mPincode.getEditText().getText().toString();

        if(name.isEmpty() || address.isEmpty() || city.isEmpty() || email.isEmpty() || number.isEmpty() || pincode.isEmpty()){
            Toast.makeText(this, "Please fill all the entries...", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String , Object> m = new HashMap<>();
        m.put("name" , name);
        m.put("address" , address);
        m.put("city" , city);
        m.put("email" , email);
        m.put("mobile" , number);
        m.put("pincode" , pincode);

        db.collection("shop").document(mUser.getUid()).set(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateField();
                        // update the document
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateField(){
        HashMap<String , Object> m = new HashMap<>();
        m.put("shop" , true);

        db.collection("users").document(mUser.getUid()).update(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomeActivity.this, "added successfully...", Toast.LENGTH_SHORT).show();
                        hasShop = true;
                        shop.setTitle("View Your Shop");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HomeActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}