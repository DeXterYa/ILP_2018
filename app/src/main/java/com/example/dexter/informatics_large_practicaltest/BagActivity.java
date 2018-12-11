package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Locale;

import javax.annotation.Nullable;

public class BagActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    Button first_button;
    Button second_button;
    Button third_button;
    Button fourth_button;

    private TextView dolr_text;
    private TextView peny_text;
    private TextView quid_text;
    private TextView shil_text;

    Toolbar mTopToolbar;

    private Double valueOfDolr;
    private Double valueOfPeny;
    private Double valueOfQuid;
    private Double valueOfShil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        first_button = findViewById(R.id.first_button);
        second_button = findViewById(R.id.second_button);
        third_button = findViewById(R.id.third_button);
        fourth_button = findViewById(R.id.fourth_button);

        dolr_text = findViewById(R.id.dolr_text);
        peny_text = findViewById(R.id.peny_text);
        quid_text = findViewById(R.id.quid_text);
        shil_text = findViewById(R.id.shil_text);

        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Coins you bought");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //noinspection CodeBlock2Expr
        mTopToolbar.setNavigationOnClickListener((View v) ->  {
                onBackPressed();
        });

        ShowCoinValue showCoinValue = new ShowCoinValue();
        showCoinValue.execute();

        first_button.setOnClickListener((View v) -> {
                if (valueOfDolr > 0) {
                    Intent intent = new Intent(BagActivity.this, ConversionActivity.class);
                    intent.putExtra("currency", "DOLR");
                    startActivity(intent);
                }
        });

        second_button.setOnClickListener((View v) -> {
                if (valueOfPeny > 0) {
                    Intent intent = new Intent(BagActivity.this, ConversionActivity.class);
                    intent.putExtra("currency", "PENY");
                    startActivity(intent);
                }
        });

        third_button.setOnClickListener((View v) -> {
                if (valueOfQuid > 0) {
                    Intent intent = new Intent(BagActivity.this, ConversionActivity.class);
                    intent.putExtra("currency", "QUID");
                    startActivity(intent);
                }
        });

        fourth_button.setOnClickListener((View v) -> {
                if (valueOfShil > 0) {
                    Intent intent = new Intent(BagActivity.this, ConversionActivity.class);
                    intent.putExtra("currency", "SHIL");
                    startActivity(intent);
                }
        });


    }


    private class ShowCoinValue extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection("User").document(firebaseUser.getUid());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            valueOfDolr = user.getDOLR();
                            valueOfPeny = user.getPENY();
                            valueOfQuid = user.getQUID();
                            valueOfShil = user.getSHIL();
                            String string_1 = "DOLR    Value: " + String.format(Locale.getDefault(), "%.2f", user.getDOLR());
                            String string_2 = "PENY    Value: " + String.format(Locale.getDefault(), "%.2f", user.getPENY());
                            String string_3 = "QUID    Value: " + String.format(Locale.getDefault(), "%.2f", user.getQUID());
                            String string_4 = "SHIL    Value: " + String.format(Locale.getDefault(), "%.2f", user.getSHIL());
                            dolr_text.setText(string_1);
                            peny_text.setText(string_2);
                            quid_text.setText(string_3);
                            shil_text.setText(string_4);
                        }

                    }
                }
            });
            return null;
        }
    }
}
