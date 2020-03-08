package com.example.convertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Data> conversions = new ArrayList<>();

    ListView listView;
    EditText eValue,search;
    Spinner spinner;
    Adapter adapter;
    ArrayAdapter<CharSequence> adapter_spinner;
    CheckBox checkBox;
    double value_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        eValue = findViewById(R.id.value);
        spinner = findViewById(R.id.spinner);
        search = findViewById(R.id.search);
        checkBox = findViewById(R.id.decimal);

        adapter_spinner = ArrayAdapter.createFromResource(this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter_spinner);

        adapter = new Adapter(this, conversions);

        addConversionsAll();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.checked_decimal = isChecked;
                adapter.notifyDataSetChanged();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterList(conversions);
                listView.setAdapter(adapter);
                for(Data data : conversions) {
                    if(spinner.getSelectedItem().toString().equals(data.type)) {
                        value_x = data.base_laws;
                        break;
                    }
                }
                if(!(eValue.getText().toString().isEmpty()))
                setValuesInList(Double.parseDouble(eValue.getText().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Data> con = new ArrayList<>();
                for(Data data : conversions) {
                    if(data.type.toLowerCase().contains(s.toString().toLowerCase())
                    && !data.type.equals(spinner.getSelectedItem()))
                        con.add(data);
                }
                adapter.listChanged(con);
            }

            @Override
            public void afterTextChanged(Editable s) { }

        });


        eValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty())
                    setValuesInList(0);
                else
                    setValuesInList(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) { }

        });

    }

    private void setValuesInList(double value) {
        for (Data data : conversions)
             data.result = (value)*(value_x)*(data.laws);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.version)
                Toast.makeText(this,"V2.00",Toast.LENGTH_LONG).show();
        return super.onContextItemSelected(item);
    }

    private void filterList(ArrayList<Data> cc) {
        ArrayList<Data> con = new ArrayList<>();
        for (Data data : cc) {
            if (!data.type.equals(spinner.getSelectedItem()))
                con.add(data);
        }
        adapter.listChanged(con);
    }


    private void addConversionsAll() {
        conversions.add(new Data("m", 1,1));
        conversions.add(new Data("cm", 100,.01));
        conversions.add(new Data("mm", 1000,.001));
        conversions.add(new Data("dm", 10,.1));
        conversions.add(new Data("ft", 3.280839895,.3048));
        conversions.add(new Data("inch", 39.37007874,.0254));
        conversions.add(new Data("yd", 1.0936133,.9144));
        conversions.add(new Data("km", .001,1000));
        conversions.add(new Data("mile", .000621371,1609.344));
        conversions.add(new Data("acre", .000247105,4046.86));
        conversions.add(new Data("ha", .0001,10000));
        conversions.add(new Data("um", 1000000,1e-6));
        conversions.add(new Data("NM", 1e+9,1e-9));
    }
	
}
