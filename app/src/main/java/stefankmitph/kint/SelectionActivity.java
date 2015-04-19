package stefankmitph.kint;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import stefankmitph.kint.R;
import stefankmitph.model.BookNavigator;
import stefankmitph.model.DatabaseManager;
import stefankmitph.model.Word;

public class SelectionActivity extends ActionBarActivity {

    private Spinner spinnerChapters;
    private Spinner spinnerBooks;
    private Spinner spinnerVerses;
    private SQLiteDatabase database;
    private BookNavigator navigator;
    private String book;
    private int chapter;
    private int verse;
    private Button buttonGo;

    private SQLiteDatabase initializeDatabase(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase database = null;

        /*try {
            //dataBaseHelper.createDataBase();
            //database = dataBaseHelper.getReadableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return database;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        buttonGo = (Button) findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("book", book);
                bundle.putInt("chapter", chapter);
                bundle.putInt("verse", verse);
                i.putExtras(bundle);

                startActivity(i);
            }
        });

        DatabaseManager.init(this);
        DatabaseManager instance = DatabaseManager.getInstance();

        this.database = initializeDatabase(this);
        navigator = new BookNavigator(database);

        spinnerBooks = (Spinner) findViewById(R.id.spinnerbooks);
        spinnerVerses = (Spinner) findViewById(R.id.spinnerverses);

        spinnerBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);

                book = (String)itemAtPosition;

                List<String> list = navigator.getChapters((String)itemAtPosition);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerChapters.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addChaptersToSpinner();

        spinnerVerses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);

                verse = Integer.parseInt((String)itemAtPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addChaptersToSpinner() {
        spinnerChapters = (Spinner)findViewById(R.id.spinnerchapters);

        spinnerChapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);

                chapter = Integer.parseInt((String)itemAtPosition);

                List<String> list = navigator.getVerses(book, chapter);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(parent.getContext(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerVerses.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}