package com.delivce.managenyama.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.R;

import java.util.Map;

public class MeatCategoriesAdapter extends RecyclerView.Adapter<MeatCategoriesAdapter.ViewHolder> {
    Context context;
    Map<String, Object> categories;

    public MeatCategoriesAdapter(Context context, Map<String, Object> categories) {
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

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
