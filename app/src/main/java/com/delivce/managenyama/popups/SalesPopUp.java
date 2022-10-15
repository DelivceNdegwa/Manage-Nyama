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
import androidx.recyclerview.widget.GridLayoutManager;

import com.delivce.managenyama.R;
import com.delivce.managenyama.adapters.MeatCategoriesAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesPopUp implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore db;
    Context context;

    public final String categoryCollection = "meat_sales";
    public final String collectionCategory= "meat_categories";
    public final String suppliersCollection = "meat_suppliers";
    public final String stockCollection = "meat_stock";
    public boolean stockUpdated = false;

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

                    updateStock(categoriesSpinner.getSelectedItem().toString(), quantitySale);

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

    private void updateStock(String category, float quantity) {

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
                            String strDate = dateTimeToday.getDateTimeToday();

                            addNewSale(quantity, category, strDate);
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

    private void addNewSale(float quantity, String category, String time) {
        Map<String, Object> newStock = new HashMap<>();

        newStock.put("quantity", quantity);
        newStock.put("category", category);
        newStock.put("time", time);

        db.collection(categoryCollection)
                .add(newStock)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String successMsg = "Sale has been added successfully";
                        Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());
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

    public void fetchCategories(){
        Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(categoryCollection)));
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
