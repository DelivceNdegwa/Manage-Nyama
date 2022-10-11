package com.delivce.managenyama.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.R;

import java.util.List;
import java.util.Map;

public class SuppliersAdapter extends RecyclerView.Adapter<SuppliersAdapter.ViewHolder> {
    Context context;
    List<Map<String, Object>> suppliers;

    public SuppliersAdapter(Context context, List<Map<String, Object>> suppliers) {
        this.context = context;
        this.suppliers = suppliers;
    }

    @NonNull
    @Override
    public SuppliersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier, parent, false);
        return new SuppliersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuppliersAdapter.ViewHolder holder, int position) {
        Map<String, Object> supplier = suppliers.get(position);

        holder.tvSupplierName.setText(String.valueOf(supplier.get("name")));
        holder.tvPhone.setText(String.valueOf(supplier.get("phone_number")));
        holder.tvLocation.setText(String.valueOf(supplier.get("location")));
    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSupplierName, tvPhone, tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSupplierName = itemView.findViewById(R.id.tv_supplier_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvLocation = itemView.findViewById(R.id.tv_location);
        }
    }
}
