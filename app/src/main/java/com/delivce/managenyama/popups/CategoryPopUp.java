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

import java.util.HashMap;
import java.util.Map;

public class CategoryPopUp {
    FirebaseFirestore db;
    Context context;

    public final String categoryCollection = "meat_categories";

    public CategoryPopUp(FirebaseFirestore db, Context context) {
        this.db = db;
        this.context = context;
    }

    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.category_popup, null);

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

        TextInputEditText categoryName = popupView.findViewById(R.id.input_category_name);
        TextInputEditText categoryPrice = popupView.findViewById(R.id.input_category_price);
        TextInputEditText etMinimumStockAmount = popupView.findViewById(R.id.input_stock_minimum_limit);
        Button btnCreateCategory = popupView.findViewById(R.id.btn_create_category);

        btnCreateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewCategory(
                            categoryName.getText().toString(),
                            Float.parseFloat(categoryPrice.getText().toString()),
                            13,
                            Double.parseDouble(etMinimumStockAmount.getText().toString())
                    );
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

    private void addNewCategory(String name, float defaultPrice, long ownerId, double stockLimit) {
        Map<String, Object> newCategory = new HashMap<>();
        newCategory.put("name", name);
        newCategory.put("default_price", defaultPrice);
        newCategory.put("owner_id", ownerId);
        newCategory.put("stock_minimum_limit", stockLimit);

        MyDialogs dialog = new MyDialogs(context);

        db.collection(categoryCollection)
                .add(newCategory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String successMsg = "Category has been added successfully";
                        Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());
//                        createSuccessDialog(successMsg);
                        dialog.createSuccessDialog(successMsg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String failureMsg = "Error adding category";
                        Log.w("CATEGORY_ERROR", failureMsg, e);

                        dialog.createFailureDialog(failureMsg);
                    }
                });
    }


}
