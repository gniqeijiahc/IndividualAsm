package com.example.myapplication2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Friend extends AppCompatActivity {
    private ListView listView;
    private ArrayList<MoneyList> dataList = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Button btnCreateName = findViewById(R.id.buttonAdd);
        getFriend();
        listView = findViewById(R.id.list_view_friend);
        FriendAdapter customAdapter = new FriendAdapter(this, dataList);
        listView.setAdapter(customAdapter);

        btnCreateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
    }

    public void saveFriend(String data){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("friends.txt", Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data + "\n");
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
    }

    private void showInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.name_input, null);

        builder.setTitle("Please Enter Friend Name");
        builder.setView(dialogView);
        builder.setCancelable(false);
        EditText ed = dialogView.findViewById(R.id.friend_name_dialog);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = ed.getText().toString().trim();
                if(!input.isEmpty()){
                    saveFriend(input);
                    dataList.add(new MoneyList(input, 0.0));
                    FriendAdapter customAdapter = new FriendAdapter(Friend.this, dataList);
                    listView.setAdapter(customAdapter);
                }
                else{
                    saveFriend("Unknown");
                }
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }
    protected void onDestroy() {
        FriendAdapter customAdapter = (FriendAdapter) listView.getAdapter();
        ArrayList<MoneyList> dataList = customAdapter.getDataList();

        StringBuilder newDataTemp = new StringBuilder();
        for(MoneyList ml : dataList){
            newDataTemp.append(ml.getName() + "\n");
        }
        String newData = new String(newDataTemp.toString());
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("friends.txt", Context.MODE_PRIVATE);
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

        super.onDestroy();

    }

    public void getFriend(){
        FileInputStream fin = null;
        BufferedReader br = null;

        try{
            fin = openFileInput("friends.txt");
            br = new BufferedReader(new InputStreamReader(fin));
            String line = "";
            while((line = br.readLine()) != null){
                name.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<name.size(); i++){
            dataList.add(new MoneyList(name.get(i), 0.0));
        }
    }
}