package com.example.dodientu.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dodientu.MainActivity;
import com.example.dodientu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText mPassword;
    private EditText mEmail;
    private Button mLogin;
    private TextView mForgetPassword,tvLogin;
    private ImageView mCreateBtn;
    private FirebaseAuth fauth;
    private ProgressBar mprogressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassword=findViewById(R.id.passwordLogin);
        mEmail=findViewById(R.id.emailLogin);

        mLogin=findViewById(R.id.login);
        mForgetPassword=findViewById(R.id.ForgetPassword);
        tvLogin=findViewById(R.id.tvLogin);
        mCreateBtn=findViewById(R.id.signUPtext);
        mprogressbar=findViewById(R.id.progressBar1);
        fauth=FirebaseAuth.getInstance();

        if(fauth.getCurrentUser()!=null){
            if(fauth.getCurrentUser().getEmail().equals("admin@gmail.com")){
                startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                finish();
            }
            else{
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String passowrd=mPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(passowrd)){
                    mEmail.setError("Password is required");
                    return;
                }
                if(passowrd.length()<6){
                    mPassword.setError("Passowrd must be length greater than 6");
                }
                mprogressbar.setVisibility(View.VISIBLE);

                fauth.signInWithEmailAndPassword(email,passowrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(email.equals("admin@gmail.com")&& passowrd.equals("password")){
                                Toast.makeText(LoginActivity.this, "Welcome My Creator", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {// khi nhấn nút tạo thì hiển thị animotion và khi trở lại thì cũng như vậy
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)// sử dụng android 5.0 trở lên
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair(tvLogin,"tvLogin");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent,activityOptions.toBundle());
            }
        });

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetActivity.class));
            }
        });
    }
}