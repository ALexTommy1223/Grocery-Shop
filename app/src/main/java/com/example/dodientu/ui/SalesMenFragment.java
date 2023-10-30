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
import com.example.dodientu.adapter.AdminSalesManAdapter;
import com.example.dodientu.model.AdminSalesMan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class SalesMenFragment extends Fragment {

    private View view ;
    private RecyclerView salesManRec;
    private AdminSalesManAdapter adapter;
    private List<AdminSalesMan> salesManList;
    private FloatingActionButton salesFloatingActionBtn;
    private ProgressBar bar;
    private DatabaseReference mDatabaseRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_sales_men, container, false);

        salesManRec=view.findViewById(R.id.salesManRecylerview);
        salesFloatingActionBtn=view.findViewById(R.id.salesManActionButton);
        bar=view.findViewById(R.id.salesManProgressBar);

        salesManList=new ArrayList<>();
        adapter=new AdminSalesManAdapter(getActivity(),salesManList);

        salesManRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        salesManRec.setAdapter(adapter);

        bar.setVisibility(View.VISIBLE);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                salesManList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()
                     ) {
                    salesManList.add(new AdminSalesMan(dataSnapshot.getKey(),dataSnapshot.child("img").getValue(String.class),
                            dataSnapshot.child("qrimage").getValue(String.class),
                            dataSnapshot.child("salary").getValue(String.class)
                            ));
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setItemListener(new AdminOfferAdapter.onItemClickListener() {
            @Override
            public void onItemCLick(int pos) {
                Intent i= new Intent(getActivity(),EditSalesMan.class);
                Bundle b=new Bundle();
                b.putString("img",salesManList.get(pos).getImg());
                b.putString("name",salesManList.get(pos).getName());
                b.putString("salary",salesManList.get(pos).getSalary());
                b.putString("qrimg",salesManList.get(pos).getQrimg());
                i.putExtras(b);
                startActivity(i);
            }
        });

        adapter.setLongListener(new AdminOfferAdapter.onLongClickListener() {
            @Override
            public void onItemLongCLick(int pos) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity()).setTitle("Confirm")
                        .setMessage("Do you want to delete").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference=mDatabaseRef.child(salesManList.get(pos).getName());
                                reference.removeValue();

                                StorageReference a= FirebaseStorage.getInstance().getReference("salesman").child(salesManList.get(pos).getName()+ ".jpg");
                                a.delete();
                                StorageReference b=FirebaseStorage.getInstance().getReference("salesman").child(salesManList.get(pos).getName());
                                b.delete();
                                StorageReference c=FirebaseStorage.getInstance().getReference("salesman").child(salesManList.get(pos).getName()+ "qr.jpg");
                                c.delete();
                                StorageReference d=FirebaseStorage.getInstance().getReference("salesman").child(salesManList.get(pos).getName()+"qr");
                                d.delete();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();
            }
        });
        salesFloatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddSalesMan.class));
            }
        });
        return view;
    }
}