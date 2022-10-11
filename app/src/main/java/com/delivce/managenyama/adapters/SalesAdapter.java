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

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    Context context;
    List<Map<String, Object>> sales;

    public SalesAdapter(Context context, List<Map<String, Object>> sales) {
        this.context = context;
        this.sales = sales;
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
        holder.saleQuantity.setText(String.valueOf(sale.get("quantity")));
        holder.saleTime.setText(String.valueOf(sale.get("time")));
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView saleCategory, saleQuantity, saleTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            saleCategory = itemView.findViewById(R.id.tv_category_sale);
            saleQuantity = itemView.findViewById(R.id.tv_quantity_sale);
            saleTime = itemView.findViewById(R.id.tv_sale_time);
        }
    }
}
