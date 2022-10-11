package com.delivce.managenyama.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.delivce.managenyama.R;
import com.delivce.managenyama.models.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<History> historyList;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cvHistory;
        TextView tvHistoryCategory, tvHistoryItemTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvHistory = itemView.findViewById(R.id.cv_history);
            tvHistoryCategory = itemView.findViewById(R.id.tv_history_category);
            tvHistoryItemTime = itemView.findViewById(R.id.tv_history_item_time);
        }
    }
}
