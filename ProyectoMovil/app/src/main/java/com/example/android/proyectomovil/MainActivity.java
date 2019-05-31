package com.example.android.proyectomovil;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;


    private static final String TEXT_STATE = "Texto actual";
    private WordViewModel mWordViewModel;

    //AQUI VAMOS A MOSTRAR EL RESULTADO
    private TextView mTextView;

    //VARIABLES DEL ADAPTER PARA LISTA TEMPORAL DE RECADOS
    private ListView maestroList;
    private EditText mItemEdit;
    private Button mAddButton;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Conectando UI CON LOS DATOS
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {

            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });

        //IMPLEMENTANDO SETTINGS

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);

            }
        });
        android.support.v7.preference.PreferenceManager
                .setDefaultValues(this,
                        R.xml.preferences, false);

        // LEER SETTINGS DE SHARED PREFERENCES
        // SettingsActivity, DISPLAY MENSAJE (FALSE)
        SharedPreferences sharedPref =
                android.support.v7.preference.PreferenceManager
                        .getDefaultSharedPreferences(this);


        Boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);

        Toast.makeText(this, switchPref.toString(),
                Toast.LENGTH_SHORT).show();


        mTextView = findViewById(R.id.VerInfo);

        //IMPLEMENTAR ADAPTER

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

        // RESTAURAR TEXTVIEW SI HAY INSTANCIA GUARDADA
        if (savedInstanceState != null) {
            mTextView.setText(savedInstanceState.getString(TEXT_STATE));
        }

    }

    public void IniciaTarea(View view) {
        //PONER MENSAJE EN TEXT VIEW
        mTextView.setText(R.string.Descansando);

        //ARRANCAR ASYNCTASK
        new SimpleAsyncTask(mTextView).execute();
    }

    //GUARDAR TEXTVIEW PARA RESTAURAR SI HAY CAMBIOS DE CONFIG
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // GRABAR INSTANCIA TEXTVIEW
        outState.putString(TEXT_STATE, mTextView.getText().toString());
    }

//IMPLEMENTAR SETTINGS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        // if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //ON OPTIONS ITEM SELECTED
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // SETTINGS PARTE
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //PARTE DE IMPLANTACION PERSISTENT DB
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}

