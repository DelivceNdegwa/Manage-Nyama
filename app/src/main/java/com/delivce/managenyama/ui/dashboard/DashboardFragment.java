package com.delivce.managenyama.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.adapters.HistoryAdapter;
import com.delivce.managenyama.adapters.SalesAdapter;
import com.delivce.managenyama.databinding.FragmentDashboardBinding;
import com.delivce.managenyama.history.HistoryActivity;
import com.delivce.managenyama.models.History;
import com.delivce.managenyama.popups.SalesPopUp;
import com.delivce.managenyama.stock.StockActivity;
import com.delivce.managenyama.suppliers.SuppliersActivity;
import com.delivce.managenyama.utils.DateTimeToday;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    // views
    private RecyclerView historyRV;

    public final String salesCollection = "meat_sales";
    List<Map<String, Object>> sales = new ArrayList<>();

    //lists
    List<History> historyList = new ArrayList<>();
    FirebaseFirestore db;
    ProgressDialog dialog;

    //adapters
    HistoryAdapter historyAdapter;
    SalesAdapter salesAdapter;

    Button btnAddSaleHome;

    ConstraintLayout suppliersLayout, historyLayout, salesLayout, stockLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        btnAddSaleHome = binding.btnAddSaleHome;


        historyRV = binding.rvHistory;
        historyRV.setNestedScrollingEnabled(true);
        historyRV.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));

        historyLayout = binding.ivHistory;
        suppliersLayout = binding.ivSuppliers;
        stockLayout = binding.ivStock;

        suppliersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), SuppliersActivity.class);
                startActivity(intent);
            }
        });

        stockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), StockActivity.class);
                startActivity(intent);
            }
        });

        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        btnAddSaleHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalesPopUp categoryPopUp = new SalesPopUp(db, requireActivity());
                categoryPopUp.showPopupWindow(view);
            }
        });


//
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//        historyList.add(new History(4, "You have made a new sale"));
//
//        historyAdapter = new HistoryAdapter(requireActivity(), historyList);
//
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        dialog = ProgressDialog.show(requireActivity(), "",
                "Fetching history. Please wait...", true);
        dialog.show();

        sales.clear();
        fetchSales();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchSales() {
        DateTimeToday dateTimeToday = new DateTimeToday();
        String strDate = dateTimeToday.getDateTimeToday();

        db = FirebaseFirestore.getInstance();
        Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(salesCollection)));
        db.collection(salesCollection)
                .whereEqualTo("time", strDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("FETCH_isSuccessful", "TRUE");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
                                sales.add(document.getData());
                            }
                            Log.d("SALES_LIST", String.valueOf(sales));

                            salesAdapter = new SalesAdapter(requireActivity(), sales);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);

                            historyRV.setLayoutManager(layoutManager);
                            historyRV.setAdapter(salesAdapter);
                            dialog.dismiss();
                        }else {

                            Log.w("FETCH_SUPPLIERS_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }
}