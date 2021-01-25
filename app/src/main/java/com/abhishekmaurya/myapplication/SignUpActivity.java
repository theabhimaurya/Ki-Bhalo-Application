package com.abhishekmaurya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishekmaurya.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");

            binding.signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (TextUtils.isEmpty(binding.emailSignup.getText().toString().trim()) ||
                    TextUtils.isEmpty(binding.passSignup.getText().toString().trim()) ||
                            TextUtils.isEmpty(binding.userName.getText().toString().trim())) {

                        Toast.makeText(SignUpActivity.this, "please enter valid details..", Toast.LENGTH_SHORT).show();

                    }else {
                        progressDialog.show();
                        auth.createUserWithEmailAndPassword
                                (binding.emailSignup.getText().toString(), binding.passSignup.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();

                                            Users user = new Users(binding.userName.getText().toString(),
                                                    binding.emailSignup.getText().toString(), binding.passSignup.getText().toString());

                                            String id = task.getResult().getUser().getUid();
                                            database.getReference().child("Users").child(id).setValue(user);

                                            Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                            startActivity(intent);


                                            Toast.makeText(SignUpActivity.this, "Create your account successfully", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
//                                            startActivity(intent);
//                                            finish();
                                        }
                                    }
                                });


                    }


                }
            });
        }



    }
