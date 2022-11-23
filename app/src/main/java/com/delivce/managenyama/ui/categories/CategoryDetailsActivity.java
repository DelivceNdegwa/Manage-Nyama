package com.delivce.managenyama.ui.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.delivce.managenyama.R;
import com.delivce.managenyama.adapters.SalesAdapter;
import com.delivce.managenyama.popups.StockPopUp;
import com.delivce.managenyama.stock.StockActivity;
import com.delivce.managenyama.utils.DateTimeToday;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDetailsActivity extends AppCompatActivity {

    private RecyclerView salesRV;
    private CardView cvAlertLimit;

    public final String salesCollection = "meat_sales";
    public final String stockCollection = "meat_stock";
    public final String categoryCollection = "meat_categories";


    List<Map<String, Object>> sales = new ArrayList<>();

    FirebaseFirestore db;
    SalesAdapter salesAdapter;
    Button btnAddStock;

    ProgressDialog dialog;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        db = FirebaseFirestore.getInstance();
        btnAddStock = findViewById(R.id.btn_add_stock);

        salesRV = findViewById(R.id.rv_stock_sales);
        salesRV.setNestedScrollingEnabled(true);
        salesRV.setLayoutManager(new LinearLayoutManager(CategoryDetailsActivity.this, LinearLayoutManager.VERTICAL, false));

        Intent i = getIntent();
        category = i.getStringExtra("CATEGORY_NAME");
        cvAlertLimit = findViewById(R.id.cv_alert_limit);


        btnAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(CategoryDetailsActivity.this, StockActivity.class);
//                startActivity(intent);
                StockPopUp stockPopUp = new StockPopUp(db, CategoryDetailsActivity.this);
                stockPopUp.showPopupWindow(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog = ProgressDialog.show(CategoryDetailsActivity.this, "",
                "Fetching sales. Please wait...", true);
        dialog.show();

        sales.clear();
        fetchSales(category);
        monitorStock(category);
    }

    private void monitorStock(String category) {
        Toast.makeText(this, "Category:"+category, Toast.LENGTH_SHORT).show();

        db.collection(categoryCollection)
                .whereEqualTo("name", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();

                    }
                });

        db.collection(stockCollection)
                .whereEqualTo("category", category)
                .get()
                
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                            double quantity = queryDocumentSnapshot.getDouble("quantity");
//                            double minLimit = queryDocumentSnapshot.getString("stock_minimum_limit");

//                            if(minLimit >= quantity){
//                                cvAlertLimit.setVisibility(View.VISIBLE);
//                            }
//                            else{
//                                cvAlertLimit.setVisibility(View.GONE);
//                            }
                        }
                    }
                });
    }

    private void fetchSales(String category) {
        DateTimeToday dateTimeToday = new DateTimeToday();
        String strDate = dateTimeToday.getDateToday();

        db = FirebaseFirestore.getInstance();
        Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(salesCollection)));
        db.collection(salesCollection)
//                .whereEqualTo("date", strDate)
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("FETCH_isSuccessful", "TRUE");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
                                sales.add(document.getData());
                            }
                            Log.d("SALES_LIST", String.valueOf(sales));

                            salesAdapter = new SalesAdapter(CategoryDetailsActivity.this, sales);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(CategoryDetailsActivity.this, LinearLayoutManager.VERTICAL, false);

                            salesRV.setLayoutManager(layoutManager);
                            salesRV.setAdapter(salesAdapter);
                            dialog.dismiss();
                        }else {

                            Log.w("FETCH_SUPPLIERS_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(CategoryDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }
}