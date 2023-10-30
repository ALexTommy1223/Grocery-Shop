package com.example.dodientu.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dodientu.R;
import com.example.dodientu.adapter.AdminOfferAdapter;
import com.example.dodientu.adapter.AdminProductAdapter;
import com.example.dodientu.model.AdminProduct;
import com.example.dodientu.model.AdminSalesMan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {
    private RecyclerView productRecyler;
    private ProgressBar bar;
    private FloatingActionButton productFloatingButton;
    private AdminProductAdapter adapter;
    private List<AdminProduct> adminProductLis;
    private DatabaseReference mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product, container, false);
        productRecyler=view.findViewById(R.id.productRecylerview);
        bar=view.findViewById(R.id.productProgressBar);
        productFloatingButton=view.findViewById(R.id.productActionButton);

        adapter=new AdminProductAdapter(getActivity(),adminProductLis);
        adminProductLis=new ArrayList<>();
        productRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
        productRecyler.setAdapter(adapter);

        mDb.addValueEventListener(new ValueEventListener() { // thêm sản phẩm lên database
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminProductLis.clear();
                for (DataSnapshot dataSnapshot1:snapshot.getChildren()  //category
                     ) {
                    for (DataSnapshot dataSnapshot2: snapshot.getChildren()//name
                         ) {
                        adminProductLis.add(new AdminProduct(dataSnapshot2.getKey(),dataSnapshot1.getKey(),
                                dataSnapshot2.child("expired").getValue(String.class),
                                dataSnapshot2.child("image").getValue(String.class),
                                dataSnapshot2.child("price").getValue(String.class),
                                dataSnapshot2.child("quantity").getValue(String.class)
                                ));
                    }
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setItemClickListener(new AdminOfferAdapter.onItemClickListener() {
            @Override
            public void onItemCLick(int pos) {
                Intent i=new Intent(getActivity(),EditProduct.class);
                Bundle b=new Bundle();
                b.putString("img",adminProductLis.get(pos).getImage());
                b.putString("name",adminProductLis.get(pos).getName());
                b.putString("category",adminProductLis.get(pos).getCategory());
                b.putString("expired",adminProductLis.get(pos).getExpired());
                b.putString("price",adminProductLis.get(pos).getPrice());
                b.putString("quantity",adminProductLis.get(pos).getQuantity());
                i.putExtras(b);
                startActivity(i);
            }
        });
        adapter.setLongClickListener(new AdminOfferAdapter.onLongClickListener() {
            @Override
            public void onItemLongCLick(int pos) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity()).setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference=mDb.child(adminProductLis.get(pos).getCategory()).child(adminProductLis.get(pos).getName());
                                reference.removeValue();
                                StorageReference z= FirebaseStorage.getInstance().getReference("offers").child(adminProductLis.get(pos).getName()+ ".jpg");
                                z.delete();
                                StorageReference x=FirebaseStorage.getInstance().getReference("offers").child(adminProductLis.get(pos).getName());
                                x.delete();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
            }
        });
        productFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddProduct.class));
            }
        });
        return view;
    }
}