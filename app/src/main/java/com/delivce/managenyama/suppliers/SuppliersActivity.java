package com.delivce.managenyama.suppliers;

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
import com.delivce.managenyama.adapters.MeatCategoriesAdapter;
import com.delivce.managenyama.adapters.SuppliersAdapter;
import com.delivce.managenyama.popups.CategoryPopUp;
import com.delivce.managenyama.popups.SuppliersPopUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuppliersActivity extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView rvSuppliers;
    TextView tvAddSupplier;
    ImageView ivAddSupplier;
    SuppliersAdapter suppliersAdapter;

    List<Map<String, Object>> suppliers = new ArrayList<>();
    ProgressDialog dialog;

    public final String suppliersCollection = "meat_suppliers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);

        db = FirebaseFirestore.getInstance();
        rvSuppliers = findViewById(R.id.rv_suppliers);

        tvAddSupplier = findViewById(R.id.tv_add_supplier);
        ivAddSupplier = findViewById(R.id.iv_add_supplier);

        tvAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuppliersPopUp suppliersPopUp = new SuppliersPopUp(db, SuppliersActivity.this);
                suppliersPopUp.showPopupWindow(view);
            }
        });
        ivAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuppliersPopUp suppliersPopUp = new SuppliersPopUp(db, SuppliersActivity.this);
                suppliersPopUp.showPopupWindow(view);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        dialog = ProgressDialog.show(SuppliersActivity.this, "",
                "Fetching suppliers. Please wait...", true);
        dialog.show();

        suppliers.clear();
        fetchSuppliers();
    }

    private void fetchSuppliers() {
        Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(suppliersCollection)));
        db.collection(suppliersCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("FETCH_isSuccessful", "TRUE");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
                                suppliers.add(document.getData());
                            }
                            Log.d("CATEGORIES_LIST", String.valueOf(suppliers));

                            suppliersAdapter = new SuppliersAdapter(SuppliersActivity.this, suppliers);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(SuppliersActivity.this, LinearLayoutManager.VERTICAL, false);

                            rvSuppliers.setLayoutManager(layoutManager);
                            rvSuppliers.setAdapter(suppliersAdapter);
                            dialog.dismiss();
                        }else {

                            Log.w("FETCH_SUPPLIERS_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(SuppliersActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }
}