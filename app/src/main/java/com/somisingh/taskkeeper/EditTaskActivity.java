package com.somisingh.taskkeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.somisingh.taskkeeper.MainActivity.ITEM_POSITION;
import static com.somisingh.taskkeeper.MainActivity.ITEM_TEXT;

public class EditTaskActivity extends AppCompatActivity {

    EditText editText;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editText = findViewById(R.id.editTaskText);
        editText.setText(getIntent().getStringExtra(ITEM_TEXT));
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        getSupportActionBar().setTitle("Edit Task");
    }

    public void onSaveItem(View v) {
        Intent intent = new Intent();
        intent.putExtra(ITEM_TEXT, editText.getText().toString());
        intent.putExtra(ITEM_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
