package com.example.dodientu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
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
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        }
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String email=mEmail.getText().toString().trim();
                final String password=mPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return ;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return ;
                }
                if(password.length()<6){
                    mPassword.setError("Password must be bigger than or equal 6 character");
                    return;
                }
                mprogressbar.setVisibility(View.VISIBLE);

                fauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(email.equals("admin@gmail.com")&& password.equals("password")){
                                Toast.makeText(LoginActivity.this, "Welcome My Creator", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Wrong user name name or password", Toast.LENGTH_SHORT).show();
                            mprogressbar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                Pair[] pair=new Pair[1];
                pair[0]=new Pair<View ,String>(tvLogin,"tvLogin");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pair);
                startActivity(intent,activityOptions.toBundle());
            }
        });
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}