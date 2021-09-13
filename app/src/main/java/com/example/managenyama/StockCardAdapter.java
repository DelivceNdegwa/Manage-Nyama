package com.example.managenyama;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StockCardAdapter extends ArrayAdapter<StockCardModel> {
    public StockCardAdapter(@NonNull Context context, ArrayList<StockCardModel> stockCardModelArrayList){
        super(context, 0, stockCardModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.stock_item, parent, false);
        }
        StockCardModel stockCardModel = getItem(position);
        TextView stockName = listitemView.findViewById(R.id.stock_name);
        ImageView stockImage = listitemView.findViewById(R.id.stock_image);
        TextView stockAmount = listitemView.findViewById(R.id.stock_amount);
        stockName.setText(stockCardModel.getStockName());
        stockImage.setImageResource(stockCardModel.getStockImage());
        stockAmount.setText(stockCardModel.getStockQuantity());
        return listitemView;
    }
}
