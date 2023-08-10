package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个Intent来跳转到MainActivity
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 获取history按钮并设置点击事件
        Button btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个Intent来跳转到HistoryActivity
                Intent intent = new Intent(Home.this, history.class);
                startActivity(intent);
            }
        });

        Button btnFriend = findViewById(R.id.btnFriend);
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个Intent来跳转到HistoryActivity
                Intent intent = new Intent(Home.this, Friend.class);
                startActivity(intent);
            }
        });
    }
}




//i)	Equal break-down scenario: Total bill RM67.40, number of people 5, each person to pay RM13.48
//Total bill RM70, number of people 3, each person to pay by percentage (25%, 35%, 40%) the amount RM17.50, RM24.50, and RM28.00
//otal bill RM72.60, number of people 4, person 1 – 4 to pay by individual amount RM15, RM16.90, RM18.90 and RM19.20.