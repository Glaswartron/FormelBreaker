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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    Button switchButton;
    LinearLayout layout;

    boolean formelPreviewSelected = false;
    ArrayList<Formel> formeln = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        setupUIViews();

    }

    @Override
    protected void onResume() {

        super.onResume();

        formeln.clear();
        buttons.clear();
        populateScrollView();

    }

    private void setupUIViews() {

        switchButton = findViewById(R.id.favoritesSwitchButton);
        layout = findViewById(R.id.favoritesLinearLayout);

        populateScrollView();

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

    private void populateScrollView() {

        layout.removeAllViews();
        formelPreviewSelected = false;

        if(Data.favorites.size() == 0) {
            Button button = new Button(this);
            button.setBackground(getDrawable(R.drawable.locked_buttonshape));
            button.setTextColor(getResources().getColor(R.color.white));
            button.setTextSize(20);
            button.setAllCaps(false);

            button.setText(R.string.noFavorites);

            layout.addView(button);

            return;
        }

        Button button;
        for (final Formel formel : Data.favorites) {

            button = new Button(this);

            Drawable buttonBackground = null;

            boolean found = false;
            for (Category cat : Data.mathsCategories) {
                for (Formel frml : cat.formeln) {
                    if(frml.favoriteIdentifier.equals(formel.favoriteIdentifier)) {
                        buttonBackground = getDrawable(R.drawable.maths_buttonshape);
                        found = true;
                        break;
                    }
                }
            }
            if(!found) {
                for (Category cat : Data.physicsCategories) {
                    for (Formel frml : cat.formeln) {
                        if (frml.favoriteIdentifier.equals(formel.favoriteIdentifier)) {
                            buttonBackground = getDrawable(R.drawable.physics_buttonshape);
                            found = true;
                            break;
                        }
                    }
                }
            }
            if(!found) {
                for (Category cat : Data.chemistryCategories) {
                    for (Formel frml : cat.formeln) {
                        if (frml.favoriteIdentifier.equals(formel.favoriteIdentifier)) {
                            buttonBackground = getDrawable(R.drawable.chemistry_buttonshape);
                            break;
                        }
                    }
                }
            }

            button.setBackground(buttonBackground);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setTextSize(20);
            button.setAllCaps(false);
            button.setText(formel.buttonText);
            formeln.add(formel);
            buttons.add(button);
            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Data.transfer = formel;

                    boolean found = false;
                    for (Category cat : Data.mathsCategories) {
                        for (Formel frml : cat.formeln) {
                            if(frml.buttonText.equals(formel.buttonText)) {
                                Data.currentField = "maths";
                                found = true;
                                break;
                            }
                        }
                    }
                    if(!found) {
                        for (Category cat : Data.physicsCategories) {
                            for (Formel frml : cat.formeln) {
                                if (frml.buttonText.equals(formel.buttonText)) {
                                    Data.currentField = "physics";
                                    found = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(!found) {
                        for (Category cat : Data.chemistryCategories) {
                            for (Formel frml : cat.formeln) {
                                if (frml.buttonText.equals(formel.buttonText)) {
                                    Data.currentField = "chemistry";
                                    break;
                                }
                            }
                        }
                    }

                    Intent intent = null;

                    int variablenSize = formel.variablen.size();

                    switch (variablenSize) {
                        case 2: intent = new Intent(FavoriteActivity.this, Formel2Activity.class);
                            break;
                        case 3: intent = new Intent(FavoriteActivity.this, Formel3Activity.class);
                            break;
                        case 4: intent = new Intent(FavoriteActivity.this, Formel4Activity.class);
                            break;
                        case 5: intent = new Intent(FavoriteActivity.this, Formel5Activity.class);
                    }

                    startActivity(intent);

                }
            });


        }


    }

    private void updateButtonText() {

        if (formelPreviewSelected) {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setText(formeln.get(i).preview);
            }
        } else {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setText(formeln.get(i).buttonText);
            }
        }


    }
}
