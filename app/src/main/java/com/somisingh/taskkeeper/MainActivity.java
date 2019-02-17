package com.somisingh.taskkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int EDIT_REQUEST_CODE = 20;
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

    private ArrayList<String> items;
    private ArrayAdapter<String> mAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems = findViewById(R.id.lvItems);
        lvItems.setAdapter(mAdapter);

        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText newItem = findViewById(R.id.etNewItem);
        String itemText = newItem.getText().toString();
        mAdapter.add(itemText);
        newItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Task added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        Log.i(TAG, "Setting up Listener on list view)");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "Item removed from list");
                items.remove(i);
                mAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Item edit called");
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra(ITEM_TEXT, items.get(position));
                intent.putExtra(ITEM_POSITION, position);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            String updatedTask = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION);
            items.set(position, updatedTask);
            mAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(getApplicationContext(), "Task updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "tasks.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e(TAG, "Error reading tasks.txt", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e)  {
            Log.e(TAG, "Error writing to file", e);
        }
    }
}
