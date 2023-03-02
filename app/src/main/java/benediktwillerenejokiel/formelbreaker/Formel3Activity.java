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


public class Formel3Activity extends AppCompatActivity {

    Formel formel = (Formel) Data.transfer;

    boolean updateLock = false;

    ArrayList<Unit> units = formel.units;
    Unit resultUnit = units.get(0);
    Unit variable1Unit = units.get(1);
    Unit variable2Unit = units.get(2);

    ArrayAdapter<String> unit1Adapter;
    ArrayAdapter<String> unit2Adapter;
    ArrayAdapter<String> resultUnitAdapter;

    byte selectedFormelIndex = 0;
    byte selectedUnit1Index = 0;
    byte selectedUnit2Index = 0;
    byte selectedResultUnitIndex = 0;
    byte lastResultUnitIndex = 0;

    byte variable1Index = 1;
    byte variable2Index = 2;

    boolean var1constant;
    boolean var2constant;

    boolean formelIsFavorite = Data.isFavorite(formel);

    BigDecimal lastResult = new BigDecimal(0);
    BigDecimal lastResultNoUnit = new BigDecimal(0);

    ConstraintLayout constraintLayout;

    Spinner umfSpinner;
    MathView mathView;
    TextView variableTV1;
    TextView variableTV2;
    EditText variableET1;
    EditText variableET2;
    Spinner unitSpinner1;
    Spinner unitSpinner2;
    Spinner resultUnitSpinner;
    ImageButton clearButton;
    ImageButton searchButton;
    ImageButton favoriteButton;
    TextView resultDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formel3);

        Options.checkgConstant(formel);
        var1constant = formel.variablen.get(0).constantValue != null;
        var2constant = formel.variablen.get(1).constantValue != null;
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

        constraintLayout = findViewById(R.id.formel3Layout);
        setupBackground();

        umfSpinner = findViewById(R.id.formel3umfSpinner);
        mathView = findViewById(R.id.formel3MathView);
        variableTV1 = findViewById(R.id.formel3VariableTV1);
        variableTV2 = findViewById(R.id.formel3VariableTV2);
        variableET1 = findViewById(R.id.formel3VariableET1);
        variableET2 = findViewById(R.id.formel3VariableET2);
        unitSpinner1 = findViewById(R.id.formel3UnitSpinner1);
        unitSpinner2 = findViewById(R.id.formel3UnitSpinner2);
        resultUnitSpinner = findViewById(R.id.formel3ResultUnitSpinner);
        clearButton = findViewById(R.id.formel3clearImageButton);
        searchButton = findViewById(R.id.formel3searchImageButton);
        favoriteButton = findViewById(R.id.formel3favoriteImageButton);
        resultDisplay = findViewById(R.id.formel3ResultDisplay);

        ArrayList<String> umformungsPreviews;
        umformungsPreviews = new ArrayList<>(formel.variablePreviews);

        for(Variable var : formel.variablen) {

            if(var.constantValue != null) {
                umformungsPreviews.remove(formel.variablen.indexOf(var));
            }

        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(Formel3Activity.this, R.layout.formel_spinner_item, umformungsPreviews.toArray(new String [umformungsPreviews.size()]));
        umfSpinner.setAdapter(adapter);

        WebSettings mathViewSettings = mathView.getSettings();
        mathViewSettings.setTextZoom(135);

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
                    case 0: if (var1constant) {
                                variable1Index = 0;
                                variable2Index = 2;
                                variable1Unit = units.get(0);
                                variable2Unit = units.get(2);
                                resultUnit = units.get(1);
                            } else {
                                variable1Index = 1;
                                variable2Index = 2;
                                variable1Unit = units.get(1);
                                variable2Unit = units.get(2);
                                resultUnit = units.get(0);
                            }
                            break;
                    case 1: if (var1constant || var2constant) {
                                variable1Index = 0;
                                variable2Index = 1;
                                variable1Unit = units.get(0);
                                variable2Unit = units.get(1);
                                resultUnit = units.get(2);
                            } else {
                                variable1Index = 0;
                                variable2Index = 2;
                                variable1Unit = units.get(0);
                                variable2Unit = units.get(2);
                                resultUnit = units.get(1);
                            }
                            break;
                    case 2: variable1Index = 0;
                            variable2Index = 1;
                            variable1Unit = units.get(0);
                            variable2Unit = units.get(1);
                            resultUnit = units.get(2);

                }

                updateViews();
                updateLock = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        unitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                selectedUnit1Index = (byte) variable1Unit.singleUnits.indexOf(adapterView.getItemAtPosition(pos));

                if(!variableET1.getText().toString().isEmpty() && !variableET2.getText().toString().isEmpty() && !updateLock)
                    showResult();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        unitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                selectedUnit2Index = (byte) variable2Unit.singleUnits.indexOf(adapterView.getItemAtPosition(pos));

                if(!variableET1.getText().toString().isEmpty() && !variableET2.getText().toString().isEmpty() && !updateLock)
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

        variableET1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!variableET1.getText().toString().isEmpty() && !variableET2.getText().toString().isEmpty())
                    showResult();

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        variableET2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!variableET1.getText().toString().isEmpty() && !variableET2.getText().toString().isEmpty())
                    showResult();

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(formel.variablen.get(variable1Index).constantValue == null)
                    variableET1.setText("");
                if (formel.variablen.get(variable2Index).constantValue == null)
                    variableET2.setText("");

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Formel3Activity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(formelIsFavorite) {
                    try {
                        FileUtil.deleteFavorite(formel, Formel3Activity.this);
                        favoriteButton.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_off));
                        formelIsFavorite = false;
                    } catch (Exception e) {e.printStackTrace();}

                    // Ein Toast wird angezeigt um den Nutzer über die Löschung des Favorits zu benachrichtigen
                    String toastText = null;
                    if(Options.lang.equals("DE"))
                        toastText = "Favorit gelöscht";
                    else if(Options.lang.equals("EN"))
                        toastText = "Favorite deleted";
                    Toast toast = Toast.makeText(Formel3Activity.this, toastText, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    try {
                        FileUtil.saveFavorite(formel, Formel3Activity.this);
                        favoriteButton.setImageDrawable(getDrawable(android.R.drawable.btn_star_big_on));
                        formelIsFavorite = true;
                    } catch (Exception e) {e.printStackTrace();}

                    // Ein Toast wird angezeigt um den Nutzer über die Speicherung des Favorits zu benachrichtigen
                    String toastText = null;
                    if(Options.lang.equals("DE"))
                        toastText = "Favorit gespeichert";
                    else if(Options.lang.equals("EN"))
                        toastText = "Favorite saved";
                    Toast toast = Toast.makeText(Formel3Activity.this, toastText, Toast.LENGTH_SHORT);
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

        if (!variableET1.isEnabled()) {variableET1.setText(""); variableET1.setEnabled(true); variableET1.setFocusableInTouchMode(true);}
        if (!variableET2.isEnabled()) {variableET2.setText(""); variableET2.setEnabled(true); variableET2.setFocusableInTouchMode(true);}

        if(!variable1Unit.identifier.equals("none")) {
            if (unitSpinner1.getVisibility() == View.INVISIBLE) {unitSpinner1.setVisibility(View.VISIBLE);}
            String [] singleUnits = variable1Unit.singleUnits.toArray(new String [variable1Unit.singleUnits.size()]);
            unit1Adapter = new ArrayAdapter<>(Formel3Activity.this, R.layout.unit_spinner_item, singleUnits);
            unit1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            unitSpinner1.setAdapter(unit1Adapter);
            unitSpinner1.setSelection(variable1Unit.singleUnits.indexOf(variable1Unit.singleUnits.get(variable1Unit.values.indexOf(1d))));
        } else {
            unitSpinner1.setVisibility(View.INVISIBLE);
            selectedUnit1Index = 0;
        }

        if(!variable2Unit.identifier.equals("none")) {
            if (unitSpinner2.getVisibility() == View.INVISIBLE) {unitSpinner2.setVisibility(View.VISIBLE);}
            String [] singleUnits2 = variable2Unit.singleUnits.toArray(new String [variable2Unit.singleUnits.size()]);
            unit2Adapter = new ArrayAdapter<>(Formel3Activity.this, R.layout.unit_spinner_item, singleUnits2);
            unit2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            unitSpinner2.setAdapter(unit2Adapter);
            unitSpinner2.setSelection(variable2Unit.singleUnits.indexOf(variable2Unit.singleUnits.get(variable2Unit.values.indexOf(1d))));
        } else {
            unitSpinner2.setVisibility(View.INVISIBLE);
            selectedUnit2Index = 0;
        }

        if(!resultUnit.identifier.equals("none")) {
            if (resultUnitSpinner.getVisibility() == View.INVISIBLE) {resultUnitSpinner.setVisibility(View.VISIBLE);}
            String [] singleResultUnits = resultUnit.singleUnits.toArray(new String [resultUnit.singleUnits.size()]);
            resultUnitAdapter = new ArrayAdapter<>(Formel3Activity.this, R.layout.unit_spinner_item, singleResultUnits);
            resultUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            resultUnitSpinner.setAdapter(resultUnitAdapter);
            resultUnitSpinner.setSelection(resultUnit.singleUnits.indexOf(resultUnit.singleUnits.get(resultUnit.values.indexOf(1d))));
        } else {
            resultUnitSpinner.setVisibility(View.INVISIBLE);
            selectedResultUnitIndex = 0;
        }

        variableTV1.setText(formel.variablen.get(variable1Index).preview);
        variableTV2.setText(formel.variablen.get(variable2Index).preview);

        if(formel.variablen.get(variable1Index).constantValue != null) {
            variableET1.setText(formel.variablen.get(variable1Index).constantPreview);
            variableET1.setEnabled(false);
            variableET1.setFocusableInTouchMode(false);
        }
        if(formel.variablen.get(variable2Index).constantValue != null) {
            variableET2.setText(formel.variablen.get(variable2Index).constantPreview);
            variableET2.setEnabled(false);
            variableET2.setFocusableInTouchMode(false);
        }

    }

    /*
       Methode zum Ausgeben des aktuellen Ergebnisses im resultDisplay
       Wird aus den Listenern verschiedener Views heraus aufgerufen um bei jeder Änderung der
       Parameter ein neues, aktuelles Ergebnis auszugeben
    */
    private void showResult() {

        if((variableET1.getText().toString().isEmpty() || variableET2.getText().toString().isEmpty()) && !resultDisplay.getText().toString().isEmpty()) {
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

        String input1;
        String input2;

        try {

            if (!updateLock) {

                input1 = variableET1.getText().toString();
                input2 = variableET2.getText().toString();

                if (formel.variablen.get(variable1Index).constantValue != null) {
                    input1 = formel.variablen.get(variable1Index).constantValue;
                } else {
                    input1 = String.valueOf((Double.valueOf(input1)) * (variable1Unit.values.get(selectedUnit1Index)));
                }
                if (formel.variablen.get(variable2Index).constantValue != null) {
                    input2 = formel.variablen.get(variable2Index).constantValue;
                } else {
                    input2 = String.valueOf((Double.valueOf(input2)) * (variable2Unit.values.get(selectedUnit2Index)));
                }

                BigDecimal finalResult = calculateResult(input1, input2);

                lastResult = finalResult;

                finalResult = MathUtil.roundBigDecimal(finalResult);

                resultDisplay.setText(MathUtil.formatNumber(finalResult));

            }

        } catch (Exception e) {
            e.printStackTrace();
            resultDisplay.setText("0");
        }

    }

    private BigDecimal calculateResult(String input1, String input2) {

        BigDecimal result;

        Expression parsedFormel = new Expression(formel.umformungen.get(selectedFormelIndex));

        Argument arg1 = new Argument(formel.variablen.get(variable1Index).symbol, input1);
        Argument arg2 = new Argument(formel.variablen.get(variable2Index).symbol, input2);

        parsedFormel.addArguments(arg1, arg2);

        parsedFormel.setVerboseMode();

        result = BigDecimal.valueOf(parsedFormel.calculate());

        lastResultNoUnit = result;

        result = result.divide(BigDecimal.valueOf(resultUnit.values.get(selectedResultUnitIndex)), 24, RoundingMode.HALF_UP);

        return result;

    }



}
