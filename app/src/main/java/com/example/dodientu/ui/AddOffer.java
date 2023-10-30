package com.example.dodientu.ui;

import static com.google.common.io.Files.getFileExtension;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dodientu.R;
import com.example.dodientu.model.Offer;
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

public class AddOffer extends AppCompatActivity {

    private ImageView img;
    private TextInputEditText name ,description;
    private TextInputLayout nameInputLayout,descriptionInputLayout;
    private Uri uriImg;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private Button add, chooseImage;
    //
    private Toolbar tooblbar;
    private RelativeLayout customCartContainer;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        img=findViewById(R.id.offerImage);
        name=findViewById(R.id.editTextOfferName);
        description=findViewById(R.id.editTextOfferDescription);
        nameInputLayout=findViewById(R.id.editTextOfferLayout);
        descriptionInputLayout=findViewById(R.id.editTextDescriptionLayout);
        add=findViewById(R.id.btnAdd);
        chooseImage=findViewById(R.id.btnChooseImg);
        tooblbar=findViewById(R.id.addOffer_Toolbar);
        mStorageRef= FirebaseStorage.getInstance().getReference("offers");

        setSupportActionBar(tooblbar);
        getSupportActionBar().setTitle("Add Offer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(name.getText().toString().isEmpty()){
                    nameInputLayout.setErrorEnabled(true);
                    nameInputLayout.setError("Please Enter Offer Name");
                }
                else{
                    nameInputLayout.setErrorEnabled(false);
                }
            }
        });
        descriptionInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(description.getText().toString().isEmpty()){
                    descriptionInputLayout.setErrorEnabled(true);
                    descriptionInputLayout.setError("Please Enter Description Name");
                }
                else{
                    descriptionInputLayout.setErrorEnabled(false);
                }
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(name.getText().toString().isEmpty()){
                    nameInputLayout.setErrorEnabled(true);
                    nameInputLayout.setError("Please Enter Offer Name");
                }
                else{
                    nameInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(description.getText().toString().isEmpty()){
                    descriptionInputLayout.setErrorEnabled(true);
                    descriptionInputLayout.setError("Please Enter Description Name");
                }
                else{
                    descriptionInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(AddOffer.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().isEmpty()||description.getText().toString().isEmpty()||
                uriImg==null){
                    Toast.makeText(AddOffer.this, "Empty Cells", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        uploadData();
                        Toast.makeText(AddOffer.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch (Exception e){
                        Toast.makeText(AddOffer.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void uploadData() {
        if(name.getText().toString().isEmpty()||description.getText().toString().isEmpty()||
                uriImg==null){
            Toast.makeText(AddOffer.this, "Empty Cells", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadImage();
        }
    }

    private void uploadImage() {
        if(uriImg!=null){
            StorageReference fileReference=mStorageRef.child(name.getText().toString()+ "."+getFileExtension(uriImg));
            mUploadTask=fileReference.putFile(uriImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful()){
                        Uri download=uriTask.getResult();
                        Offer offer=new Offer(description.getText().toString().trim(),
                                download.toString());

                        DatabaseReference z= FirebaseDatabase.getInstance()
                                .getReference("offers");
                                z.child(name.getText().toString().trim()).setValue(offer);


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddOffer.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    public String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uriImg));
    }
    private void choosePhoto() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,RegisterActivity.GALARY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RegisterActivity.GALARY_PICK&& resultCode==RESULT_OK&& data!=null){
            uriImg=data.getData();
            Log.e("uri",uriImg.toString());
            try{
                Picasso.get().load(uriImg).fit().centerCrop().into(img);
            }catch (Exception e){
                Log.e(this.toString(),e.getMessage().toString());
            }
        }
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
}