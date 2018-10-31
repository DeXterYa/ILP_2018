package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReglogActivity extends AppCompatActivity {

    Button btn_login, btn_register;
    FirebaseUser firebaseUser1;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser1 != null){
            Intent intent = new Intent(ReglogActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglog);






        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReglogActivity.this, LoginActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReglogActivity.this, StartActivity.class));
            }
        });
    }



}
