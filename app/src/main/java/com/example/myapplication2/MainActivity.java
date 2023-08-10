package com.example.myapplication2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<EditText> nameList = new ArrayList<>();
    private ArrayList<TextView> amountList = new ArrayList<>();
    private ArrayList<EditText> percentageList = new ArrayList<>();
    private Double totalAmount = 0.0;
    private int numberOfInputs = 0;
    int buttonSelection = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = findViewById(R.id.inputsContainer);

        Button aa = (Button) findViewById(R.id.buttonAA);
        Button percentage = (Button) findViewById(R.id.buttonPercentage);
        Button amount = (Button) findViewById(R.id.buttonAmount);
        aa.setOnClickListener((View.OnClickListener) this);
        percentage.setOnClickListener((View.OnClickListener) this);
        amount.setOnClickListener((View.OnClickListener) this);

        EditText editTextTotalAmount = findViewById(R.id.editTextNumberSigned1);
        editTextTotalAmount.addTextChangedListener(new MainActivity.CurrencyTextWatcher());
        editTextTotalAmount.setRawInputType(InputType.TYPE_CLASS_NUMBER);


        Button btSave = (Button) findViewById(R.id.buttonSave);
        Button btCalculate = (Button) findViewById(R.id.buttonCalculate);

        showInputDialog();


        btCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonSelection == 2){
                    ArrayList<Double> percentFunction2 = new ArrayList<Double>();
                    Double total = 0.0;
                    for(EditText ed : percentageList){
                        Double percent = Double.parseDouble(ed.getText().toString());
                        total += percent;
                        percentFunction2.add(percent);
                    }
                    if(Double.compare(total, 100) != 0){
                        // Show a Toast message to inform the user of the error
                        Toast.makeText(MainActivity.this, "The total percentage must be 100%", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int index = 0;
                        for (TextView tv : amountList) {
                            // Get the corresponding percentage from percentFunction2
                            Double percentage = percentFunction2.get(index);
                            // Calculate the amount for this TextView
                            Double amount = (totalAmount * percentage) / 100.0;

                            // Format the amount to two decimal places
                            String formattedAmount = String.format("RM %.2f", amount);

                            // Set the formatted amount as the text of the TextView
                            tv.setText(formattedAmount);
                            tv.setVisibility(View.VISIBLE);

                            // Move to the next index in percentFunction2
                            index++;
                        }
                    }

                }
                else if(buttonSelection == 3){
                    ArrayList<Double> percentFunction2 = new ArrayList<Double>();
                    Double total = 0.0;
                    for(EditText ed : percentageList){
                        Double percent = Double.parseDouble(ed.getText().toString());
                        total += percent;
                        percentFunction2.add(percent);
                    }
                    if(Double.compare(total, totalAmount) != 0){
                        // Show a Toast message to inform the user of the error
                        double diff = totalAmount - total;
                        if(diff < 0){
                            Toast.makeText(MainActivity.this, String.format("Total Amount didn't match, exceed RM %.2f",  diff), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, String.format("Total Amount didn't match, still need RM %.2f", diff), Toast.LENGTH_LONG).show();
                        }

                    }
                    else{
                        int index = 0;
                        for (TextView tv : amountList) {
                            // Get the corresponding percentage from percentFunction2
                            Double percentage = percentFunction2.get(index);
                            // Calculate the amount for this TextView
                            Double amount = percentage;

                            // Format the amount to two decimal places
                            String formattedAmount = String.format("RM %.2f", amount);

                            // Set the formatted amount as the text of the TextView
                            tv.setText(formattedAmount);

                            // Move to the next index in percentFunction2
                            index++;
                        }
                        for(EditText ed : percentageList){
                            ed.setVisibility(View.GONE);
                            ed.setHint("Enter Amount");
                        }
                        for(TextView tv : amountList){
                            tv.setVisibility(View.VISIBLE);
                        }
                    }


                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < nameList.size(); i++) {
                    EditText nameEditText = nameList.get(i);
                    TextView amountTextView = amountList.get(i);

                    String name = nameEditText.getText().toString().trim();
                    String amount = amountTextView.getText().toString().trim();

                    if (!name.isEmpty() && !amount.isEmpty()) {
                        stringBuilder.append(name).append(" ").append(amount).append("\n");
                    }
                }
                String data = new String(stringBuilder.toString());


                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, data);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

    }


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
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < nameList.size(); i++) {
                EditText nameEditText = nameList.get(i);
                TextView amountTextView = amountList.get(i);

                String name = nameEditText.getText().toString().trim();
                String amount = amountTextView.getText().toString().trim();

                if (!name.isEmpty() && !amount.isEmpty()) {
                    stringBuilder.append(name).append(" ").append(amount).append("\n");
                }
            }
            String data = new String(stringBuilder.toString());
            FileOutputStream out = null;
            BufferedWriter writer = null;
            try {
                out = openFileOutput("data.txt", Context.MODE_APPEND);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(data);
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
            Toast.makeText(this, "Save button clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, history.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_refresh){
            EditText editTextNumberOfInputs = findViewById(R.id.numberOfPeople1);
            EditText editTextTotalAmount = findViewById(R.id.editTextNumberSigned1);
            totalAmount = Double.parseDouble(editTextTotalAmount.getText().toString());
            numberOfInputs = Integer.parseInt(editTextNumberOfInputs.getText().toString());

            generateInput();
            Toast.makeText(this, "Refresh button clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onClick(View v) {
        Button btCalculate = (Button) findViewById(R.id.buttonCalculate);
        Button btSave = (Button) findViewById(R.id.buttonSave);
        Button btAA = (Button) findViewById(R.id.buttonAA);
        Button btPercentage = (Button) findViewById(R.id.buttonPercentage);
        Button btAmount = (Button) findViewById(R.id.buttonAmount);
        TextView edHint = findViewById(R.id.textHintt);

        switch (v.getId()) {
            case R.id.buttonAA:
                buttonSelection = 1;
                btCalculate.setVisibility(View.GONE);
                Double average = totalAmount / numberOfInputs;
//                generateInput();
                for(EditText ed: percentageList){
                    ed.setVisibility(View.GONE);
                }
                for(TextView tv: amountList){
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("RM " + average);
                }
                edHint.setText("Equally");
                break;
            case R.id.buttonPercentage:
                buttonSelection = 2;
                btCalculate.setVisibility(View.VISIBLE);
                btCalculate.setText("Calculate");
                for(EditText ed : percentageList){
                    ed.setVisibility(View.VISIBLE);
                    ed.setHint("Percent");
                }
                for(TextView tv: amountList){
                    tv.setText("RM 0.00");
                }
                edHint.setText("Percentage");
                break;
            case R.id.buttonAmount:
                buttonSelection = 3;
                btCalculate.setVisibility(View.VISIBLE);
                btCalculate.setText("Check");
                for(EditText ed : percentageList){
                    ed.setVisibility(View.VISIBLE);
                    ed.setHint("Amount");
                }
                for(TextView tv : amountList){
                    tv.setText("RM 0.00");
                }
                edHint.setText("Amount");
                break;
            default:
                break;
        }
    }

    private void showInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input, null);


        final EditText totalAmountEditText = dialogView.findViewById(R.id.editTextTotalAmount);
        final EditText totalPeopleEditText = dialogView.findViewById(R.id.editTextTotalPeople);
        totalAmountEditText.addTextChangedListener(new CurrencyTextWatcher());
        totalAmountEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setTitle("Please Enter Total amount and Total people");
        builder.setView(dialogView);
        builder.setCancelable(false);

        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String totalAmountStr = totalAmountEditText.getText().toString();
                String totalPeopleStr = totalPeopleEditText.getText().toString();

                totalAmount = Double.parseDouble(totalAmountStr);
                numberOfInputs = Integer.parseInt(totalPeopleStr);
                EditText editTextNumberOfInputs = findViewById(R.id.numberOfPeople1);
                EditText editTextTotalAmount = findViewById(R.id.editTextNumberSigned1);
                editTextNumberOfInputs.setText(Integer.toString(numberOfInputs));
                editTextTotalAmount.setText(String.format( "%.2f", totalAmount));

                generateInput();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numberOfInputs = 2;
                totalAmount = 0.0;
                EditText editTextNumberOfInputs = findViewById(R.id.numberOfPeople1);
                EditText editTextTotalAmount = findViewById(R.id.editTextNumberSigned1);
                editTextNumberOfInputs.setText(Integer.toString(numberOfInputs));
                editTextTotalAmount.setText(Double.toString(totalAmount));
                generateInput();
            }
        });

        builder.create().show();
    }

    public void generateInput(){
        LinearLayout container = findViewById(R.id.inputsContainer);
        container.removeAllViews();
        nameList.clear();
        amountList.clear();
        percentageList.clear();
//        EditText editTextNumberOfInputs = findViewById(R.id.numberOfPeople1);
//        EditText editTextTotalAmount = findViewById(R.id.editTextNumberSigned1);
//        numberOfInputs = Integer.parseInt(editTextNumberOfInputs.getText().toString());
//        totalAmount = Double.parseDouble(editTextTotalAmount.getText().toString());
        ArrayList<String> nameHint = new ArrayList<>();
        FileInputStream fin = null;
        BufferedReader br = null;
        try{
            fin = openFileInput("friends.txt");
            br = new BufferedReader(new InputStreamReader(fin));
            String line = "";
            while((line = br.readLine()) != null){
                nameHint.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < numberOfInputs; i++) {
            // Create a new LinearLayout
            LinearLayout linearLayout = new LinearLayout(MainActivity.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Create name text;
            ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nameHint);

            AutoCompleteTextView editText = new AutoCompleteTextView(this);
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 2));
            editText.setHint("Enter Name ");
            nameList.add(editText);
            editText.setAdapter(arrayAdapter);
            editText.setThreshold(1);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d("beforeTextChanged", String.valueOf(s));
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("onTextChanged", String.valueOf(s));
                }
                @Override
                public void afterTextChanged(Editable s) {
                    Log.d("afterTextChanged", String.valueOf(s));
                }
            });

            // You can customize the EditText further as needed

            EditText editText2 = new EditText(MainActivity.this);
            editText2.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            editText2.setVisibility(View.GONE);
            editText2.setRawInputType(InputType.TYPE_CLASS_NUMBER);
//            editText2.removeTextChangedListener();
//            editText2.addTextChangedListener(new CurrencyTextWatcher());

            // Create TextView
            TextView textView = new TextView(MainActivity.this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,LinearLayout.LayoutParams.MATCH_PARENT, 1));
            textView.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            // Calculate the amount per person
            double amountPerPerson = totalAmount / numberOfInputs;

            // Format the amount to two decimal places
            String formattedAmount = String.format("RM %.2f", amountPerPerson);
            // Set the result in the TextView
            textView.setText(formattedAmount);
            textView.setVisibility(View.VISIBLE);

            amountList.add(textView);
            percentageList.add(editText2);
            // You can customize the TextView further as needed

            // Add EditText and TextView to the LinearLayout
            linearLayout.addView(editText);
            linearLayout.addView(editText2);
            linearLayout.addView(textView);

            // Add the LinearLayout to the existing inputsContainer
            container.addView(linearLayout);
        }

    }

    class CurrencyTextWatcher implements TextWatcher {

        boolean mEditing;

        public CurrencyTextWatcher() {
            mEditing = false;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public synchronized void afterTextChanged(Editable s) {
            if (!mEditing) {
                mEditing = true;

                String digits = s.toString().replaceAll("\\D", "");
                try {
                    String formatted = String.format( "%.2f", (Double.parseDouble(digits) / 100));
                    s.replace(0, s.length(), formatted);
                } catch (NumberFormatException nfe) {
                    s.clear();
                }

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}

