package com.example.myapplication2;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<MoneyList> dataList;
    private Context context;

    public CustomAdapter(Context context, ArrayList<MoneyList> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.money_list, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewMoney = convertView.findViewById(R.id.textViewMoney);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        MoneyList itemModel = dataList.get(position);
        textViewName.setText(itemModel.getName());
        textViewMoney.setText("RM " + String.valueOf(itemModel.getMoney()));
        btnDelete.setOnClickListener(v -> {
            dataList.remove(position); // Remove the item from the data list
            notifyDataSetChanged(); // Update the ListView
        });


        return convertView;
    }

    public ArrayList<MoneyList> getDataList() {
        return dataList;
    }

}
