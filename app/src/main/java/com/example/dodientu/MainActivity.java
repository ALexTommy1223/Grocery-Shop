package com.example.dodientu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dodientu.R;
import com.example.dodientu.adapter.GridLayoutAdapter;
import com.example.dodientu.adapter.My_Adapter;
import com.example.dodientu.model.FavouritesClass;
import com.example.dodientu.model.HorizontalProductModel;
import com.example.dodientu.model.Model;
import com.example.dodientu.model.Offers;
import com.example.dodientu.model.User;
import com.example.dodientu.ui.CartActivity;
import com.example.dodientu.ui.CategoryActivity;
import com.example.dodientu.ui.FavouritesActivity;
import com.example.dodientu.ui.LoginActivity;
import com.example.dodientu.ui.OrderActivity;
import com.example.dodientu.ui.UserProfileActivity;
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
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private CircleImageView image;
    private TextView  mPerson_Name;
    private FirebaseAuth mAuth;
    private String uId,name,photo;
    private FirebaseUser currentUser;
    private NavigationView navigationView;
    private ViewPager pager;
    private My_Adapter adapter;
    private List<Model> models;
    private DatabaseReference db;
    private View mnavigationView;
    private static List<FavouritesClass> favourites;

    //custom xml views(cart icon)
    private RelativeLayout customCartContainer;
    private TextView pageTitle;
    private TextView customCartNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        uId=currentUser.getUid();

        navigationView=findViewById(R.id.navegation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mnavigationView=navigationView.getHeaderView(0);
        mPerson_Name=navigationView.findViewById(R.id.persname);
        image=mnavigationView.findViewById(R.id.circimage);
        drawerLayout=findViewById(R.id.drawber);

        mToolbar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolbar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //retrieve header view user data
        navigation_view_header_data();
        //retrieve favourites
        netrieve_Fav();
        //firstview;
        retrieve_Electroncis();
        //SecondView
        retrieve_Fruits();
        //Third View 
        retrieve_Meats();
        //Fourth View
        retrieve_Vegatables();
        //offers
        retrieve_Offers();
        //refresh CartIcon
        showCartIcon();
        
        //to check if the total price is zero or not
        handleTotalPriceTozeroIfNotExist();
    }

    private void handleTotalPriceTozeroIfNotExist() {
        DatabaseReference root=FirebaseDatabase.getInstance().getReference();
        DatabaseReference m=root.child("cart").child(uId);
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    FirebaseDatabase.getInstance().getReference().child("cart").child(uId).child("totalPrice").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    private void showCartIcon() {
        //toolbar & cartIcon
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.main2_toolbar,null);
        actionBar.setCustomView(view);

        //custom action items xml
        customCartContainer=(RelativeLayout) findViewById(R.id.customCartIconContainer);
        pageTitle=(TextView) findViewById(R.id.pageTitle);
        customCartNumber=(TextView) findViewById(R.id.customCartNumber);

        pageTitle.setText("Home");
        setNumberOfItemInCartIcon();
    }

    private void setNumberOfItemInCartIcon() {
        DatabaseReference root=FirebaseDatabase.getInstance().getReference();
        DatabaseReference m=root.child("cart").child(uId);

        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.getChildrenCount()==1){
                        customCartNumber.setVisibility(View.GONE);
                    }
                    else{
                        customCartNumber.setVisibility(View.VISIBLE);
                        customCartNumber.setText(String.valueOf(snapshot.getChildrenCount()-1));
                    }
                }
                else{
                    customCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    private void retrieve_Offers() {
        DatabaseReference root=FirebaseDatabase.getInstance().getReference();
        DatabaseReference m=root.child("offers");

        models=new ArrayList<>();
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()
                     ) {
                    Offers offers=new Offers();
                    offers=ds.getValue(Offers.class);
                    offers.setTitle(ds.getKey().toString());
                    models.add(new Model(offers.getImg(),offers.getTitle(),offers.getDescription()));
                    adapter=new My_Adapter(MainActivity.this,models);
                    pager=findViewById(R.id.cardView);
                    pager.setAdapter((PagerAdapter) adapter);
                    pager.setPadding(130,0,130,0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    private void retrieve_Vegatables() {
        LinearLayout mLayout=findViewById(R.id.my_cardView4);
        LayoutInflater inflater=getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout,mLayout,false);
        TextView gridLayoutTitle=mLayout.findViewById(R.id.grid_product_layout_textview);
        gridLayoutTitle.setText("Vegetables");
        Button gridLayoutViewBtn=mLayout.findViewById(R.id.grid_button_layout_viewall_button);

        final GridView gv=mLayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastModels=new ArrayList<>();
        final  GridLayoutAdapter my_adapter;

        my_adapter=new GridLayoutAdapter(lastModels,favourites,MainActivity.this);
        db=FirebaseDatabase.getInstance().getReference().child("product").child("Vegetables");

        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()
                     ) {
                    User my_User=new User();
                    my_User=ds.getValue(User.class);
                    my_User.setCategory(ds.getKey().toString());
                    lastModels.add(new HorizontalProductModel(my_User.getImage(),my_User.getCategory(),my_User.getPrice(),false,my_User.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        db.addListenerForSingleValueEvent(eventListener);
        gridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("Category Name","Vegetables");
                startActivity(intent);
            }
        });
    }

    private void retrieve_Meats() {
        LinearLayout myLayout=findViewById(R.id.my_cardView3);
        LayoutInflater inflater=getLayoutInflater();

        inflater.inflate(R.layout.grid_product_layout,myLayout,false);
        TextView girdlayoutTitle=myLayout.findViewById(R.id.grid_product_layout_textview);
        girdlayoutTitle.setText("ELectronics");
        Button gridLayoutViewBtn=myLayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv=myLayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastModel=new ArrayList<>();
        final GridLayoutAdapter my_Adapter;

        my_Adapter=new GridLayoutAdapter(lastModel,favourites,MainActivity.this);
        db= FirebaseDatabase.getInstance().getReference().child("product").child("Meats");
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()
                ) {
                    User my_user=new User();
                    my_user=ds.getValue(User.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastModel.add(new HorizontalProductModel(my_user.getImage(),my_user.getCategory(),my_user.getPrice(),false,my_user.getExpired()));
                }
                gv.setAdapter(my_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        db.addListenerForSingleValueEvent(eventListener);
        gridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("Category Name","Meats");
                startActivity(intent);
            }
        });
    }

    private void retrieve_Fruits() {
        LinearLayout myLayout=findViewById(R.id.my_cardView);
        LayoutInflater inflater=getLayoutInflater();

        inflater.inflate(R.layout.grid_product_layout,myLayout,false);
        TextView girdlayoutTitle=myLayout.findViewById(R.id.grid_product_layout_textview);
        girdlayoutTitle.setText("ELectronics");
        Button gridLayoutViewBtn=myLayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv=myLayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastModel=new ArrayList<>();
        final GridLayoutAdapter my_Adapter;

        my_Adapter=new GridLayoutAdapter(lastModel,favourites,MainActivity.this);
        db= FirebaseDatabase.getInstance().getReference().child("product").child("Fruits");
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()
                ) {
                    User my_user=new User();
                    my_user=ds.getValue(User.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastModel.add(new HorizontalProductModel(my_user.getImage(),my_user.getCategory(),my_user.getPrice(),false,my_user.getExpired()));
                }
                gv.setAdapter(my_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        db.addListenerForSingleValueEvent(eventListener);
        gridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("Category Name","Fruits");
                startActivity(intent);
            }
        });
    }

    private void retrieve_Electroncis() {
        LinearLayout myLayout=findViewById(R.id.my_cardView);
        LayoutInflater inflater=getLayoutInflater();

        inflater.inflate(R.layout.grid_product_layout,myLayout,false);
        TextView girdlayoutTitle=myLayout.findViewById(R.id.grid_product_layout_textview);
        girdlayoutTitle.setText("ELectronics");
        Button gridLayoutViewBtn=myLayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv=myLayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastModel=new ArrayList<>();
        final GridLayoutAdapter my_Adapter;

        my_Adapter=new GridLayoutAdapter(lastModel,favourites,MainActivity.this);
        db= FirebaseDatabase.getInstance().getReference().child("product").child("Electronics");
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()
                     ) {
                    User my_user=new User();
                    my_user=ds.getValue(User.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastModel.add(new HorizontalProductModel(my_user.getImage(),my_user.getCategory(),my_user.getPrice(),false,my_user.getExpired()));
                }
                gv.setAdapter(my_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        db.addListenerForSingleValueEvent(eventListener);
        gridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("Category Name","Electronics");
                startActivity(intent);
            }
        });

    }

    private void netrieve_Fav() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("favourites")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        favourites=new ArrayList<>();
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()
                     ) {
                    FavouritesClass fav=new FavouritesClass();
                    fav=ds.getValue(FavouritesClass.class);
                    favourites.add(fav);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    private void navigation_view_header_data() {
        DatabaseReference root=FirebaseDatabase.getInstance().getReference();
        DatabaseReference m=root.child("users").child(uId);
        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name=snapshot.child("Name").getValue().toString();
                    photo=snapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(image);
                    }
                    else{
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);

                        mPerson_Name.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item))
            return  true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.profile){
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
        }
        else if(id==R.id.favourites){
            startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
        }
        else if(id==R.id.cart){
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }
        else if(id==R.id.myOrders){
            startActivity(new Intent(MainActivity.this, OrderActivity.class));
        }
        else if(id==R.id.fruits){
            Intent intent= new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Catgory Name","Fruits");
            startActivity(intent);
        }
        else if(id==R.id.vegetables){
            Intent intent= new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Catgory Name","Vegetables");
            startActivity(intent);
        }
        else if(id==R.id.fruits){
            Intent intent= new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Catgory Name","Electronics");
            startActivity(intent);
        }
        else if(id==R.id.Logout){
            checkLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkLogout() {
        AlertDialog.Builder checkAlter=new AlertDialog.Builder(MainActivity.this);
        checkAlter.setMessage("Do you want to logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alter=checkAlter.create();
        alter.setTitle("Logout");
        alter.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}