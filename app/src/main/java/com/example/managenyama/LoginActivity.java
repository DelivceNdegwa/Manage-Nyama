package com.example.managenyama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText emailUser, passwordUser;
    Button loginButton, signUpButton, forgotPasswordButton;
    FirebaseFirestore db;

    // Bottom Sheet Dialog
    BottomSheetDialog dialog;
    TextInputEditText firstName, lastName, butcheryName, emailSignUp, passwordSignUp, confirmPassword;
    Button signUpBtn, cancelBtn;

    ProgressDialog progressDialog;




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        emailUser = findViewById(R.id.user_email);
        passwordUser = findViewById(R.id.user_password);
        loginButton = findViewById(R.id.btnLogin);
        signUpButton = findViewById(R.id.btnSignUp);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailUser.getText().toString();
                String password = passwordUser.getText().toString();

                validateLoginCredentials(email, password);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog = new BottomSheetDialog(LoginActivity.this);
                dialog.setContentView(R.layout.sign_up);
                dialog.setCanceledOnTouchOutside(false);

                 db = FirebaseFirestore.getInstance();

                firstName = dialog.findViewById(R.id.first_name);
                lastName = dialog.findViewById(R.id.last_name);
                butcheryName = dialog.findViewById(R.id.butchery_name);
                emailSignUp = dialog.findViewById(R.id.signup_email);
                passwordSignUp = dialog.findViewById(R.id.signup_password);
                confirmPassword = dialog.findViewById(R.id.password_confirm);
                signUpBtn = dialog.findViewById(R.id.sign_up);
                cancelBtn = dialog.findViewById(R.id.cancel_button);

                dialog.show();

                signUpBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String first_name = firstName.getText().toString();
                        String last_name = lastName.getText().toString();
                        String butchery_name = butcheryName.getText().toString();
                        String email_signUp = emailSignUp.getText().toString();
                        String password_signUp = passwordSignUp.getText().toString();
                        String confirm_password = confirmPassword.getText().toString();


                        validateUserCredentials(first_name, last_name, butchery_name, email_signUp, password_signUp, confirm_password);

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        }

    public void validateLoginCredentials(String email, String password){
        if(email.trim().isEmpty()){
            emailUser.setError("This field cannot be empty");
        }
        else if(!isValidEmail(email)){
            emailUser.setError("Email is invalid");
        }
        else if(password.trim().isEmpty()){
            passwordUser.setError("This field cannot be empty");
        }
        else{
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Login you in...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            signInUser(email,password);
        }
    }


    public void validateUserCredentials(String first_name, String last_name, String butchery_name, String email, String password, String confirm_password){
        if(first_name.trim().isEmpty()){
            firstName.setError("This field cannot be empty");
        }
        else if(last_name.trim().isEmpty()){
            lastName.setError("This field cannot be empty");
        }
        else if(butchery_name.trim().isEmpty()){
            butcheryName.setError("This field cannot be empty");
        }
        else if(email.trim().isEmpty()){
            emailSignUp.setError("This field cannot be empty");
        }
        else if(password.trim().isEmpty()){
            passwordSignUp.setError("This field cannot be empty");
        }
        else if(confirm_password.trim().isEmpty()){
            confirmPassword.setError("This field cannot be empty");
        }
        else if(!password.equals(confirm_password)){
            passwordSignUp.setError("Passwords did not match");
            confirmPassword.setError("Passwords did not match");
        }
        else if(password.length() < 6){
            passwordSignUp.setError("Password needs 6 or more characters");
        }
        else{

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Signing up...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            createNewUser(first_name, last_name, butchery_name, email, password);
        }

    }


    public void createNewUser(String first_name, String last_name, String butchery_name, String email, String password){
        createUserCredentialsAccount(email, password, butchery_name);

        ArrayList<StockModel> stockModelArrayList = new ArrayList<>();
        stockModelArrayList.add(new StockModel("Beef","0", "400", "ButcheryName"));
        stockModelArrayList.add(new StockModel("Goat","0", "450","ButcheryName"));
        stockModelArrayList.add(new StockModel("Chicken","0", "700", "ButcheryName"));
        stockModelArrayList.add(new StockModel("Fish","0", "600", "ButcheryName"));
        stockModelArrayList.add(new StockModel("Mutton","0", "500", "ButcheryName"));
        stockModelArrayList.add(new StockModel("Pork","0", "500", "ButcheryName"));



        Map<String, Object> stockItems = new HashMap<>();

        Iterator iterator = stockModelArrayList.iterator();
        StockModel stockModel;
        CollectionReference stockButchery = db.collection("stock_"+email);

        while(iterator.hasNext()){
            stockModel = (StockModel)iterator.next();
            stockItems.put("stock_name", stockModel.getStockName());
            stockItems.put("stock_quantity_kg", "0");
            stockItems.put("stock_price_ksh", stockModel.getStockPrice());
            stockItems.put("butchery", butchery_name);

            stockButchery.document(stockModel.getStockName())
                    .set(stockItems, SetOptions.merge());

        }

        db.collection("Sales");


        HashMap<String, Object> map = new HashMap<>();

        map.put("first_name", first_name);
        map.put("last_name",last_name);
        map.put("butchery_name", butchery_name);
        map.put("email", email);



        db.collection("Users")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(LoginActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                        Log.d("onFailureMessage:::", "Error: "+e);
                    }
                });



    }

    public void createUserCredentialsAccount(String email, String password, String butchery_name){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            DocumentReference reference = db.collection("Users").document(user.getUid());

                            Intent i = new Intent(LoginActivity.this, HomeScreen.class);
                            i.putExtra("UserInfo", user);
                            i.putExtra("Butchery", butchery_name);
                            i.putExtra("email", email);
                            startActivity(i);

                            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot snapshot = task.getResult();
                                        if(snapshot.exists()){
                                            Log.d("SnapShot:::", "Exists");

                                        }
                                        else{
                                            Log.d("SnapShot:::", "Does not exist");
                                        }
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    public void signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeScreen.class);
                            i.putExtra("UserInfo", user);
                            i.putExtra("email", email);
                            startActivity(i);
//                            updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void logUserOut(){
        FirebaseAuth.getInstance().signOut();
    }

}