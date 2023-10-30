package com.example.dodientu.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dodientu.R;
import com.example.dodientu.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddProduct extends AppCompatActivity {
    private TextInputEditText name,quantity,price,expDate;
    private ImageView img;
    private Button add,choose;
    private Uri imgUri;
    private String category;
    private StorageReference mStorageRef;
    private Spinner spinner;
    private StorageTask mUploadTask;
    private TextInputLayout nameLayout,priceLayout,quantityLayout,expDateLayout;

    //
    private Toolbar toolbar;
    private RelativeLayout customCartContainer;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        toolbar=findViewById(R.id.addProduct_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=findViewById(R.id.editTextProductName);
        quantity=findViewById(R.id.editTextProductNumber);
        price=findViewById(R.id.editTextProductPrice);
        expDate=findViewById(R.id.editTextProductExpire);
        //
        img=findViewById(R.id.imgProduct);
        //
        nameLayout=findViewById(R.id.editTextProductNameLayout);
        quantityLayout=findViewById(R.id.editTextProductNumberLayout);
        priceLayout=findViewById(R.id.editTextProductPriceLayout);
        expDateLayout=findViewById(R.id.editTextProductExpireLayout);
        //
        add=findViewById(R.id.btnAdd);
        choose=findViewById(R.id.btnChooseImg);

        spinner=findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.productstypes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mStorageRef= FirebaseStorage.getInstance().getReference("products");

        nameLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(name.getText().toString().trim().isEmpty()){
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Please enter offer name");
                }
                else {
                    nameLayout.setErrorEnabled(false);
                }
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(name.getText().toString().trim().isEmpty()){
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Please enter offer name");
                }
                else {
                    nameLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        quantityLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(quantity.getText().toString().trim().isEmpty()){
                    quantityLayout.setErrorEnabled(true);
                    quantityLayout.setError("Please Enter Offer Name");
                }
                else{
                    quantityLayout.setErrorEnabled(false);
                }
            }
        });
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(quantity.getText().toString().trim().isEmpty()){
                    quantityLayout.setErrorEnabled(true);
                    quantityLayout.setError("Please enter offer name");
                }
                else {
                    quantityLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        priceLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(price.getText().toString().trim().isEmpty()){
                    priceLayout.setErrorEnabled(true);
                    priceLayout.setError("Please Enter Offer Name");
                }
                else{
                    priceLayout.setErrorEnabled(false);
                }
            }
        });
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(price.getText().toString().trim().isEmpty()){
                    priceLayout.setErrorEnabled(true);
                    priceLayout.setError("Please enter offer name");
                }
                else {
                    priceLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        expDateLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (expDate.getText().toString().trim().isEmpty()) {
                    expDateLayout.setErrorEnabled(true);
                    expDateLayout.setError("Please Enter Offer Name");
                } else {
                    expDateLayout.setErrorEnabled(false);
                }
            }
        });

        expDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (expDate.getText().toString().trim().isEmpty()) {
                    expDateLayout.setErrorEnabled(true);
                    expDateLayout.setError("Please Enter Offer Name");
                } else {
                    expDateLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask!=null&&mUploadTask.isInProgress()){
                    Toast.makeText(AddProduct.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().isEmpty()||quantity.getText().toString().isEmpty()||price.getText().toString().isEmpty()||expDate.getText().toString().isEmpty()){
                    Toast.makeText(AddProduct.this, "Empty Cell", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadData();
                    Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void uploadData() {
        if(name.getText().toString().isEmpty()||quantity.getText().toString().isEmpty()||price.getText().toString().isEmpty()||expDate.getText().toString().isEmpty()) {
            Toast.makeText(AddProduct.this, "Empty Cell", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadImage();
        }
    }

    private void uploadImage() {// upload image
        if(imgUri!=null){
            StorageReference fileReference=mStorageRef.child(name.getText().toString()+"."+getFileExtension(imgUri));
            mUploadTask=fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful()){
                        Uri download=uriTask.getResult();
                        Product product=new Product(quantity.getText().toString().trim(),
                                price.getText().toString().trim(),
                                expDate.getText().toString().trim(),
                                download.toString()
                                );

                        DatabaseReference z= FirebaseDatabase.getInstance()
                                .getReference()
                                .child("product")
                                .child("category")
                                .child(name.getText().toString());
                        z.setValue(product);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public String getFileExtension(Uri uri){ //change uri to string
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onStart() {
        super.onStart();
        notShowCartIcon();
    }

    private void notShowCartIcon() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.main2_toolbar,null);

        customCartContainer=findViewById(R.id.customCartIconContainer);
        pageTitle=findViewById(R.id.pageTitle);
        customCartContainer.setVisibility(View.GONE);
    }

    private void openImage() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,RegisterActivity.GALARY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RegisterActivity.GALARY_PICK&&resultCode== Activity.RESULT_OK&&data.getData()!=null&&data!=null){
            imgUri=data.getData();
            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            }
            catch(Exception e){
                Log.e(this.toString(),e.getMessage().toString());
            }
        }
    }
}