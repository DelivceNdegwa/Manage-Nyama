package com.delivce.managenyama.popups;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import com.delivce.managenyama.R;
import com.delivce.managenyama.utils.MyDialogs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockPopUp {
    FirebaseFirestore db;
    Context context;

    public final String categoryCollection = "meat_stock";
    public final String suppliersCollection = "meat_suppliers";

    List<Map<String, Object>> categories = new ArrayList<>();
    List<Map<String, Object>> suppliers = new ArrayList<>();

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
        TextInputEditText category = popupView.findViewById(R.id.input_category);
        TextInputEditText supplier = popupView.findViewById(R.id.input_supplier);
        Button btnCreateStock = popupView.findViewById(R.id.btn_create_stock);

        btnCreateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewStock(Float.parseFloat(stockQuantity.getText().toString()), category.getText().toString(), supplier.getText().toString());
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

        db.collection(categoryCollection)
                .add(newStock)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String successMsg = "Stock has been added successfully";
                        Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());
//                        createSuccessDialog(successMsg);
                        dialog.createSuccessDialog(successMsg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String failureMsg = "Error adding stock";
                        Log.w("CATEGORY_ERROR", failureMsg, e);

                        dialog.createFailureDialog(failureMsg);
                    }
                });
    }

//    public void fetchCategories(){
//        db.collection(categoryCollection)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        String[] categoryNames = {};
//                        if(task.isSuccessful()){
//                            Log.d("FETCH_isSuccessful", "TRUE");
//                            for(QueryDocumentSnapshot document: task.getResult()){
//                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
//                                categories.add(document.getData());
//                                categoryNames.
//                            }
//
//
//                            Log.d("CATEGORIES_LIST", String.valueOf(categories));
//
//
//                        }else {
//
//                            Log.w("FETCH_CATEGORY_ERROR", "Error getting documents.", task.getException());
//                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    public void fetchSuppliers(){
//        db.collection(suppliersCollection)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            Log.d("FETCH_isSuccessful", "TRUE");
//                            for(QueryDocumentSnapshot document: task.getResult()){
//                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
//                                suppliers.add(document.getData());
//                            }
//                            Log.d("CATEGORIES_LIST", String.valueOf(suppliers));
//
//                        }else {
//
//                            Log.w("FETCH_SUPPLIERS_ERROR", "Error getting documents.", task.getException());
//                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
