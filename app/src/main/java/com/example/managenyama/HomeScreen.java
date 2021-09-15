package com.example.managenyama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreen extends AppCompatActivity {
    MaterialCardView stockCard, saleCard, profileCard, reportCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        stockCard = findViewById(R.id.stock_id);
        saleCard = findViewById(R.id.sale_id);
        profileCard = findViewById(R.id.profile_id);
        reportCard = findViewById(R.id.report_id);


        Intent i = getIntent();

        String butchery_name = i.getStringExtra("butchery_name");
        String mail = i.getStringExtra("email");

        stockCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, Stock.class);
                i.putExtra("butchery_name", butchery_name);
                i.putExtra("email", mail);
                startActivity(i);
            }
        });

        saleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, Sales.class);
                i.putExtra("butchery_name", butchery_name);
                i.putExtra("email", mail);
                startActivity(i);

            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, Profile.class);
                startActivity(i);
            }
        });
    }
}