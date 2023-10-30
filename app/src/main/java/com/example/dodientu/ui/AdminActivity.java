package com.example.dodientu.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.adminLogoutId){
            checkLogout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkLogout() {
        AlertDialog.Builder check=new AlertDialog.Builder(AdminActivity.this);

        check.setMessage("Do you want to logout").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alter=check.create();
        alter.setTitle("Logout");
        alter.show();
    }
}