package com.example.managenyama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Stock extends AppCompatActivity {
    FirebaseFirestore db;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        gridView = findViewById(R.id.grid_stock);

        ArrayList<StockCardModel> stockCardModelArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();


        stockCardModelArrayList.add(new StockCardModel("Beef", R.drawable.beef, "10kg"));
        stockCardModelArrayList.add(new StockCardModel("Goat", R.drawable.goat, "10kg"));
        stockCardModelArrayList.add(new StockCardModel("Chicken", R.drawable.chicken,"10kg"));
        stockCardModelArrayList.add(new StockCardModel("Fish", R.drawable.tilapia,"10kg"));
        stockCardModelArrayList.add(new StockCardModel("Mutton", R.drawable.mutton,"10kg"));
        stockCardModelArrayList.add(new StockCardModel("Pork", R.drawable.pork,"10kg"));

        StockCardAdapter adapter = new StockCardAdapter(Stock.this, stockCardModelArrayList);
        gridView.setAdapter(adapter);
        // We will create a stock table with the documents Stock Name, Stock Quantity and Stock Price


        ArrayList<StockModel> stockModelArrayList = new ArrayList<>();
        stockModelArrayList.add(new StockModel("Beef","10", "400"));
        stockModelArrayList.add(new StockModel("Goat","10", "450"));
        stockModelArrayList.add(new StockModel("Chicken","10", "700"));
        stockModelArrayList.add(new StockModel("Fish","10", "600"));
        stockModelArrayList.add(new StockModel("Mutton","10", "500"));
        stockModelArrayList.add(new StockModel("Pork","10", "500"));


        Map<String, Object> stockItems = new HashMap<>();

        Iterator iterator = stockModelArrayList.iterator();

        while(iterator.hasNext()){
            StockModel stockModel = (StockModel)iterator.next();

            stockItems.put("stock_name", stockModel.getStockName());
            stockItems.put("stock_quantity_kg", stockModel.getStockQuantity());
            stockItems.put("stock_price_ksh", stockModel.getStockPrice());

            addStockToFirestore(stockItems, stockModel.getStockName());
        }

//        addStockToFirestore(stockItems);
        getStockPrice();


    }

    public void addStockToFirestore(Map<String, Object> stockItemsObject, String documentName){

            db.collection("Stock")
                    .document(documentName)
                    .set(stockItemsObject, SetOptions.merge());
    }

    public void getStockPrice(){
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
    }

    public void updateStockFireStore(){


    }

}