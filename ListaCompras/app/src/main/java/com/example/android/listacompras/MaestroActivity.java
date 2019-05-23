package com.example.android.listacompras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MaestroActivity extends AppCompatActivity {

    private ListView maestroList;
    private EditText mItemEdit;
    private Button mAddButton;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maestroList = (ListView) findViewById(R.id.shopping_listView);
        mItemEdit = (EditText) findViewById(R.id.item_editText);
        mAddButton = (Button) findViewById(R.id.add_button);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        maestroList.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                mAdapter.add(item);
                mAdapter.notifyDataSetChanged();
                mItemEdit.setText("");
            }
        });
    }
}