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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.delivce.managenyama.R;
import com.delivce.managenyama.adapters.MeatCategoriesAdapter;
import com.delivce.managenyama.utils.MyDialogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuppliersPopUp {
    FirebaseFirestore db;
    Context context;

    List<Map<String, Object>> categories = new ArrayList<>();
    List<Map<String, Object>> suppliers = new ArrayList<>();

    public final String categoryCollection = "meat_categories";

    public final String suppliersCollection = "meat_suppliers";

    public SuppliersPopUp(FirebaseFirestore db, Context context) {
        this.db = db;
        this.context = context;
    }

    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.suppliers_pop_up, null);

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

        TextInputEditText supplierName = popupView.findViewById(R.id.input_supplier_name);
        TextInputEditText supplierPhone = popupView.findViewById(R.id.input_supplier_phone_number);
        TextInputEditText location = popupView.findViewById(R.id.input_supplier_location);
        Button btnCreateSupplier = popupView.findViewById(R.id.btn_create_supplier);

        btnCreateSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewSupplier(supplierName.getText().toString(), supplierPhone.getText().toString(), location.getText().toString());
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

    private void addNewSupplier(String name, String phoneNumber, String location) {
        Map<String, Object> newCategory = new HashMap<>();
        newCategory.put("name", name);
        newCategory.put("phone_number", phoneNumber);
        newCategory.put("location", location);

        MyDialogs dialog = new MyDialogs(context);

        db.collection(suppliersCollection)
                .add(newCategory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String successMsg = "Supplier added successfully";
                        Log.d("SUPPLIER_SUCCESS", successMsg + " with ID: " + documentReference.getId());
//                        createSuccessDialog(successMsg);
                        dialog.createSuccessDialog(successMsg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String failureMsg = "Error adding supplier";
                        Log.w("CATEGORY_ERROR", failureMsg, e);

                        dialog.createFailureDialog(failureMsg);
                    }
                });
    }

}
