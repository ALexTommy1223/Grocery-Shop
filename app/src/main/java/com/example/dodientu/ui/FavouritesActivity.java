package com.example.dodientu.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dodientu.MainActivity;
import com.example.dodientu.R;
import com.example.dodientu.adapter.My_Adapter;
import com.example.dodientu.adapter.My_Adapter_Recycler_View;
import com.example.dodientu.model.FavouritesClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavouritesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPerson_Name;
    private CircleImageView image;
    private String userId;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private RecyclerView.Adapter my_adapter;

    //custom xml views (cart icon)
    private RelativeLayout customCartContainer;
    private TextView pageTitle;
    private TextView customCartNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        userId=currentUser.getUid();

        mToolbar=findViewById(R.id.main_Toolbar);
        setSupportActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        defineNavigation();
    }

    private void defineNavigation() {
        View mnavigationview;
        navigationView = findViewById(R.id.navegation_view2);
        drawerLayout = findViewById(R.id.drawer2);

        navigationView.setNavigationItemSelectedListener(this);
        mnavigationview = navigationView.getHeaderView(0);
        mPerson_Name = mnavigationview.findViewById(R.id.persname);
        image = mnavigationview.findViewById(R.id.circimage);


        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getNavHeaderData();
    }

    private void getNavHeaderData() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(userId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String photo = dataSnapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);
                    mPerson_Name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
        retrieve_fav();
    }

    private void retrieve_fav() {
        LinearLayout mLayout=(LinearLayout) findViewById(R.id.recyclerViewlayout);
        LayoutInflater inflater=getLayoutInflater();
        inflater.inflate(R.layout.favourite_recycler_view,mLayout,false);

        final  RecyclerView rc=mLayout.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager;

        gridLayoutManager=new GridLayoutManager(this,2 ,GridLayoutManager.VERTICAL,false);
        rc.setLayoutManager(gridLayoutManager);

        final List<FavouritesClass> favourite_list=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("favorites")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    FavouritesClass fav=new FavouritesClass();
                    fav=ds.getValue(FavouritesClass.class);
                    favourite_list.add(fav);
                }
                my_adapter=new My_Adapter_Recycler_View(favourite_list);
                rc.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showCartIcon();

        handleTotalPriceToZeroIfNotExist();
    }

    private void showCartIcon() {
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.main2_toolbar,null);
        actionBar.setCustomView(view);

        //************custom action items xml**********************
        customCartContainer = (RelativeLayout)findViewById(R.id.customCartIconContainer);
        pageTitle =(TextView)findViewById(R.id.pageTitle);
        customCartNumber = (TextView)findViewById(R.id.customCartNumber);

        pageTitle.setText("Favourites");
        setNumberOfItemsInCartIcon();

        customCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavouritesActivity.this, CartActivity.class));
            }
        });
    }

    private void setNumberOfItemsInCartIcon() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount()==1){
                        customCartNumber.setVisibility(View.GONE);
                    }
                    else {
                        customCartNumber.setVisibility(View.VISIBLE);
                        customCartNumber.setText(String.valueOf(dataSnapshot.getChildrenCount()-1));
                    }
                }
                else{
                    customCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    private void handleTotalPriceToZeroIfNotExist() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("cart").child(userId).child("totalPrice").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       int id=item.getItemId();
        if (id == R.id.home) {
            startActivity(new Intent(FavouritesActivity.this, MainActivity.class));
        }
        else if (id == R.id.profile) {
            startActivity(new Intent(FavouritesActivity.this, UserProfileActivity.class));
        }
        else if(id == R.id.cart){
            startActivity(new Intent(FavouritesActivity.this, CartActivity.class));
        }
        else if(id == R.id.myOrders){
            startActivity(new Intent(FavouritesActivity.this, OrderActivity.class));
        }
        else if(id==R.id.fruits){
            Intent intent =new Intent(FavouritesActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Fruits");
            startActivity(intent);
        }
        else if(id==R.id.vegetables){
            Intent intent =new Intent(FavouritesActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Vegetables");
            startActivity(intent);
        }
        else if(id==R.id.meats){
            Intent intent =new Intent(FavouritesActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Meats");
            startActivity(intent);
        }
        else if(id==R.id.electronics){
            Intent intent =new Intent(FavouritesActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Electronics");
            startActivity(intent);
        }
        else if (id == R.id.Logout) {
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CheckLogout() {
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(FavouritesActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(FavouritesActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = checkAlert.create();
        alert.setTitle("LogOut");
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))return true;
        return super.onOptionsItemSelected(item);
    }
}