package com.example.managenyama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChickenStock extends AppCompatActivity {
    FirebaseFirestore db;
    String meat_quantity;
    String meat_price;
    String butcheryName;
    TextView costMeat, quantityMeat, butcheryTitle;

    Button updateStock, updatePrice;

    ProgressDialog progressDialog;
    BottomSheetDialog dialog;

    TextInputEditText mPrice;
    Button confirmation, cancelBtn;
    TextView priceTitle;

    TextInputEditText mQuantity;
    Button confirmationQ, cancelBtnQ;
    TextView priceTitleQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken_stock);


        Intent i = getIntent();
        String mail = i.getStringExtra("email");
        costMeat = findViewById(R.id.price_meat);
        quantityMeat = findViewById(R.id.amount_meat);
        butcheryTitle = findViewById(R.id.butchery_title);

        updateStock = findViewById(R.id.addStock);
        updatePrice = findViewById(R.id.changeCost);

        String stockName = "stock_"+mail;
        db = FirebaseFirestore.getInstance();
        DocumentReference stockRef = db.collection(stockName).document("Chicken");
        //final String[] meat_price = new String[1];

        updateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomSheetDialog(ChickenStock.this);
                dialog.setContentView(R.layout.update_stock);
                dialog.setCanceledOnTouchOutside(false);

                dialog.show();

                mQuantity = dialog.findViewById(R.id.meat_quantity);
                confirmationQ = dialog.findViewById(R.id.update_stock);
                cancelBtnQ = dialog.findViewById(R.id.cancel_button);
                priceTitleQ = dialog.findViewById(R.id.update_title);

                cancelBtnQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                confirmationQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String stockUpdated = mQuantity.getText().toString();
                        stockRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot snapshot = task.getResult();
                                String initialStock = snapshot.getString("stock_quantity_kg");
                                Float finalStock = Float.parseFloat(stockUpdated)+Float.parseFloat(initialStock);

                                if(task.isSuccessful()){
                                    stockRef.update("stock_quantity_kg", String.valueOf(finalStock))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(ChickenStock.this, "Stock has been updated", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ChickenStock.this, "Could not update stock", Toast.LENGTH_SHORT).show();
                                                    Log.d("ERROR:::", e.getMessage());
                                                }
                                            });
                                }else{
                                    Toast.makeText(ChickenStock.this, "Stock could not be updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


            }
        });

        updatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new BottomSheetDialog(ChickenStock.this);
                dialog.setContentView(R.layout.update_cost);
                dialog.setCanceledOnTouchOutside(false);

                mPrice = dialog.findViewById(R.id.update_cost_in_ksh);
                confirmation = dialog.findViewById(R.id.update_cost);
                cancelBtn = dialog.findViewById(R.id.cancel_button);
                priceTitle = dialog.findViewById(R.id.update_title);

                dialog.show();

                confirmation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String priceUpdated = mPrice.getText().toString();
                        stockRef.update("stock_price_ksh", priceUpdated)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ChickenStock.this, "Cost has been updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChickenStock.this, "Could not update cost", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR:::", e.getMessage());
                                    }
                                });
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

        stockRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Log.d("SNAPSHOT:::", "Exists");
                        //meat_price[0] = snapshot.getString("stock_price_ksh");
                        meat_quantity = snapshot.getString("stock_quantity_kg");
                        meat_price = snapshot.getString("stock_price_ksh");
                        butcheryName = snapshot.getString("butchery");

                        costMeat.setText(meat_price);
                        quantityMeat.setText(meat_quantity);
                        butcheryTitle.setText(butcheryName);

                        Log.d("SNAPSHOTDETAILS:::", ""+snapshot.getData());
                        Log.d("SNAPSHOTDETAILS1:::", meat_quantity);



                    }else{
                        Toast.makeText(ChickenStock.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                        Log.d("SNAPSHOT:::", "Does Not Exist");
                    }

                }
                else{
                    Toast.makeText(ChickenStock.this, "Unfortunately Stock data could not be fetched", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}