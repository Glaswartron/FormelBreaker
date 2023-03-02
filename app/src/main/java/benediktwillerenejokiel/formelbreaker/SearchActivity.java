/*
    © Benedikt Wille, Rene Jokiel 2018
    Urheber: Benedikt Wille, Rene Jokiel

    Dieses Dokument ist gemäß deutschem Urheberschutz geschützt

    Die Vervielfältigung, Bearbeitung, Verbreitung und jede Art der Verwertung außerhalb der Grenzen
    des Urheberrechtes bedürfen der schriftlichen Zustimmung des jeweiligen Autors bzw. Erstellers

    Die Vervielfältigung oder Neu-Umsetzung (auch einzelner Komponenten) der Software auf
    Basis der Software wird Dritten nicht gestattet
*/

package benediktwillerenejokiel.formelbreaker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText queryEditText;
    Spinner fieldSpinner;
    Spinner typeSpinner;
    LinearLayout resultsLayout;

    private static String selectedField = "all";
    private static String selectedSearchType = "formelname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupUIViews();

    }

    private void setupUIViews() {

        queryEditText = findViewById(R.id.searchEditText);
        fieldSpinner = findViewById(R.id.searchFieldSpinner);
        typeSpinner = findViewById(R.id.searchTypeSpinner);
        resultsLayout = findViewById(R.id.searchLinearLayout);

        ArrayAdapter<CharSequence> fieldAdapter;
        fieldAdapter = ArrayAdapter.createFromResource(this, R.array.search_field_spinner_items, android.R.layout.simple_spinner_item);

        fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> typeAdapter;
        typeAdapter = ArrayAdapter.createFromResource(this, R.array.search_type_spinner_items, android.R.layout.simple_spinner_item);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fieldSpinner.setAdapter(fieldAdapter);
        typeSpinner.setAdapter(typeAdapter);

        fieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                switch(pos) {
                    case 0: selectedField = "all";
                            break;
                    case 1: selectedField = "maths";
                            break;
                    case 2: selectedField = "physics";
                            break;
                    case 3: selectedField = "chemistry";
                }

                runSearch();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                switch(pos) {
                    case 0: selectedSearchType = "formelname";
                            break;
                    case 1: selectedSearchType = "variablename";
                            break;
                    case 2: selectedSearchType = "variablesymbol";
                            break;
                    case 3: selectedSearchType = "category";
                }

                runSearch();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                runSearch();

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


    }

    private void runSearch() {

        String query = queryEditText.getText().toString();
        if(query.equals(" ")) {
            return;
        }
        ArrayList<Category> searchPool = new ArrayList<>();

        ArrayList<?> results = new ArrayList<>();

        switch (selectedField) {
            case "all":
                searchPool.addAll(Data.mathsCategories);
                searchPool.addAll(Data.physicsCategories);
                searchPool.addAll(Data.chemistryCategories);
                break;
            case "maths":
                searchPool.addAll(Data.mathsCategories);
                break;
            case "physics":
                searchPool.addAll(Data.physicsCategories);
                break;
            case "chemistry":
                searchPool.addAll(Data.chemistryCategories);
        }

        switch (selectedSearchType) {
            case "formelname":
                results = SearchUtil.searchByFormelName(query, searchPool);
                break;
            case "variablename":
                results = SearchUtil.searchByVariableName(query, searchPool);
                break;
            case "variablesymbol":
                results = SearchUtil.searchByVariableSymbol(query, searchPool);
                break;
            case "category":
                results = SearchUtil.searchCategory(query, searchPool);
        }

        populateScrollView(results);

    }

    private void populateScrollView(ArrayList<?> results) {

        resultsLayout.removeAllViews();

        if(results.size() == 0) {
            Button button = new Button(this);
            button.setBackground(getDrawable(R.drawable.locked_buttonshape));
            button.setTextColor(getResources().getColor(R.color.white));
            button.setTextSize(18);
            button.setAllCaps(false);

            button.setText(R.string.noSearchResults);

            resultsLayout.addView(button);
        }

        if(selectedSearchType.equals("category")) {

            Button button;
            Drawable buttonBackground = null;
            for (final Category cat : (ArrayList<Category>) results) {
                if(Data.mathsCategories.contains(cat)) {
                    buttonBackground = getDrawable(R.drawable.maths_buttonshape);
                } else if(Data.physicsCategories.contains(cat)) {
                    buttonBackground = getDrawable(R.drawable.physics_buttonshape);
                } else if(Data.chemistryCategories.contains(cat)) {
                    buttonBackground = getDrawable(R.drawable.chemistry_buttonshape);
                }

                button = new Button(this);
                button.setBackground(buttonBackground);
                button.setTextColor(getResources().getColor(R.color.white));
                button.setTextSize(20);
                button.setAllCaps(false);

                button.setText(cat.buttonText);

                resultsLayout.addView(button);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Data.transfer = cat;

                        if(Data.mathsCategories.contains(cat)) {
                            Data.currentField = "maths";
                        } else if(Data.physicsCategories.contains(cat)) {
                            Data.currentField = "physics";
                        } else if(Data.chemistryCategories.contains(cat)) {
                            Data.currentField = "chemistry";
                        }

                        Intent intent = new Intent(SearchActivity.this, FormelChooserActivity.class);
                        startActivity(intent);

                    }
                });

            }

        } else {

            Button button;
            for (final Formel formel : (ArrayList<Formel>) results) {

                Drawable buttonBackground = null;

                for (Category cat : Data.mathsCategories) {
                    if(cat.formeln.contains(formel)) {
                        buttonBackground = getDrawable(R.drawable.maths_buttonshape);
                        break;
                    }
                }
                if(buttonBackground == null) {
                    for (Category cat : Data.physicsCategories) {
                        if(cat.formeln.contains(formel)) {
                            buttonBackground = getDrawable(R.drawable.physics_buttonshape);
                            break;
                        }
                    }
                }
                if(buttonBackground == null) {
                    for (Category cat : Data.chemistryCategories) {
                        if(cat.formeln.contains(formel)) {
                            buttonBackground = getDrawable(R.drawable.chemistry_buttonshape);
                            break;
                        }
                    }
                }

                button = new Button(this);
                button.setBackground(buttonBackground);
                button.setTextColor(getResources().getColor(R.color.white));
                button.setTextSize(18);
                button.setAllCaps(false);
                button.setText(formel.buttonText);
                resultsLayout.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Data.transfer = formel;

                        boolean found = false;
                        for (Category cat : Data.mathsCategories) {
                            if(cat.formeln.contains(formel)) {
                                Data.currentField = "maths";
                                found = true;
                                break;
                            }
                        }
                        if(!found) {
                            for (Category cat : Data.physicsCategories) {
                                if (cat.formeln.contains(formel)) {
                                    Data.currentField = "physics";
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if(!found) {
                            for (Category cat : Data.chemistryCategories) {
                                if (cat.formeln.contains(formel)) {
                                    Data.currentField = "chemistry";
                                    break;
                                }
                            }
                        }

                        Intent intent = null;

                        int variablenSize = formel.variablen.size();

                        switch (variablenSize) {
                            case 2: intent = new Intent(SearchActivity.this, Formel2Activity.class);
                                break;
                            case 3: intent = new Intent(SearchActivity.this, Formel3Activity.class);
                                break;
                            case 4: intent = new Intent(SearchActivity.this, Formel4Activity.class);
                                break;
                            case 5: intent = new Intent(SearchActivity.this, Formel5Activity.class);
                        }

                        startActivity(intent);

                    }
                });

            }





        }




    }


}
