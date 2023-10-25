package com.example.dodientu.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dodientu.MainActivity;
import com.example.dodientu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private Bitmap bitmap;
    public static final int GALARY_PICK=1;
    private StorageReference mStorageReference;
    private Uri resultUri;
    private RelativeLayout rlayout;
    private String uId;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        final EditText email = findViewById(R.id.email);
        final EditText name = findViewById(R.id.name);
        final EditText pass1 = findViewById(R.id.pass1);
        final EditText pass2 = findViewById(R.id.pass2);
        final EditText num = findViewById(R.id.num);


        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //
        rlayout=findViewById(R.id.rlayout);
        animation=AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);


        imageView = findViewById(R.id.image);
        final Button register = findViewById(R.id.register);
        TextView login = findViewById(R.id.login);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()||name.getText().toString().isEmpty()||
                pass1.getText().toString().isEmpty()||pass2.getText().toString().isEmpty()||num.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Empty Celss", Toast.LENGTH_SHORT).show();
                }
                else if(!pass1.getText().toString().equals(pass2.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "you must write password in two boxes", Toast.LENGTH_SHORT).show();
                    pass1.setText("");
                    pass2.setText("");
                }
                else{
                    String mailTxt=email.getText().toString(),passTxt=pass1.getText().toString();

                    mAuth.createUserWithEmailAndPassword(mailTxt,passTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                HashMap<String , String> hashMap=new HashMap<>();
                                hashMap.put("Name",name.getText().toString());
                                hashMap.put("Image","default");
                                hashMap.put("Phone",num.getText().toString());

                                FirebaseUser currentUser=mAuth.getCurrentUser();
                                uId=currentUser.getUid();

                                DatabaseReference z=FirebaseDatabase.getInstance().getReference().child("users");
                                z.child(uId).setValue(hashMap);

                                if(resultUri!=null) uploadImageInStorageDatabase(resultUri);

                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void choosePhoto() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),GALARY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_PICK && resultCode == RESULT_OK) {
            try {
                Uri imageUri=data.getData();
                CropImage.activity(imageUri).setAspectRatio(1,1).start(this);

                ParcelFileDescriptor descriptor=getContentResolver().openFileDescriptor(imageUri,"r");
                FileDescriptor fileDescriptor=descriptor.getFileDescriptor();
                bitmap= BitmapFactory.decodeFileDescriptor(fileDescriptor);

                descriptor.close();
            }
            catch (Exception e){
                Log.e("Babala","filenotfound",e);
            }
            imageView.setImageBitmap(bitmap);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            if(requestCode==RESULT_OK){
                Uri uri=result.getUri();
                resultUri=uri;
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }
        }
    }
    private void uploadImageInStorageDatabase(Uri resultUri){
        FirebaseUser currentUser=mAuth.getCurrentUser();
        uId=currentUser.getUid();

        final StorageReference filePath=mStorageReference.child("users_image").child(uId+"jpg");

        filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference mUserDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(uId);
                        mUserDatabase.child("Image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RegisterActivity.this, "Upload image successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}