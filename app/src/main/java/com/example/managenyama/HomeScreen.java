package com.example.managenyama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreen extends AppCompatActivity {
    MaterialCardView stockCard, saleCard, profileCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        stockCard = findViewById(R.id.stock_id);
        saleCard = findViewById(R.id.sale_id);
        profileCard = findViewById(R.id.profile_id);

        stockCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, Stock.class);
                startActivity(i);
            }
        });

        saleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, Sales.class);
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