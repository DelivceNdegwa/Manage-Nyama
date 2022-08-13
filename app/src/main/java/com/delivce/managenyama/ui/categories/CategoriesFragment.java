package com.delivce.managenyama.ui.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.R;
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

    List<Map<String, Object>> categories = new ArrayList<>();

    FirebaseFirestore db;
    public final String categoryCollection = getResources().getString(R.string.CATEGORY_COLLECTION);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CategoriesViewModel homeViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        categoriesRv = binding.rvCategories;

        db = FirebaseFirestore.getInstance();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchCategories();

    }

    private void fetchCategories() {
       db.collection(categoryCollection)
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           for(QueryDocumentSnapshot document: task.getResult()){
                               Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
                               categories.add(document.getData());
                           }
                       }else {
                           Log.w("FETCH_CATEGORY_ERROR", "Error getting documents.", task.getException());
                       }
                   }
               });
    }

    private void addNewCategory(long id, String name, float defaultPrice, long ownerId) {
        Map<String, Object> newCategory = new HashMap<>();
        newCategory.put("id", id);
        newCategory.put("name", name);
        newCategory.put("default_price", defaultPrice);
        newCategory.put("owner_id", ownerId);


        db.collection(categoryCollection)
                .add(newCategory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String successMsg = "Category has been added successfully";
                        Log.d("CATEGORY_SUCCESS", successMsg + " with ID: " + documentReference.getId());
                        createSuccessDialog(successMsg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String failureMsg = "Error adding document";
                        Log.w("CATEGORY_ERROR", failureMsg, e);
                        createFailureDialog(failureMsg);
                    }
                });
    }



    // Dialogs
    private void createSuccessDialog(String successMsg) {
    }

    private void createFailureDialog(String failureMsg) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}