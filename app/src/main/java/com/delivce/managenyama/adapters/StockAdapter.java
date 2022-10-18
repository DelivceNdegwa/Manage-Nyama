package com.delivce.managenyama.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.R;
import com.delivce.managenyama.ui.categories.CategoryDetailsActivity;

import java.util.List;
import java.util.Map;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    Context context;
    List<Map<String, Object>> stocks;

    public StockAdapter(Context context, List<Map<String, Object>> stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ViewHolder holder, int position) {
        Map<String, Object> stock = stocks.get(position);

        holder.tvStockQuantity.setText(String.valueOf(stock.get("quantity"))+" kg");
        holder.tvStockName.setText(String.valueOf(stock.get("category")));
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStockName, tvStockQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStockName = itemView.findViewById(R.id.tv_item_stock_name);
            tvStockQuantity = itemView.findViewById(R.id.tv_stock_quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CategoryDetailsActivity.class);
                    intent.putExtra("CATEGORY_NAME", tvStockName.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }
}
