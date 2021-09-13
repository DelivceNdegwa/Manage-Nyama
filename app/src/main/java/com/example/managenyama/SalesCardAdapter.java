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

import java.util.ArrayList;

public class SalesCardAdapter extends ArrayAdapter<SalesCardModel> {
    public SalesCardAdapter(@NonNull Context context, ArrayList<SalesCardModel> salesCardModelArrayList){
        super(context, 0, salesCardModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.sales_item, parent, false);
        }
        SalesCardModel salesCardModel = getItem(position);
        TextView salesName = listitemView.findViewById(R.id.sale_name);
        ImageView salesImage = listitemView.findViewById(R.id.sales_image);
        TextView salesNumber = listitemView.findViewById(R.id.sales_number);
        salesName.setText(salesCardModel.getSalesName());
        salesImage.setImageResource(salesCardModel.getSalesImage());
        salesNumber.setText(salesCardModel.getSaleNumber());
        return listitemView;
    }
}
