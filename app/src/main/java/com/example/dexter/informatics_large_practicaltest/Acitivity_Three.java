package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Acitivity_Three extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "FriendsSystem";
    private static final String COLLECTION_KEY = "Chat";
    private static final String DOCUMENT_KEY = "Message";
    private static final String NAME_FIELD = "Name";
    private static final String TEXT_FIELD = "Text";

    private FirebaseFirestore firestore;
    private DocumentReference filestoreChat;}

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_three);
//        mAuth = FirebaseAuth.getInstance();
//
//
//
//
//
//
//        // Navigation bar
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        Menu menu = bottomNavigationView.getMenu();
//        MenuItem menuItem= menu.getItem(3);
//        menuItem.setChecked(true);
//        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
//            switch (item.getItemId()) {
//                case R.id.navigation_map:
//                    Intent intent1 = new Intent(Acitivity_Three.this, Activity_One.class);
//                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivityForResult(intent1,0);
//                    overridePendingTransition(0,0);
//                    break;
//
//                case R.id.navigation_coins:
//                    Intent intent2 = new Intent(Acitivity_Three.this, Activity_Two.class);
//                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivityForResult(intent2,0);
//                    overridePendingTransition(0,0);
//                    break;
//
//                case R.id.navigation_friends:
////                    Intent intent3 = new Intent(Acitivity_Three.this, Acitivity_Three.class);
////                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////                    startActivityForResult(intent3,0);
////                    overridePendingTransition(0,0);
//                    break;
//                case R.id.navigation_welcome:
//                    Intent intent4 = new Intent(Acitivity_Three.this, MainActivity.class);
//                    intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivityForResult(intent4,0);
//                    overridePendingTransition(0,0);
//                    break;
//
//
//            }
//            return false;
//        });
//    }
//
//
//
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//
//    private void createAccount(String email, String password) {
//        Log.d(TAG, "createAccount:" + email);
//        if (!validateForm()) {
//            return;
//        }
//
//        showProgressDialog();
//
//        // [START create_user_with_email]
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(Acitivity_Three.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // [START_EXCLUDE]
//                        hideProgressDialog();
//                        // [END_EXCLUDE]
//                    }
//                });
//        // [END create_user_with_email]
//    }
//
//
//}
