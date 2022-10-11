package com.delivce.managenyama.ui.categories;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.adapters.MeatCategoriesAdapter;
import com.delivce.managenyama.popups.CategoryPopUp;
import com.delivce.managenyama.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView categoriesRv;
    private TextView tvNewCategory;
    private ImageView ivNewCategory;

    private MeatCategoriesAdapter meatCategoriesAdapter;

    List<Map<String, Object>> categories = new ArrayList<>();
    ProgressDialog dialog;

    FirebaseFirestore db;
    public final String categoryCollection = "meat_categories";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CategoriesViewModel homeViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        categoriesRv = binding.rvCategories;
        tvNewCategory = binding.tvAddCategory;
        ivNewCategory = binding.ivAddCategory;

        db = FirebaseFirestore.getInstance();

        tvNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryPopUp categoryPopUp = new CategoryPopUp(db, requireActivity());
                categoryPopUp.showPopupWindow(view);
            }
        });

        ivNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryPopUp categoryPopUp = new CategoryPopUp(db, requireActivity());
                categoryPopUp.showPopupWindow(view);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        dialog = ProgressDialog.show(requireActivity(), "",
                "Fetching categories. Please wait...", true);

        dialog.show();
        categories.clear();
        fetchCategories();

    }

    private void testFirestore(){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FIRESTORE_SUCCCESS", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIRESTORE_FAILURE", "Error adding document", e);
                    }
                });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FIRESTORE_READ_SUCCESS", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("FIRESTORE_READ_ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }



    private void fetchCategories() {
       Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(categoryCollection)));
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
                           }
                           Log.d("CATEGORIES_LIST", String.valueOf(categories));

                           meatCategoriesAdapter = new MeatCategoriesAdapter(requireActivity(), categories);
                           GridLayoutManager layoutManager=new GridLayoutManager(requireActivity(),2);

                           categoriesRv.setLayoutManager(layoutManager);
                           categoriesRv.setAdapter(meatCategoriesAdapter);
                           dialog.dismiss();
                       }else {

                           Log.w("FETCH_CATEGORY_ERROR", "Error getting documents.", task.getException());
                           Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                           dialog.dismiss();
                       }
                   }
               });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}