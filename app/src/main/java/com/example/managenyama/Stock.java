package com.example.managenyama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Stock extends AppCompatActivity {
    FirebaseFirestore db;
    StockModel model;
    MaterialCardView beefStock, goatStock, chickenStock, fishStock, muttonStock, porkStock;
    //String stock_quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);



        beefStock=findViewById(R.id.beef_stock);
        goatStock=findViewById(R.id.goat_stock);
        chickenStock=findViewById(R.id.chicken_stock);
        fishStock=findViewById(R.id.fish_stock);
        muttonStock=findViewById(R.id.mutton_stock);
        porkStock=findViewById(R.id.pork_stock);



        Intent i = getIntent();
        //String butchery_name = i.getStringExtra("butchery_name");
        String mail = i.getStringExtra("email");

        beefStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Stock.this, BeefStock.class);
                i.putExtra("email", mail);
                startActivity(i);
            }
        });

        goatStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Stock.this, GoatStock.class);
                i.putExtra("email", mail);
                startActivity(i);
            }
        });

        chickenStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Stock.this, ChickenStock.class);
                i.putExtra("email", mail);
                startActivity(i);
            }
        });

        fishStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Stock.this, FishStock.class);
                i.putExtra("email", mail);
                startActivity(i);
            }
        });

        muttonStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Stock.this, MuttonStock.class);
                i.putExtra("email", mail);
                startActivity(i);
            }
        });

        porkStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Stock.this, PigStock.class);
                i.putExtra("email", mail);
                startActivity(i);
            }
        });


        // We will create a stock table with the documents Stock Name, Stock Quantity and Stock Price

    }


    public void getStockQuantity(String stockName, String documentName){
        DocumentReference stockRef = db.collection(stockName).document(documentName);
        //final String[] meat_price = new String[1];

        stockRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Log.d("SNAPSHOT:::", "Exists");
                        //meat_price[0] = snapshot.getString("stock_price_ksh");
                        String meat_quantity = snapshot.getString("stock_quantity_kg");
                        String meat_price = snapshot.getString("stock_price_ksh");
                        String butcheryName = snapshot.getString("butchery");

                        model = new StockModel(stockName, meat_quantity, meat_price, butcheryName);

                        Log.d("SNAPSHOTDETAILS:::", ""+snapshot.getData());
                        Log.d("SNAPSHOTDETAILS1:::", meat_quantity);


                    }else{
                        Toast.makeText(Stock.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                        Log.d("SNAPSHOT:::", "Does Not Exist");
                    }

                }
                else{
                    Toast.makeText(Stock.this, "Unfortunately Stock data could not be fetched", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

   /* public void getStockQuantity(String collectionName, String documentName){
        DocumentReference stockRef = db.collection(collectionName).document(documentName);

        stockRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Log.d("SNAPSHOT:::", "Exists");
                    }else{
                        Toast.makeText(Stock.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(Stock.this, "Unfortunately Stock data could not be fetched", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
*/
/*    public void getStockPrice(){
        db.collection("Butchery")
                .whereEqualTo("stock_price", "10kg")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Toast.makeText(Stock.this, "Success on fetching data", Toast.LENGTH_SHORT).show();
                        Log.d("onSuccessData:::", queryDocumentSnapshots.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("onFailureData:::", "Failed to fetch data");
                    }
                });
    }*/

    public void updateStockFireStore(){


    }

}