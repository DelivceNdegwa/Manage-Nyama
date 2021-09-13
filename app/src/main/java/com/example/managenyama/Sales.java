package com.example.managenyama;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class Sales extends AppCompatActivity {
    FirebaseFirestore db;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        ArrayList<SalesCardModel> salesCardModelArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        salesCardModelArrayList.add(new SalesCardModel("Beef",R.drawable.beef,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Goat",R.drawable.goat,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Chicken",R.drawable.chicken,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Fish",R.drawable.tilapia,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Mutton",R.drawable.mutton,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Pork",R.drawable.pork,"0"));

        gridView = findViewById(R.id.grid_sale);
        SalesCardAdapter salesAdapter = new SalesCardAdapter(Sales.this, salesCardModelArrayList);
        gridView.setAdapter(salesAdapter);

        HashMap<String, Object> saleInstance = new HashMap<>();

        saleInstance.put("meat_type", "Beef");
        saleInstance.put("meat_quantity", "1");
        saleInstance.put("meat_price", "450");
        saleInstance.put("time_of_sale", FieldValue.serverTimestamp());

        addSaleToFireStore(saleInstance, "Beef", "1", FieldValue.serverTimestamp());
    }

    public void addSaleToFireStore(HashMap<String, Object> hashObject, String salesName, String saleAmount, FieldValue timeOfSale){
        DocumentReference documentReference = db.collection("Stock").document(salesName);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.exists()){
                   String stockQuantity =  documentSnapshot.getString("stock_quantity_kg");
                   Float totalStockQuantity = Float.parseFloat(stockQuantity);
                   Float amountSale = Float.parseFloat(saleAmount);

                   if(amountSale > totalStockQuantity){
                       Toast.makeText(Sales.this, "Sorry there is not enough stock,  try a lower amount", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       String updatedStockQuantity = String.valueOf(totalStockQuantity-amountSale);
                       documentReference.update("stock_quantity_kg", updatedStockQuantity);
                       db.collection("Sales")
                               .document(timeOfSale.toString())
                               .set(hashObject, SetOptions.merge());
                   }
                }else{
                    Toast.makeText(Sales.this, "Invalid sale", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}