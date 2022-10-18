package com.delivce.managenyama.popups;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.delivce.managenyama.R;
import com.delivce.managenyama.utils.CommonVariables;
import com.delivce.managenyama.utils.DateTimeToday;
import com.delivce.managenyama.utils.MyDialogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockPopUp implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore db;
    Context context;

    CommonVariables common = new CommonVariables();

    public final String categoryCollection = "meat_categories";
    public final String suppliersCollection = "meat_suppliers";
    public final String stockCollection = "meat_stock";

    List<Map<String, Object>> categories = new ArrayList<>();
    List<Map<String, Object>> suppliers = new ArrayList<>();

    Spinner categoriesSpinner, suppliersSpinner;
    ArrayList<String> categoryNames = new ArrayList<>();
    ArrayList<String> supplierNames = new ArrayList<>();

    DateTimeToday dateTimeToday = new DateTimeToday();


    String selectedCategory, selectedSupplier, stockMinimumValue;

    public StockPopUp(FirebaseFirestore db, Context context) {
        this.db = db;
        this.context = context;
    }

    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.stock_popup, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        String stockMinimumLimit;

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextInputEditText stockQuantity = popupView.findViewById(R.id.input_stock_quantity);
        TextInputEditText stockPrice = popupView.findViewById(R.id.et_purchase_cost);
        suppliersSpinner = popupView.findViewById(R.id.spinner_stock_supplier);
        categoriesSpinner = popupView.findViewById(R.id.stock_category_spinner);

        categoriesSpinner.setOnItemSelectedListener(this);
        suppliersSpinner.setOnItemSelectedListener(this);

        fetchCategories();
        fetchSuppliers();

        Button btnCreateStock = popupView.findViewById(R.id.btn_create_stock);

        btnCreateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewStock(
                            Float.parseFloat(stockQuantity.getText().toString()),
                            categoriesSpinner.getSelectedItem().toString(),
                            suppliersSpinner.getSelectedItem().toString(),
                            stockPrice.getText().toString());
                    popupWindow.dismiss();
                }
                catch (Exception e){
                    Log.d("POP_CATEGORY_ADD_ERROR", e.getMessage());
                }
            }
        });

        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void fetchCurrentCategory(String category) {
        db.collection(categoryCollection)
                .whereEqualTo("name", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot querySnapshot = task.getResult();

                            for(QueryDocumentSnapshot queryDocumentSnapshot: querySnapshot){
                                stockMinimumValue = (String) queryDocumentSnapshot.get("stock_minimum_limit");
                            }
                        }

                    }
                });

    }

    private void addNewStock(float quantity, String category, String supplier, String stockPrice) {
        Map<String, Object> newStock = new HashMap<>();

        newStock.put("quantity", quantity);
        newStock.put("category", category);
        newStock.put("supplier", supplier);


        MyDialogs dialog = new MyDialogs(context);
        DocumentReference documentReference= db.collection(stockCollection).document(category);

        fetchCurrentCategory(category);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String successMsg = "Stock has been added successfully";

                String time = dateTimeToday.getDateTimeToday();
                String date = dateTimeToday.getDateToday();

                if(documentSnapshot.exists()){
                    double stockQuantity  = documentSnapshot.getDouble("quantity");
                    double newQuantity = stockQuantity+(double) quantity;
                    documentReference.update("quantity", newQuantity);
                    documentReference.update("stock_minimum_limit", stockMinimumValue);

                    addNewStockMonitor(quantity, category, time, date, stockPrice);
                    dialog.createSuccessDialog(successMsg);
                }
                else{
                    newStock.put("stock_minimum_limit", Float.parseFloat(stockMinimumValue));
                    db.collection(stockCollection).document(category)
                            .set(newStock)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());

                                    addNewStockMonitor(quantity, category, time, date, stockPrice);
                                    dialog.createSuccessDialog(successMsg);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String failureMsg = "Error adding stock";
                                    Log.w("CATEGORY_ERROR", failureMsg, e);

                                    dialog.createFailureDialog(failureMsg);
                                }
                            });
                }
            }
        });
    }


    private void addNewStockMonitor(float quantity, String category, String time, String date, String stockPrice) {
        Log.d("TEST_STOCK_INSTANCE", "Called");
        Map<String, Object> newStock = new HashMap<>();

        newStock.put("category", category);
        newStock.put("stock_price", stockPrice);
//        newStock.put("status", common.STOCK_MONITOR_PENDING);
        newStock.put("stock_quantity", quantity);
        newStock.put("sale_quantity", common.STOCK_MONITOR_DEFAULT_SALE_QUANTITY);
        newStock.put("accumulated_sale_price", common.STOCK_MONITOR_DEFAULT_SALE_CUMMULATIVE_PRICE);
        newStock.put("date", date);
        newStock.put("time", time);

        db.collection(common.STOCK_MONITOR_COLLECTION)
                        .whereEqualTo("category", category)
                        .whereEqualTo("status", common.STOCK_MONITOR_ACTIVE)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty()){
                                        newStock.put("status", common.STOCK_MONITOR_ACTIVE);
                                    }
                                    else{
                                        newStock.put("status", common.STOCK_MONITOR_PENDING);
                                    }
                                    addNewCollection(newStock);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("STOCK_MONITOR_ERROR", e.getMessage());
                            }
                        });




    }

    private void addNewCollection(Map<String, Object> newStock) {
        db.collection(common.STOCK_MONITOR_COLLECTION)
                .add(newStock)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String successMsg = "Stock instance has been added successfully";
                        Log.d("STOCK_INSTANCE_SUCCESS", successMsg + " with ID: " + documentReference.getId());
//                        createSuccessDialog(successMsg);
//                        dialog.createSuccessDialog(successMsg);
                        Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String failureMsg = "Error adding sale";
                        Log.w("CATEGORY_ERROR", failureMsg, e);

//                        dialog.createFailureDialog(failureMsg);
                        Toast.makeText(context, failureMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(view.getId()){
            case R.id.stock_category_spinner:
                selectedCategory = categoryNames.get(i);
            case R.id.spinner_stock_supplier:
                selectedSupplier = supplierNames.get(i);

            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void fetchCategories(){
        db.collection(categoryCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            Log.d("FETCH_isSuccessful", "TRUE");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
                                categories.add(document.getData());
                                categoryNames.add(String.valueOf(document.get("name")));
                            }


                            Log.d("CATEGORIES_LIST", String.valueOf(categories));
                            ArrayAdapter categoryAd
                                    = new ArrayAdapter(
                                    context,
                                    android.R.layout.simple_spinner_item,
                                    categoryNames);

                            categoryAd.setDropDownViewResource(
                                    android.R.layout
                                            .simple_spinner_dropdown_item);

                            categoriesSpinner.setAdapter(categoryAd);



                        }else {

                            Log.w("FETCH_CATEGORY_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void fetchSuppliers(){
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
                                supplierNames.add(document.getString("name"));
                            }
                            Log.d("SUPPLIERS_LIST", String.valueOf(suppliers));
                            ArrayAdapter supplierAd
                                    = new ArrayAdapter(
                                    context,
                                    android.R.layout.simple_spinner_item,
                                    supplierNames);

                            supplierAd.setDropDownViewResource(
                                    android.R.layout
                                            .simple_spinner_dropdown_item);

                            suppliersSpinner.setAdapter(supplierAd);

                        }else {

                            Log.w("FETCH_SUPPLIERS_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
