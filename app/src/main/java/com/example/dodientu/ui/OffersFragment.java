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
import com.example.dodientu.model.AdminOffers;
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


public class OffersFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //
    private RecyclerView offerRecyclerView;
    private AdminOfferAdapter adapter;
    private FloatingActionButton offerFloatingAction;
    private ProgressBar bar;
    private DatabaseReference mDatabaseRef;
    private View view;
    private List<AdminOffers> offersList;

    public OffersFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OffersFragment newInstance(String param1, String param2) {
        OffersFragment fragment = new OffersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view =inflater.inflate(R.layout.fragment_offers, container, false);

        bar=view.findViewById(R.id.offerProgressBar);
        offerRecyclerView=view.findViewById(R.id.offerRecylerview);
        offerFloatingAction=view.findViewById(R.id.offerActionButton);

        offersList=new ArrayList<>();
        adapter=new AdminOfferAdapter(getActivity(),offersList);

        offerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        offerRecyclerView.setAdapter(adapter);

        bar.setVisibility(View.VISIBLE);
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("offers");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offersList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()
                     ) {
                    offersList.add(new AdminOffers(snapshot.getKey(),snapshot.child("description").getValue(String.class),
                            snapshot.child("img").getValue(String.class)
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
                Intent i=new Intent(getActivity(),EditOffer.class);
                Bundle b=new Bundle();
                b.putString("img",offersList.get(pos).getOfferImg());
                b.putString("name",offersList.get(pos).getOfferName());
                b.putString("description",offersList.get(pos).getOfferDescription());
                i.putExtras(b);
                startActivity(i);
            }
        });
        adapter.setLongListener(new AdminOfferAdapter.onLongClickListener() {
            @Override
            public void onItemLongCLick(int pos) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity()).setTitle("Confirmation")
                        .setMessage("Do you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference=mDatabaseRef.child(offersList.get(pos).getOfferName());
                                reference.removeValue();
                                StorageReference z = FirebaseStorage.getInstance().getReference("offers").child(offersList.get(pos).getOfferName() + ".jpg");
                                z.delete();
                                StorageReference x = FirebaseStorage.getInstance().getReference("offers").child(offersList.get(pos).getOfferName());
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
        offerFloatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddOffer.class));
            }
        });
        return view;
    }
}