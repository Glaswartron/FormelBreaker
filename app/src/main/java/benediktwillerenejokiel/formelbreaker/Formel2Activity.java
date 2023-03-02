/*
    © Benedikt Wille, Rene Jokiel 2018
    Urheber: Benedikt Wille, Rene Jokiel

    Dieses Dokument ist gemäß deutschem Urheberschutz geschützt

    Die Vervielfältigung, Bearbeitung, Verbreitung und jede Art der Verwertung außerhalb der Grenzen
    des Urheberrechtes bedürfen der schriftlichen Zustimmung des jeweiligen Autors bzw. Erstellers

    Die Vervielfältigung oder Neu-Umsetzung (auch einzelner Komponenten) der Software auf
    Basis der Software wird Dritten nicht gestattet

    Diese Software nutzt die mXparser-Library von Mariusz Gromada:
    Copyright 2010-2017 MARIUSZ GROMADA. All rights reserved. You may use this software under the condition of Simplified BSD License.
*/


package benediktwillerenejokiel.formelbreaker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import io.github.kexanie.library.MathView;

public class Formel2Activity extends AppCompatActivity {

    Formel formel = (Formel) Data.transfer;

    boolean updateLock = false;

    ArrayList<Unit> units = formel.units;
    Unit resultUnit = units.get(0);
    Unit variableUnit = units.get(1);

    ArrayAdapter<String> unitAdapter;
    ArrayAdapter<String> resultUnitAdapter;

    byte selectedFormelIndex = 0;
    byte selectedUnitIndex = 0;
    byte selectedResultUnitIndex = 0;
    byte lastResultUnitIndex = 0;

    boolean formelIsFavorite = Data.isFavorite(formel);

    BigDecimal lastResult = new BigDecimal(0);
    BigDecimal lastResultNoUnit = new BigDecimal(0);

    ConstraintLayout constraintLayout;

    Spinner umfSpinner;
    MathView mathView;
    TextView variableTV;
    EditText variableET;
    Spinner unitSpinner;
    Spinner resultUnitSpinner;
    ImageButton clearButton;
    ImageButton searchButton;
    ImageButton favoriteButton;
    TextView resultDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formel2);

        setupUIViews();

    }

    @Override
    protected void onResume() {

        super.onResume();

        if(Data.isFavorite(formel)) {
            favoriteButton.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_on));
        } else {
            favoriteButton.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_off));
        }

    }

    /*
       Referenzvariablen werden mit Views verknüpft
       Views werden Attribute der Formel als Text etc. zugewiesen
       Funktionalität für alle Buttons und Spinner
    */
    private void setupUIViews() {

        constraintLayout = findViewById(R.id.formel2Layout);
        setupBackground();

        umfSpinner = findViewById(R.id.formel2umfSpinner);
        mathView = findViewById(R.id.formel2MathView);
        variableTV = findViewById(R.id.formel2VariableTV);
        variableET = findViewById(R.id.formel2VariableET);
        unitSpinner = findViewById(R.id.formel2UnitSpinner);
        resultUnitSpinner = findViewById(R.id.formel2ResultUnitSpinner);
        clearButton = findViewById(R.id.formel2clearImageButton);
        searchButton = findViewById(R.id.formel2searchImageButton);
        favoriteButton = findViewById(R.id.formel2favoriteImageButton);
        resultDisplay = findViewById(R.id.formel2ResultDisplay);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(Formel2Activity.this, R.layout.formel_spinner_item, formel.variablePreviews.toArray(new String [formel.variablePreviews.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        umfSpinner.setAdapter(adapter);

        WebSettings mathViewSettings = mathView.getSettings();
        mathViewSettings.setTextZoom(150);

        updateViews();

        //mathView.reload();

        if(formelIsFavorite) {
            favoriteButton.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_on));
        }

        umfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                updateLock = true;

                selectedFormelIndex = (byte) pos;

                switch(selectedFormelIndex) {
                    case 0: variableUnit = units.get(1);
                            resultUnit = units.get(0);
                            break;
                    case 1: variableUnit = units.get(0);
                            resultUnit = units.get(1);
                }

                updateViews();
                updateLock = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                    selectedUnitIndex = (byte) variableUnit.singleUnits.indexOf(adapterView.getItemAtPosition(pos));

                    if(!variableET.getText().toString().isEmpty())
                        showResult();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        resultUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                lastResultUnitIndex = selectedResultUnitIndex;
                selectedResultUnitIndex = (byte) resultUnit.singleUnits.indexOf(adapterView.getItemAtPosition(pos));

                if(!updateLock)
                    showResult();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        variableET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!variableET.getText().toString().isEmpty())
                    showResult();

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                variableET.setText("");

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Formel2Activity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(formelIsFavorite) {
                    try {
                        FileUtil.deleteFavorite(formel, Formel2Activity.this);
                        favoriteButton.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_off));
                        formelIsFavorite = false;
                    } catch (Exception e) {e.printStackTrace();}

                    // Ein Toast wird angezeigt um den Nutzer über die Löschung des Favorits zu benachrichtigen
                    String toastText = null;
                    if(Options.lang.equals("DE"))
                        toastText = "Favorit gelöscht";
                    else if(Options.lang.equals("EN"))
                        toastText = "Favorite deleted";
                    Toast toast = Toast.makeText(Formel2Activity.this, toastText, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    try {
                        FileUtil.saveFavorite(formel, Formel2Activity.this);
                        favoriteButton.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_on));
                        formelIsFavorite = true;
                    } catch (Exception e) {e.printStackTrace();}

                    // Ein Toast wird angezeigt um den Nutzer über die Speicherung als Favorit zu benachrichtigen
                    String toastText = null;
                    if(Options.lang.equals("DE"))
                        toastText = "Favorit gespeichert";
                    else if(Options.lang.equals("EN"))
                        toastText = "Favorite saved";

                    Toast toast = Toast.makeText(Formel2Activity.this, toastText, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    // Methode zum Auswählen des passenden Backgrounds je nach gewähltem Field
    private void setupBackground() {
        Drawable background = null;
        switch (Data.currentField) {
            case "maths":
                background = getDrawable(R.drawable.maths_clean_background);
                break;
            case "physics":
                background = getDrawable(R.drawable.physics_clean_background);
                break;
            case "chemistry":
                background = getDrawable(R.drawable.chemistry_clean_background);
        }
        constraintLayout.setBackground(background);
    }

    /*
       Methode zum Updaten aller beteiligten Views nachdem eine neue Umformung aus dem umfSpinner ausgewählt wurde
       Wird aus dem onItemSelectedListener des umfSpinner heraus aufgerufen
    */
    private void updateViews() {

        mathView.setText(formel.latexStrings.get(selectedFormelIndex));

        if(!variableUnit.identifier.equals("none")) {
            if (unitSpinner.getVisibility() == View.INVISIBLE) {unitSpinner.setVisibility(View.VISIBLE);}
            String [] singleUnits = variableUnit.singleUnits.toArray(new String [variableUnit.singleUnits.size()]);
            unitAdapter = new ArrayAdapter<>(Formel2Activity.this, R.layout.unit_spinner_item, singleUnits);
            unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            unitSpinner.setAdapter(unitAdapter);
            unitSpinner.setSelection(variableUnit.singleUnits.indexOf(variableUnit.singleUnits.get(variableUnit.values.indexOf(1d))));
        } else {
            unitSpinner.setVisibility(View.INVISIBLE);
            selectedUnitIndex = 0;
        }

        if(!resultUnit.identifier.equals("none")) {
            if (resultUnitSpinner.getVisibility() == View.INVISIBLE) {resultUnitSpinner.setVisibility(View.VISIBLE);}
            String [] singleResultUnits = resultUnit.singleUnits.toArray(new String [resultUnit.singleUnits.size()]);
            resultUnitAdapter = new ArrayAdapter<>(Formel2Activity.this, R.layout.unit_spinner_item, singleResultUnits);
            resultUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            resultUnitSpinner.setAdapter(resultUnitAdapter);
            resultUnitSpinner.setSelection(resultUnit.singleUnits.indexOf(resultUnit.singleUnits.get(resultUnit.values.indexOf(1d))));
        } else {
            resultUnitSpinner.setVisibility(View.INVISIBLE);
            selectedResultUnitIndex = 0;
        }

        variableTV.setText(formel.variablen.get(selectedFormelIndex == 0 ? 1 : 0).preview);
    }

    /*
       Methode zum Ausgeben des aktuellen Ergebnisses im resultDisplay
       Wird aus den Listenern verschiedener Views heraus aufgerufen um bei jeder Änderung der
       Parameter ein neues, aktuelles Ergebnis auszugeben
    */
    private void showResult() {

        if((variableET.getText().toString().isEmpty() && !resultDisplay.getText().toString().isEmpty())) {
            BigDecimal roundedResult;
            if(selectedResultUnitIndex == lastResultUnitIndex)
                roundedResult = lastResult;
            else
                roundedResult = lastResultNoUnit;

            roundedResult = roundedResult.divide(BigDecimal.valueOf(resultUnit.values.get(selectedResultUnitIndex)), 24, RoundingMode.HALF_UP);
            roundedResult = MathUtil.roundBigDecimal(roundedResult);
            resultDisplay.setText(MathUtil.formatNumber(roundedResult));
            return;
        }

        String input;

        try {

            if(!updateLock) {

                input = variableET.getText().toString();

                input = String.valueOf((Double.valueOf(input)) * (variableUnit.values.get(selectedUnitIndex)));

                BigDecimal finalResult = calculateResult(input);

                lastResult = finalResult;

                finalResult = MathUtil.roundBigDecimal(finalResult);

                resultDisplay.setText(MathUtil.formatNumber(finalResult));

            }

        } catch (Exception e) {
            resultDisplay.setText("0");
        }

    }

    private BigDecimal calculateResult(String input) {

        BigDecimal result;

        Expression parsedFormel = new Expression(formel.umformungen.get(selectedFormelIndex));
        Argument arg = null;

        switch(selectedFormelIndex) {
            case 0: arg = new Argument(formel.variablen.get(1).symbol, input);
                    break;
            case 1: arg = new Argument(formel.variablen.get(0).symbol, input);
        }

        parsedFormel.addArguments(arg);

        parsedFormel.setVerboseMode();

        result = BigDecimal.valueOf(parsedFormel.calculate());

        lastResultNoUnit = result;

        return result.divide(BigDecimal.valueOf(resultUnit.values.get(selectedResultUnitIndex)), 24, RoundingMode.HALF_UP);

    }



}