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

public class CategoryChooserActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    LinearLayout layout;

    String currentFieldLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);

        currentFieldLocal = Data.currentField;

        setupUIViews();


    }

    /*
       Algorithmus zum Füllen des ScrollView bzw. LinearLayout mit generischen Buttons
       Für jede Category aus der in Data.transfer gespeicherten Category-Liste wird ein Button samt OnClickListener erstellt
    */
    private void setupUIViews() {

        layout = findViewById(R.id.categoryLayout);
        constraintLayout = findViewById(R.id.categoryChooserLayout);
        setupBackground();

        Drawable buttonBackground = null;
        if (Data.currentField.equals("maths"))
            buttonBackground = getDrawable(R.drawable.maths_buttonshape);
        else if (Data.currentField.equals("physics"))
            buttonBackground = getDrawable(R.drawable.physics_buttonshape);
        else if (Data.currentField.equals("chemistry"))
            buttonBackground = getDrawable(R.drawable.chemistry_buttonshape);

        Button button;
        for (final Category cat : ((ArrayList<Category>) Data.transfer)) {

            button = new Button(this);
            button.setBackground(buttonBackground);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setElevation(30);
            button.setTextSize(20);
            button.setAllCaps(false);

            button.setText(cat.buttonText);

            layout.addView(button);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Data.transfer = cat;

                    Data.currentField = currentFieldLocal;

                    Intent intent = new Intent(CategoryChooserActivity.this, FormelChooserActivity.class);
                    startActivity(intent);

                }
            });

        }

    }

    // Methode zum Auswählen des passenden Backgrounds je nach gewähltem Field
    private void setupBackground() {
        Drawable background = null;
        if(Data.currentField.equals("maths"))
            background = getDrawable(R.drawable.maths_background);
        else if(Data.currentField.equals("physics"))
            background = getDrawable(R.drawable.physics_background);
        else if(Data.currentField.equals("chemistry"))
            background = getDrawable(R.drawable.chemistry_background);

        constraintLayout.setBackground(background);
    }
}
