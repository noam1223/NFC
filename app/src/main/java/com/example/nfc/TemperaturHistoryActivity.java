package com.example.nfc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.nfc.Classes.TempClass;
import com.example.nfc.Helpers.DatabaseHelperSQLite;
import com.example.nfc.Helpers.TempAdapter;

import java.util.ArrayList;

public class TemperaturHistoryActivity extends AppCompatActivity {


    private RecyclerView tempRecyclerView;
    private RecyclerView.Adapter tempAdapter;
    private RecyclerView.LayoutManager tempLayoutManager;

    private ArrayList<TempClass> tempClassArrayList;

    DatabaseHelperSQLite databaseHelperSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatur_history);

        databaseHelperSQLite = new DatabaseHelperSQLite(this);
        initilaizeMessageRecyclerView();
        produceTempList();


    }

    private void produceTempList() {

        Cursor data = databaseHelperSQLite.getData();
        data.moveToFirst();
        while (!data.isAfterLast()){

            tempClassArrayList.add(new TempClass(data.getString(data.getColumnIndex(DatabaseHelperSQLite.getCol2())),
                    data.getString(data.getColumnIndex(DatabaseHelperSQLite.getCol3()))));
            data.moveToNext();

        }

        tempAdapter.notifyDataSetChanged();
    }


    private void initilaizeMessageRecyclerView() {

        tempClassArrayList = new ArrayList<>();
        tempRecyclerView = findViewById(R.id.temp_recycler_view);
        tempRecyclerView.setNestedScrollingEnabled(false);
        tempRecyclerView.setHasFixedSize(false);
        tempLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        tempRecyclerView.setLayoutManager(tempLayoutManager);
        tempAdapter = new TempAdapter(this ,tempClassArrayList);
        tempRecyclerView.setAdapter(tempAdapter);
        tempRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

    }
}
