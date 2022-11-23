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

public class SalesPopUp implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore db;
    Context context;

    public final String saleCollection = "meat_sales";
    public final String collectionCategory= "meat_categories";
    public final String suppliersCollection = "meat_suppliers";
    public final String stockCollection = "meat_stock";
    public boolean stockUpdated = false;

    float categoryPrice = 0;

    CommonVariables common = new CommonVariables();

    List<Map<String, Object>> categories = new ArrayList<>();
    List<Map<String, Object>> suppliers = new ArrayList<>();

    ArrayList<String> categoryNames = new ArrayList<>();

    MyDialogs dialog = new MyDialogs(context);

    Spinner categoriesSpinner;
    View popupView;

    String selectedCategory;
    float quantitySale;

    public SalesPopUp(FirebaseFirestore db, Context context) {
        this.db = db;
        this.context = context;
    }

    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.sales_popup, null);

        fetchCategories();

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextInputEditText saleQuantity = popupView.findViewById(R.id.input_sale_quantity);
//        TextInputEditText category = popupView.findViewById(R.id.input_category);
        Button btnCreateSale = popupView.findViewById(R.id.btn_make_sale);
        categoriesSpinner = popupView.findViewById(R.id.categories_spinner);
//        categoriesSpinner.setOnClickListener(context);
        categoriesSpinner.setOnItemSelectedListener(this);
        btnCreateSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantitySale = Float.parseFloat(saleQuantity.getText().toString());
                    String categoryName = categoriesSpinner.getSelectedItem().toString();

                    float salePrice = getSetCategoryPrice(categoryName);

                    updateStock(categoryName, quantitySale, salePrice);

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

    private float getSetCategoryPrice(String categoryName) {

        db.collection(common.CATEGORY_COLLECTION).whereEqualTo("name", categoryName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                        categoryPrice = 0;
                    }
                });
        return categoryPrice;
    }

    private void updateStock(String category, float quantity, float salePrice) {

        DocumentReference documentReference= db.collection(stockCollection).document(category);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if(documentSnapshot.exists()){
                    double stockQuantity  = documentSnapshot.getDouble("quantity");
                    if(stockQuantity < quantity){
                        String errorMessage = "Not enough "+category+" Stock";
//                        dialog.createFailureDialog(errorMessage);
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try{
                            documentReference.update("quantity", stockQuantity-(double) quantity);

                            DateTimeToday dateTimeToday = new DateTimeToday();
                            String strTime = dateTimeToday.getDateTimeToday();
                            String strDate = dateTimeToday.getDateToday();

                            addNewSale(quantity, category, strTime, strDate, salePrice);
                        }
                        catch (Exception e){
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d("STOCK_UPDATE_ERROR", e.getMessage());
                        }
                    }
                }
            }
        });

//        db.collection(stockCollection)
//                .whereEqualTo("category", category)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                float stockQuantity = (float) document.get("quantity");
//                                float updatedQuantity = stockQuantity - quantity;
//
//                                document
//                            }
//                        } else {
//
//                        }
//                    }
//                });
    }

    private void addNewSale(float quantity, String category, String time, String date, float salePrice) {
        Map<String, Object> newStock = new HashMap<>();

        newStock.put("quantity", quantity);
        newStock.put("category", category);
        newStock.put("time", time);
        newStock.put("date", date);

        db.collection(saleCollection)
                .add(newStock)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String successMsg = "Sale has been added successfully";
                        Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());
//                        createSuccessDialog(successMsg);
//                        dialog.createSuccessDialog(successMsg);

                        updateStockMonitor(category ,quantity, salePrice);

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

    private void updateStockMonitor(String category, float quantity, float salePrice) {
        db.collection(common.STOCK_MONITOR_COLLECTION)
                .whereEqualTo("category", category)
                .whereEqualTo("status", common.STOCK_MONITOR_ACTIVE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       List<DocumentSnapshot> queryDocumentSnapshot = task.getResult().getDocuments();

                       for(DocumentSnapshot documentSnapshot: queryDocumentSnapshot){
                           long currentQuantity = documentSnapshot.getLong("sale_quantity");
                           long stockQuantity = documentSnapshot.getLong("stock_quantity");
                           long cummulativeSalePrice = documentSnapshot.getLong("accumulated_sale_price");
                           updateStockMonitorDocument(documentSnapshot.getId(), stockQuantity, currentQuantity, (long) quantity, cummulativeSalePrice, (long) salePrice);
                       }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void updateStockMonitorDocument(String id, long stockQuantity, long currentQuantity, long quantity, long cummulativeSalePrice, long salePrice) {
        long newQuantity = currentQuantity+quantity;
        long newCummulativeSalePrice = cummulativeSalePrice+salePrice;

        if((stockQuantity - newQuantity) > 0){
            db.collection(common.STOCK_MONITOR_COLLECTION)
                    .document(id)
                    .update(
                            "accumulated_sale_price", newCummulativeSalePrice,
                            "sale_quantity", newQuantity);

        }
        else{
            db.collection(common.STOCK_MONITOR_COLLECTION)
                    .document(id)
                    .update(
                            "accumulated_sale_price", newCummulativeSalePrice,
                            "sale_quantity", newQuantity,
                            "status", common.STOCK_MONITOR_DEPLETED
                            );
        }
    }


    public void fetchCategories(){
        Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(saleCollection)));
        db.collection(collectionCategory)
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
                            Log.d("CATEGORIES_LIST", String.valueOf(categoryNames));

                            ArrayAdapter ad
                                    = new ArrayAdapter(
                                    context,
                                    android.R.layout.simple_spinner_item,
                                    categoryNames);

                            ad.setDropDownViewResource(
                                    android.R.layout
                                            .simple_spinner_dropdown_item);

                            categoriesSpinner.setAdapter(ad);

                        }else {
                            Log.w("FETCH_CATEGORY_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(context,
//                        categoryNames.get(i),
//                        Toast.LENGTH_LONG)
//                .show();

        String text = adapterView.getItemAtPosition(i).toString();
//        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        Log.d("ON_CATEGORY_SELECTED", categoryNames.get(i));

        selectedCategory = categoryNames.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
