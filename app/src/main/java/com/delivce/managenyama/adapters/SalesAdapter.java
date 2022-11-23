package com.delivce.managenyama.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    Context context;
    List<Map<String, Object>> sales;
    FirebaseFirestore db;

    double defaultPrice;

    public SalesAdapter(Context context, List<Map<String, Object>> sales) {
        this.context = context;
        this.sales = sales;
    }

    public SalesAdapter(Context context, FirebaseFirestore db, List<Map<String, Object>> sales) {
        this.context = context;
        this.sales = sales;
        this.db = db;
    }


    @NonNull
    @Override
    public SalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales, parent, false);
        return new SalesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.ViewHolder holder, int position) {
        Map<String, Object> sale = sales.get(position);

        holder.saleCategory.setText(String.valueOf(sale.get("category")));
        holder.saleQuantity.setText(String.valueOf(sale.get("quantity"))+ "kg");
        holder.saleTime.setText(String.valueOf(sale.get("time")));

        getDefaultPrice("default_price");

        Log.d("SALE_AD1", String.valueOf(sale.get("quantity")));
        Log.d("SALE_AD2", String.valueOf(defaultPrice));

        double totalCash = defaultPrice * (double) sale.get("quantity");

        holder.tvCashMade.setText(String.valueOf(totalCash));
    }

    private void getDefaultPrice(String default_price) {
        db.collection("meat_categories")
                .whereEqualTo("name", default_price)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot: task.getResult()){
                            Log.d("SALE_AD3", String.valueOf(documentSnapshot.getDouble("default_price")));
                            defaultPrice = documentSnapshot.getDouble("default_price");
                            break;
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView saleCategory, saleQuantity, saleTime, tvCashMade;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            saleCategory = itemView.findViewById(R.id.tv_category_sale);
            saleQuantity = itemView.findViewById(R.id.tv_quantity_sale);
            saleTime = itemView.findViewById(R.id.tv_sale_time);
            tvCashMade = itemView.findViewById(R.id.tv_cash_made);
        }
    }
}
