package com.example.roomdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextInputLayout name,email,phoneNo,password;
    Button add;
    RadioButton male,female;
    String gender="Male";

    List<MainData> dataList = new ArrayList<>();

    RoomDB database;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phoneNo=findViewById(R.id.phoneNo);
        password=findViewById(R.id.password);

        male=findViewById(R.id.radio_male);
        female=findViewById(R.id.radio_female);

        add=findViewById(R.id.btn_ADD);


        database=RoomDB.getInstance(this);
        dataList=database.mainDao().getAll();



        adapter=new MainAdapter(MainActivity.this,dataList);
        

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName=name.getEditText().getText().toString().trim();
                String sEmail=email.getEditText().getText().toString().trim();
                String sPhone=phoneNo.getEditText().getText().toString().trim();
                String sPass=password.getEditText().getText().toString().trim();
                String sGender=gender;

                if(!validatUserName()|!validatEmail()|!validatPhoneNo()|!validatPassword()){
                    return;
                }

                   MainData data=new MainData();
                    data.setName(sName);
                    data.setEmail(sEmail);
                    data.setPhone(sPhone);
                    data.setPassword(sPass);
                    data.setGender(sGender);

                    database.mainDao().insert(data);

                    name.getEditText().setText("");
                    email.getEditText().setText("");
                    phoneNo.getEditText().setText("");
                    password.getEditText().setText("");

                    male.setChecked(true);
                    female.setChecked(false);

                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Data Inserted Successfully, Displaying Data Now", Toast.LENGTH_LONG).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent=new Intent(MainActivity.this,add.class);
                            startActivity(intent);
                        }
                    }, 3000);

            }
        });

    }

    private boolean validatUserName(){
        String val= name.getEditText().getText().toString();
        String noWhiteSpace="\\A\\w{4,20}\\z";
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(name.getEditText().getText().toString());
        boolean bs = ms.matches();

        if(val.isEmpty()){
            name.setError("Field Cannot be empty");
            return false;
        }
        else if (bs == false) {
           name.setError(("Username should not contain digits or special letters"));
           return false;
        }
        else if(val.length()>=15){
            name.setError("Username to long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)){
            name.setError("White Space are not allowed");
            return false;
        }
        else{
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatEmail(){
        String val= email.getEditText().getText().toString();
        if(val.isEmpty()){
            email.setError("Field Cannot be empty");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            email.setError("Invaild email address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    private boolean validatPhoneNo(){
        String val= phoneNo.getEditText().getText().toString();
        if(val.isEmpty()){
            phoneNo.setError("Field Cannot be empty");
            return false;
        }
        else if(!((val.length()) ==11)){
            phoneNo.setError("Phone Number should be of 11 Digits");
            return false;
        }
        else{
            phoneNo.setError(null);
            return true;
        }
    }

    private boolean validatPassword(){
        String val= password.getEditText().getText().toString();
        String passwordVal="^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        CharSequence input=val;
        Pattern pattern = Pattern.compile(passwordVal, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        if(val.isEmpty()){
            password.setError("Field Cannot be empty");
            return false;
        }
        else if(!matcher.matches()){
            password.setError("Password is too weak");
            return false;
        }
        else if(val.length()<8){
            password.setError("Password should be of 8 or more characters");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_male:
                if (checked){
                    gender="Male";
                }
                    break;
            case R.id.radio_female:
                if (checked){
                    gender="Female";
                }
                    break;
        }
    }

}