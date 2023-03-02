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
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FormelChooserActivity extends AppCompatActivity {

    LinearLayout layout;
    ConstraintLayout constraintLayout;
    Category category;
    Button switchButton;
    boolean formelPreviewSelected = false;
    ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formel_chooser);

        setupUIViews();

    }

    /*
       Algorithmus zum Füllen des ScrollView bzw. LinearLayout mit generischen Buttons
       Für jede Formel aus der in Data.transfer gespeicherten Category wird ein Button samt OnClickListener erstellt
       Je nachdem, wie viele Umformungen die Formel hat, wird zwischen verschiedenen Calculator-Activities ausgewählt
       Und: Funktionalität für den SwitchButton
    */
    private void setupUIViews() {

        layout = findViewById(R.id.formelLayout);
        constraintLayout = findViewById(R.id.formelChooserLayout);
        setupBackground();

        switchButton = findViewById(R.id.formelSwitchButton);

        category = (Category) Data.transfer;

        Drawable buttonBackground = null;
        if (Data.currentField.equals("maths"))
            buttonBackground = getDrawable(R.drawable.maths_buttonshape);
        else if (Data.currentField.equals("physics"))
            buttonBackground = getDrawable(R.drawable.physics_buttonshape);
        else if (Data.currentField.equals("chemistry"))
            buttonBackground = getDrawable(R.drawable.chemistry_buttonshape);

        Button button;
        for(final Formel formel : category.formeln) {

            button = new Button(this);
            button.setBackground(buttonBackground);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setTextSize(20);
            button.setAllCaps(false);
            button.setText(formel.buttonText);
            buttons.add(button);
            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Data.transfer = formel;

                    Intent intent = null;

                    int variablenSize = formel.variablen.size();

                    switch (variablenSize) {
                        case 2: intent = new Intent(FormelChooserActivity.this, Formel2Activity.class);
                                break;
                        case 3: intent = new Intent(FormelChooserActivity.this, Formel3Activity.class);
                                break;
                        case 4: intent = new Intent(FormelChooserActivity.this, Formel4Activity.class);
                                break;
                        case 5: intent = new Intent(FormelChooserActivity.this, Formel5Activity.class);
                    }

                    startActivity(intent);

                }
            });

        }

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!formelPreviewSelected) {
                    formelPreviewSelected = true;
                    switchButton.setText(R.string.switchButtonLabel2);
                }
                else {
                    formelPreviewSelected = false;
                    switchButton.setText(R.string.switchButtonLabel1);
                }

                updateButtonText();

            }
        });


    }

    // Methode zum Auswählen des passenden Backgrounds je nach gewähltem Field
    private void setupBackground() {
        Drawable background = null;
        switch (Data.currentField) {
            case "maths":
                background = getDrawable(R.drawable.maths_background);
                break;
            case "physics":
                background = getDrawable(R.drawable.physics_background);
                break;
            case "chemistry":
                background = getDrawable(R.drawable.chemistry_background);
        }
        constraintLayout.setBackground(background);
    }

    /*
       Methode zum ändern der Button-Texte bei Klick auf Name/Formel
       Wird aus dem OnClickListener des Buttons heraus ausgeführt, der in setupUIViews() instanziiert wird
    */
    private void updateButtonText() {

        if (formelPreviewSelected) {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setText(category.formeln.get(i).preview);
            }
        } else {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setText(category.formeln.get(i).buttonText);
            }
        }

    }


}
