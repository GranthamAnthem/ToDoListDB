package com.hfad.dbtester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button addButton;
    Button orderButton;
    Button normalButton;
    Button searchButton;
    EditText addText;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView listView;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        addButton = findViewById(R.id.addButton);
        orderButton = findViewById(R.id.orderButton);
        normalButton = findViewById(R.id.normalButton);
        searchButton = findViewById(R.id.searchButton);
        addText = findViewById(R.id.addText);
        listView = findViewById(R.id.listView);
        listItem = new ArrayList<>();


        viewData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                intent.putExtra(DetailActivity.EXTRA_TODOID, (int) id);
                startActivity(intent);

                }
        });

/*        // delete data
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = listView.getItemAtPosition(position).toString();
                if (db.deleteDataList(text)) {
                    Toast.makeText(MainActivity.this, "" + text + " Deleted", Toast.LENGTH_SHORT).show();
                    addText.setText("");
                    listItem.clear();
                    viewData();
                } else {
                    Toast.makeText(MainActivity.this, "" + text + " Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addText.getText().toString();
                if (!name.equals("") && db.insertDataList(name)) {
                    Toast.makeText(MainActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                    addText.setText("");
                    listItem.clear();
                    viewData();
                } else {
                    Toast.makeText(MainActivity.this, "Data Not Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.clear();
                String name = addText.getText().toString();
                cursor = db.searchDataList(name);
                if (cursor.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()) {
                        listItem.add(cursor.getString(1));
                    }
                    adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, listItem);
                    listView.setAdapter(adapter);
                }
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.clear();
                sortedData();
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.clear();
                normalSort();
            }
        });
    }

    private void viewData() {
        cursor = db.viewDataList();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1));
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            listView.setAdapter(adapter);
        }
    }

    private void sortedData() {
        cursor = db.sortDataList();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1));
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            listView.setAdapter(adapter);
        }
    }


    private void normalSort() {
        cursor = db.viewDataList();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1));
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            listView.setAdapter(adapter);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

}