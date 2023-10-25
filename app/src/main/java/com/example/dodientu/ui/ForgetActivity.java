package com.example.dodientu.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dodientu.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {
    private Button forget;
    private EditText inputEmail;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        auth=FirebaseAuth.getInstance();

        forget=findViewById(R.id.forgetBtn);
        inputEmail=findViewById(R.id.editEmail);
        progressBar=findViewById(R.id.progressBar);


        forget.setOnClickListener(view -> {
            String email=inputEmail.getText().toString();
            if(!email.equals("")){
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        Toast.makeText(this, "Password has been to your email please check your inbox", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(this, "Please input email or name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}