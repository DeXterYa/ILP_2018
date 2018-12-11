package com.example.dexter.informatics_large_practicaltest;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;


public class StartActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button btn_register;


    FirebaseAuth mAuth;
    DatabaseReference reference;
    private DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register");
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener((View v) -> {
            if (username.getText() != null && email.getText() != null && password.getText() != null ) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(StartActivity.this, "Please complete all the information", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(StartActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);
                }
            }
        });
        //noinspection CodeBlock2Expr
        toolbar.setNavigationOnClickListener((View v) -> {
                onBackPressed();
        });


    }





    // Initialize the user's information
    private void register (String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userid = user.getUid();


                                documentReference = FirebaseFirestore.getInstance().collection("User").document(userid);

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("username", username);
                                hashMap.put("imageURL", "default");
                                hashMap.put("status", "offline");
                                hashMap.put("search", username.toLowerCase());
                                hashMap.put("SHIL", 0.0);
                                hashMap.put("DOLR", 0.0);
                                hashMap.put("QUID", 0.0);
                                hashMap.put("PENY", 0.0);
                                hashMap.put("GOLD", 0.0);
                                hashMap.put("level", 0.0);


                                documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }
}
