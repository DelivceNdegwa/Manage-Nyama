package com.delivce.managenyama.authentication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.delivce.managenyama.ButcheryRegisterActivity;
import com.delivce.managenyama.MainActivity;
import com.delivce.managenyama.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText etFirstName, etLastName, etPhone, etEmail, etPassword, etConfirmPassword;
    Button btnSignUp, btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String USER_COLLECTION = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = FirebaseFirestore.getInstance();

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etPhone = findViewById(R.id.et_phone_number);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        btnSignUp = findViewById(R.id.btn_sign_up);
        btnLogin = findViewById(R.id.login_btn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyDetails(
                        etFirstName.getText().toString().trim(),
                        etLastName.getText().toString().trim(),
                        etPhone.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etPassword.getText().toString().trim(),
                        etConfirmPassword.getText().toString().trim()
                );


            }
        });
    }

    private void verifyDetails(String firstName, String lastName, String phoneNumber, String email, String password, String confirmPassword){
        AtomicInteger emptyField = new AtomicInteger();
        HashMap<String, TextInputEditText> valueInputFieldMap = new HashMap<>();
        valueInputFieldMap.put(firstName, etFirstName);
        valueInputFieldMap.put(lastName, etLastName);
        valueInputFieldMap.put(phoneNumber, etPhone);
        valueInputFieldMap.put(email, etEmail);
        valueInputFieldMap.put(password, etPassword);
        valueInputFieldMap.put(confirmPassword, etConfirmPassword);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            valueInputFieldMap.forEach((inputValue, inputField) -> {
                if(inputValue.isEmpty()){
                    inputField.setError("Please input a value here");
                    emptyField.addAndGet(1);
                }
            });
        }

        if(!email.contains("@")){
            etEmail.setError("Please input a valid email");
            return;
        }
        else if(emptyField.get() > 0) {
            return;
        }
        else if(!password.equals(confirmPassword)){
            etPassword.setError("Passwords did not match");
            return;
        }
        else{
            createAccount(email, password, phoneNumber, firstName, lastName);
        }

    }

    private void createAccount(String email, String password, String phone, String firstName, String lastName){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                            Toast.makeText(SignUpActivity.this, "Account created", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });

        Map<String, Object>  user = new HashMap<>();
        user.put("email", email);
        user.put("phone_number", phone);
        user.put("first_name", firstName);
        user.put("last_name", lastName);


        db.collection(USER_COLLECTION)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        signIn(email, password);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("USER_ONFAILURE",e.getMessage());
                        Toast.makeText(SignUpActivity.this, "Details not saved, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            Intent intent = new Intent(SignUpActivity.this, ButcheryRegisterActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }



}