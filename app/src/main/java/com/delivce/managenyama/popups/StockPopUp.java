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

    public final String categoryCollection = "meat_categories";
    public final String suppliersCollection = "meat_suppliers";
    public final String stockCollection = "meat_stock";

    List<Map<String, Object>> categories = new ArrayList<>();
    List<Map<String, Object>> suppliers = new ArrayList<>();

    Spinner categoriesSpinner, suppliersSpinner;
    ArrayList<String> categoryNames = new ArrayList<>();
    ArrayList<String> supplierNames = new ArrayList<>();


    String selectedCategory, selectedSupplier;

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

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextInputEditText stockQuantity = popupView.findViewById(R.id.input_stock_quantity);
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
                    addNewStock(Float.parseFloat(stockQuantity.getText().toString()), categoriesSpinner.getSelectedItem().toString(), suppliersSpinner.getSelectedItem().toString());
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

    private void addNewStock(float quantity, String category, String supplier) {
        Map<String, Object> newStock = new HashMap<>();

        newStock.put("quantity", quantity);
        newStock.put("category", category);
        newStock.put("supplier", supplier);

        MyDialogs dialog = new MyDialogs(context);
        DocumentReference documentReference= db.collection(stockCollection).document(category);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String successMsg = "Stock has been added successfully";
                if(documentSnapshot.exists()){
                    double stockQuantity  = documentSnapshot.getDouble("quantity");
                    double newQuantity = stockQuantity+(double) quantity;
                    documentReference.update("quantity", newQuantity);
                    dialog.createSuccessDialog(successMsg);
                }
                else{
                    db.collection(stockCollection).document(category)
                            .set(newStock)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());
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

//        db.collection(categoryCollection)
//                .add(newStock)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        String successMsg = "Stock has been added successfully";
//                        Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());
////                        createSuccessDialog(successMsg);
//                        dialog.createSuccessDialog(successMsg);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        String failureMsg = "Error adding stock";
//                        Log.w("CATEGORY_ERROR", failureMsg, e);
//
//                        dialog.createFailureDialog(failureMsg);
//                    }
//                });
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
