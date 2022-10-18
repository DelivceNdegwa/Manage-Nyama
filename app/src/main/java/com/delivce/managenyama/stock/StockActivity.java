package com.delivce.managenyama.stock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delivce.managenyama.R;
import com.delivce.managenyama.adapters.StockAdapter;
import com.delivce.managenyama.adapters.SuppliersAdapter;
import com.delivce.managenyama.popups.StockPopUp;
import com.delivce.managenyama.popups.SuppliersPopUp;
import com.delivce.managenyama.suppliers.SuppliersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView rvStock;
    TextView tvAddStock;
    ImageView ivAddStock;
    StockAdapter stockAdapter;

    List<Map<String, Object>> stocks = new ArrayList<>();
    ProgressDialog dialog;

    public final String stockCollection = "meat_stock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        db = FirebaseFirestore.getInstance();
        rvStock = findViewById(R.id.rv_stock);
        tvAddStock = findViewById(R.id.tv_add_stock);
        ivAddStock = findViewById(R.id.iv_add_stock);

        tvAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(StockActivity.this, "CLICKED", Toast.LENGTH_SHORT).show();
                StockPopUp stockPopUp = new StockPopUp(db, StockActivity.this);
                stockPopUp.showPopupWindow(view);
            }
        });
        ivAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockPopUp stockPopUp = new StockPopUp(db, StockActivity.this);
                stockPopUp.showPopupWindow(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        dialog = ProgressDialog.show(StockActivity.this, "",
                "Fetching stock. Please wait...", true);
        dialog.show();

        stocks.clear();

        fetchStock();
    }

    private void fetchStock() {
        Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(stockCollection)));
        db.collection(stockCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("FETCH_isSuccessful", "TRUE");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
                                stocks.add(document.getData());
                            }
                            Log.d("CATEGORIES_LIST", String.valueOf(stocks));

                            stockAdapter = new StockAdapter(StockActivity.this, stocks);
//                            LinearLayoutManager layoutManager=new LinearLayoutManager(StockActivity.this, LinearLayoutManager.VERTICAL, false);
                            GridLayoutManager layoutManager=new GridLayoutManager(StockActivity.this,2);
                            rvStock.setLayoutManager(layoutManager);
                            rvStock.setAdapter(stockAdapter);
                            dialog.dismiss();
                        }else {

                            Log.w("FETCH_SUPPLIERS_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(StockActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }
}