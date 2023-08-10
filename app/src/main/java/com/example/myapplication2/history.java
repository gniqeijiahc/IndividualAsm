package com.example.myapplication2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class history extends AppCompatActivity {
    private  ListView listView;
    private ArrayList<MoneyList> dataList = new ArrayList<>();
    private ArrayList<MoneyList> oriList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        FileInputStream fin = null;
        BufferedReader br = null;
        ArrayList<String> name = new ArrayList<>();
        ArrayList<Double> money = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        try{
            fin = openFileInput("data.txt");
            br = new BufferedReader(new InputStreamReader(fin));
            String line = "";
            while((line = br.readLine()) != null){
                sb.append(line);
                String[] part = line.split("RM");
                String nameStr = part[0];
                Double amount = Double.parseDouble(part[part.length - 1]);
                name.add(nameStr);
                money.add(amount);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int i=0; i<name.size(); i++){
            dataList.add(new MoneyList(name.get(i), money.get(i)));
        }
        oriList = new ArrayList<>(dataList);

        Collections.sort(dataList, new NameComparator2());
        listView = findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter(this, dataList);
        listView.setAdapter(customAdapter);
    }

    //menuSave button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单资源文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理菜单项点击事件
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (needToSave()) {
            // Show an AlertDialog to prompt the user to save changes
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Unsaved change will discard");
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Save the data here
                    saveData();
                    finish(); // Finish the activity after saving
                }
            });
            builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); // Finish the activity without saving
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User canceled the prompt, do nothing
                }
            });
            builder.show();
        } else {
            // No changes need to be saved, just finish the activity
            super.onBackPressed();
        }
    }


    public void saveData(){
        CustomAdapter customAdapter = (CustomAdapter) listView.getAdapter();
        ArrayList<MoneyList> dataList = customAdapter.getDataList();

        StringBuilder newDataTemp = new StringBuilder();
        for(MoneyList ml : dataList){
            newDataTemp.append(ml.getString() + "\n");
        }
        String newData = new String(newDataTemp.toString());
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(newData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        oriList = null;
        oriList = new ArrayList<>(customAdapter.getDataList());
    }

    public boolean needToSave(){
        CustomAdapter customAdapter = (CustomAdapter) listView.getAdapter();
        ArrayList<MoneyList> dataList2 = new ArrayList<>(customAdapter.getDataList());
        return !oriList.equals(dataList2);
    }


    //moneyList comparator
    private static class NameComparator2 implements Comparator<MoneyList> {
        @Override
        public int compare(MoneyList ml1, MoneyList ml2) {
            // 根据money属性进行比较
            return ml1.getName().compareTo(ml2.getName());
        }
    }
}
