package com.delivce.managenyama.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.R;
import com.delivce.managenyama.ui.categories.CategoryDetailsActivity;

import java.util.List;
import java.util.Map;

public class MeatCategoriesAdapter extends RecyclerView.Adapter<MeatCategoriesAdapter.ViewHolder> {
        Context context;
        List<Map<String, Object>> categories;

    public MeatCategoriesAdapter(Context context, List<Map<String, Object>> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MeatCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new MeatCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeatCategoriesAdapter.ViewHolder holder, int position) {
        Map<String, Object> categoryItem = categories.get(position);
        holder.categoryName = String.valueOf(categoryItem.get("name"));
        Log.d("MAP_CATEGORY_NAME", holder.categoryName);
        holder.categoryPrice = String.valueOf(categoryItem.get("default_price"));
        holder.tvCategoryName.setText(holder.categoryName);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        String categoryPrice, categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.tv_item_category_name);

        }
    }
}
