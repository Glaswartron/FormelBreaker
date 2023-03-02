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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.startapp.android.publish.adsCommon.AutoInterstitialPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

public class StartActivity extends AppCompatActivity {

    Button mathsButton;
    Button physicsButton;
    Button chemistryButton;
    Button moreButton;
    Button searchButton;
    Button favoritesButton;
    Button optionsButton;
    Button constantsButton;
    Button unitsButton;
    Button privacyPolicyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // StartApp SDK Initialization
        StartAppSDK.init(this, "208972692", false);

        //User wurden NICHT nach Zustimmung gefragt bzgl. GDPR und personalisierter Werbung
        StartAppSDK.setUserConsent (this, "pas", System.currentTimeMillis(),false);

        // Autostitials alle 6 Activitystarts
        StartAppAd.enableAutoInterstitial();
        StartAppAd.setAutoInterstitialPreferences(new AutoInterstitialPreferences().setActivitiesBetweenAds(6));

        // Keine Splash Ad beim Start der App
        StartAppAd.disableSplash();

        setContentView(R.layout.activity_start);

        /*
           Überprüft welches Locale vom Gerät verwendet wird - Wird vor
           FileUtil.parseXMLData aufgerufen, damit die in den Optionen
           gesetzte Sprache mit dem Locale verglichen werden kann
        */
        Options.setLanguageFromLocale();

        FileUtil.parseXMLData(StartActivity.this);

        /*
           FileUtil-Methode zum Einlesen der Favoriten aus dem internen Speicher wird aufgerufen
           Funktioniert aus ungeklärten Gründen nicht in FileUtil.parseXMLData :D
        */
        try {
            FileUtil.loadFavorites(StartActivity.this);
        } catch (Exception e) {e.printStackTrace();}

        setupUIViews();

    }



    /*
       Referenzvariablen werden mit Views verknüpft
       Funktionalität für alle Buttons
    */
    private void setupUIViews() {

        mathsButton = findViewById(R.id.mathsButton);
        physicsButton = findViewById(R.id.physicsButton);
        chemistryButton = findViewById(R.id.chemistryButton);
        moreButton = findViewById(R.id.moreButton);
        searchButton = findViewById(R.id.searchButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        optionsButton = findViewById(R.id.optionsButton);
        constantsButton = findViewById(R.id.constantsButton);
        unitsButton = findViewById(R.id.unitsButton);
        privacyPolicyButton = findViewById(R.id.privacyPolicyButton);

        mathsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, CategoryChooserActivity.class);
                Data.transfer = Data.mathsCategories;
                Data.currentField = "maths";
                startActivity(intent);


            }
        });

        physicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, CategoryChooserActivity.class);
                Data.transfer = Data.physicsCategories;
                Data.currentField = "physics";
                startActivity(intent);


            }
        });

        chemistryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, CategoryChooserActivity.class);
                Data.transfer = Data.chemistryCategories;
                Data.currentField = "chemistry";
                startActivity(intent);


            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, DeluxeAdActivity.class);
                startActivity(intent);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, FavoriteActivity.class);
                startActivity(intent);

            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, OptionsActivity.class);
                startActivity(intent);

            }
        });
        constantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, DeluxeAdActivity.class);
                startActivity(intent);

            }
        });
        unitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, DeluxeAdActivity.class);
                startActivity(intent);

            }
        });
        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sites.google.com/site/formelbreakerprivacypolicy/"));
                if (browserIntent.resolveActivity(StartActivity.this.getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
            }
        });
    }
}
