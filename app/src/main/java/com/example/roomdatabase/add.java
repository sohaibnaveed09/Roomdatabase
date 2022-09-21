package com.example.roomdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class add extends AppCompatActivity {

    Button btn_add;
    RecyclerView recyclerView;
    RoomDB database;
    MainAdapter adapter;
    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        recyclerView=findViewById(R.id.recycler_vew);
        btn_add=findViewById(R.id.add);

        database=RoomDB.getInstance(this);
        dataList=database.mainDao().getAll();

        adapter=new MainAdapter(add.this,dataList);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(add.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}