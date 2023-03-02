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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {

    Spinner langSpinner;
    SeekBar roundSeekBar;
    TextView roundValueTextView;
    Spinner gConstantSpinner;
    Button legalButton;

    // Interagiert der User gerade mit dem Menü ? Wird aus void OnUserInteraction() heraus gesetzt
    boolean userIsInteracting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        setupUIViews();

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    /*
       Referenzvariablen werden mit Views verknüpft
       Funktionalität für alle Views
    */
    private void setupUIViews() {

        langSpinner = findViewById(R.id.options_lang_spinner);
        roundSeekBar = findViewById(R.id.options_round_seekBar);
        roundValueTextView = findViewById(R.id.options_roundValue_textView);
        gConstantSpinner = findViewById(R.id.options_gconstant_spinner);
        legalButton = findViewById(R.id.legal_button);

        ArrayAdapter<CharSequence> langAdapter;
        langAdapter = ArrayAdapter.createFromResource(this, R.array.options_langs_spinner_items, android.R.layout.simple_spinner_item);

        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> gConstantAdapter;
        gConstantAdapter = ArrayAdapter.createFromResource(this, R.array.options_gconstant_spinner_items, android.R.layout.simple_spinner_item);

        gConstantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        langSpinner.setAdapter(langAdapter);
        gConstantSpinner.setAdapter(gConstantAdapter);

        if(Options.lang.equals("DE"))
            langSpinner.setSelection(0);
        else if(Options.lang.equals("EN"))
            langSpinner.setSelection(1);

        roundSeekBar.setProgress(Options.roundScale - 1);
        roundValueTextView.setText(String.valueOf(Options.roundScale));

        if(Options.gconstant)
            gConstantSpinner.setSelection(1);
        else
            gConstantSpinner.setSelection(0);

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                if (userIsInteracting) {
                    try {
                        switch (pos) {
                            case 0:
                                Options.lang = "DE";
                                FileUtil.editOptions(OptionsActivity.this, 0, "DE");
                                break;
                            case 1:
                                Options.lang = "EN";
                                FileUtil.editOptions(OptionsActivity.this, 0, "EN");
                        }

                        showLangChangeAlert();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        roundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (userIsInteracting) {
                    try {
                        FileUtil.editOptions(OptionsActivity.this, 1, String.valueOf((progress + 1)));
                        roundValueTextView.setText(String.valueOf(progress + 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        gConstantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                if (userIsInteracting) {
                    try {
                        switch (pos) {
                            case 0:
                                FileUtil.editOptions(OptionsActivity.this, 2, "false");
                                break;
                            case 1:
                                FileUtil.editOptions(OptionsActivity.this, 2, "true");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        legalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OptionsActivity.this, LegalActivity.class);
                startActivity(intent);

            }
        });

    }

    private void showLangChangeAlert() {
        String messageDE = "Bitte beachte, dass die Änderung der Sprache im Optionsmenü" +
                           " aufgrund von Android-Beschränkungen nur die Sprache der Formeln," +
                           " Variablen, etc. ändert. Damit auch die UI in einer anderen Sprache" +
                           " angezeigt wird, musst du die Systemsprache deines Smartphones" +
                           " entsprechend ändern";
        String messageEN = "Please note that due to Android limitations changing the language" +
                           " from the options menu only changes the language of the formulas," +
                           " variables, etc. If the UI shall also be displayed in another language" +
                           " it is necessary to change the system language of your smartphone accordingly";
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(OptionsActivity.this);
        alertBuilder.setMessage(Options.lang.equals("DE") ? messageDE : messageEN);
        AlertDialog dialog = alertBuilder.create();
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
