package com.example.managenyama;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class Sales extends AppCompatActivity {
    FirebaseFirestore db;
    BottomSheetDialog dialog;

    Button addSale;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        addSale = findViewById(R.id.add_sale);

        Intent i = getIntent();
        String mail = i.getStringExtra("email");
        String butcheryName = i.getStringExtra("butchery_name");

        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomSheetDialog(Sales.this);
                dialog.setContentView(R.layout.make_sale);
                dialog.setCanceledOnTouchOutside(false);

                TextInputEditText meatQuantity, meatType;
                Button saleConfirm;

                meatQuantity = dialog.findViewById(R.id.meat_quantity);
                meatType = dialog.findViewById(R.id.meat_type);
                saleConfirm = dialog.findViewById(R.id.sale_confirm);
                dialog.show();

                saleConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(meatQuantity.getText().toString().trim().isEmpty()){
                            meatQuantity.setError("This field cannot be empty");
                        }
                        else if( meatType.getText().toString().trim().isEmpty()){
                            meatType.setError("This field cannot be empty");
                        }
                        else{
                            progressDialog = new ProgressDialog(Sales.this);
                            progressDialog.setMessage("Adding Sale...");
                            progressDialog.setCancelable(false);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show();


                            db = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = db.collection("stock_"+mail).document( meatType.getText().toString());
                            Log.d("Tag:::", "stock_"+mail);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String saleAmount = meatQuantity.getText().toString();
                                    FieldValue timeOfSale;

                                    Log.d("Tag:::", "stock_"+mail);

                                    if(documentSnapshot.exists()){
                                        String stockQuantity =  documentSnapshot.getString("stock_quantity_kg");
                                        Float totalStockQuantity = Float.parseFloat(stockQuantity);
                                        Float amountSale = Float.parseFloat(saleAmount);

                                        if(amountSale > totalStockQuantity){
                                            Toast.makeText(Sales.this, "Sorry there is not enough stock,  try a lower amount", Toast.LENGTH_SHORT).show();
                                            Log.d("Tag:::", "less quantity");
                                            progressDialog.dismiss();
                                        }
                                        else{
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("meat_type", meatType.getText().toString());
                                            map.put("meat_quantity", meatQuantity.getText().toString());
                                            map.put("meat_price", documentSnapshot.getString("stock_price_ksh"));
                                            map.put("time_of_sale", FieldValue.serverTimestamp());

                                            String updatedStockQuantity = String.valueOf(totalStockQuantity-amountSale);
                                            documentReference.update("stock_quantity_kg", updatedStockQuantity);
                                            db.collection("sales_"+mail)
                                                    .document(String.valueOf(FieldValue.serverTimestamp()))
                                                    .set(map, SetOptions.merge());

                                            progressDialog.dismiss();
                                        }
                                    }else{
                                        Toast.makeText(Sales.this, "Invalid sale", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });

            }
        });

/*
        ArrayList<SalesCardModel> salesCardModelArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        salesCardModelArrayList.add(new SalesCardModel("Beef",R.drawable.beef,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Goat",R.drawable.goat,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Chicken",R.drawable.chicken,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Fish",R.drawable.tilapia,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Mutton",R.drawable.mutton,"0"));
        salesCardModelArrayList.add(new SalesCardModel("Pork",R.drawable.pork,"0"));



        HashMap<String, Object> saleInstance = new HashMap<>();

        saleInstance.put("meat_type", "Beef");
        saleInstance.put("meat_quantity", "1");
        saleInstance.put("meat_price", "450");
        saleInstance.put("time_of_sale", FieldValue.serverTimestamp());

        addSaleToFireStore(saleInstance, "Beef", "1", FieldValue.serverTimestamp());*/
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