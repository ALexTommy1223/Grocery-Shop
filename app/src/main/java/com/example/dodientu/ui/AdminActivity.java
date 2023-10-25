package com.example.dodientu.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.dodientu.R;
import com.example.dodientu.model.SalesMan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private BottomNavigationView bottomNavigationView;
    private TextView fragementTitle;
    private FirebaseAuth mAuth;
    private RelativeLayout customCartContainer;
    private TextView pageTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth=FirebaseAuth.getInstance();
        mToolbar=(Toolbar) findViewById(R.id.admin_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Admin Control");

        fragementTitle=findViewById(R.id.fragmentTitle);
        bottomNavigationView=findViewById(R.id.bottom_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(naveListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener naveListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            int id=item.getItemId();

            if(id==R.id.productId){
                selectedFragment=new ProductFragment();
                fragementTitle.setText("All Products");
            }
            else if(id==R.id.offersId){
                selectedFragment=new OffersFragment();
                fragementTitle.setText("All Products");
            }
            else if(id==R.id.salesMenId){
                selectedFragment=new SalesMenFragment();
                fragementTitle.setText("All SalesMen");
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        notShowCartIcon();
    }
    
    private void notShowCartIcon() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.main2_toolbar,null);

        customCartContainer=(RelativeLayout) findViewById(R.id.customCartIconContainer);
        pageTitle=findViewById(R.id.pageTitle);
        pageTitle.setVisibility(View.GONE);
        customCartContainer.setVisibility(View.GONE);
    }
}