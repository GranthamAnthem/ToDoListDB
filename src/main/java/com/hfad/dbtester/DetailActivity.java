package com.hfad.dbtester;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button addButton;
    Button orderButton;
    Button normalButton;
    Button searchButton;
    EditText addText;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ArrayAdapter adapterOrder;
    ListView listView;
    public static final String EXTRA_TODOID = "todoID";
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
        int todoId = (Integer) getIntent().getExtras().get(EXTRA_TODOID);
        final String nameId = Integer.toString(todoId);

        viewData(nameId);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = listView.getItemAtPosition(position).toString();
                if (db.deleteData(text)) {
                    Toast.makeText(DetailActivity.this, "" + text + " Deleted", Toast.LENGTH_SHORT).show();
                    addText.setText("");
                    listItem.clear();
                    viewData(nameId);
                } else {
                    Toast.makeText(DetailActivity.this, "" + text + " Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = addText.getText().toString();
                if (!name.equals("") && db.insertDataItems(nameId,name)) {
                    Toast.makeText(DetailActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                    addText.setText("");
                    listItem.clear();
                    viewData(nameId);
                } else {
                    Toast.makeText(DetailActivity.this, "Data Not Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.clear();
                String name = addText.getText().toString();
                cursor = db.searchData(name);
                if (cursor.getCount() == 0) {
                    Toast.makeText(DetailActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()) {
                        listItem.add(cursor.getString(1));
                    }
                    adapter = new ArrayAdapter(DetailActivity.this, android.R.layout.simple_list_item_1, listItem);
                    listView.setAdapter(adapter);
                }
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.clear();
                sortedData(nameId);
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItem.clear();
                normalSort(nameId);
            }
        });
    }

    private void viewData(String nameId) {
        cursor = db.viewData(nameId);
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

    private void sortedData(String nameId) {
        cursor = db.sortData(nameId);
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


    private void normalSort(String nameId) {
        cursor = db.viewData(nameId);
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
        db.close();
    }

}
