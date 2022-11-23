package com.delivce.managenyama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ButcheryRegisterActivity extends AppCompatActivity {

    TextInputEditText etButcheryName, etButcheryLocation;
    Button btnRegisterButchery;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String BUTCHERY_COLLECTION = "Butcheries";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butchery_register);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etButcheryLocation = findViewById(R.id.et_butchery_location);
        etButcheryName = findViewById(R.id.et_butchery_name);
        btnRegisterButchery = findViewById(R.id.btn_register_butchery);

        btnRegisterButchery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCredentials(
                        etButcheryName.getText().toString().trim(),
                        etButcheryLocation.getText().toString().trim()
                );
            }
        });
    }

    private void verifyCredentials(String butcheryName, String butcheryLocation) {
        if(butcheryName.isEmpty()){
            etButcheryName.setError("Input butchery name");
        }
        else if(butcheryLocation.isEmpty()){
            etButcheryLocation.setError("Input butchery location");
        }
        else{
            registerButchery(butcheryName, butcheryLocation);
        }
    }

    private void registerButchery(String butcheryName, String butcheryLocation) {
        Map<String, Object> butchery = new HashMap<>();
        String userId = mAuth.getCurrentUser().getUid();
        if(!(userId.isEmpty())){
            butchery.put("owner_id", userId);
            butchery.put("name", butcheryName);
            butchery.put("location", butcheryLocation);

            db.collection(BUTCHERY_COLLECTION)
                    .add(butchery)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ButcheryRegisterActivity.this, "Butchery registered successfully", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(ButcheryRegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ButcheryRegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this, "Please login to register your butchery", Toast.LENGTH_SHORT).show();
        }

    }
}