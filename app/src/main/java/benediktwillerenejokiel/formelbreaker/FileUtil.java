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
import android.content.res.AssetManager;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class FileUtil {

    // Methode zum Einlesen aller vorhandenen XML-Daten beim Start der App
    public static void parseXMLData(Context context) {

        AssetManager aman = context.getAssets();

        try {

            /* FileUtil-Methode zum Einlesen der gespeicherten Optionen
               aus dem internen Speicher wird aufgerufen
               Muss als erstes aufgerufen werden, damit die Sprachoption
               korrekt gesetzt ist
            */
            FileUtil.loadOptions(context);

            // Categories werden aus dem XML-Dokument eingelesen und in Data gespeichert
            Data.mathsCategories = XMLParser.parseCategories(aman.open("MathsCategories.xml"));
            Data.physicsCategories = XMLParser.parseCategories(aman.open("PhysicsCategories.xml"));
            Data.chemistryCategories = XMLParser.parseCategories(aman.open("ChemistryCategories.xml"));

            // Units.xml werden aus dem XML-Dokument eingelesen und in Data gespeichert
            Data.units = XMLParser.parseUnits(aman.open("Units.xml"));

            // Variablen.xml werden aus dem XML-Dokument eingelesen und in Data gespeichert + Konstanten werden seperat gespeichert
            Data.variablen = XMLParser.parseVariablen(aman.open("Variablen.xml"));
            Data.storeConstants();
            // Aufruf der Methode Data.savegStates() zum seperaten Speichern des variablen und konstanten g
            Data.savegStates();

            /*
               Formeln werden aus dem XML-Dokument eingelesen und Data.populateCategories(...) als Parameter übergeben
               Ebenfalls übergeben wird eine Referenz auf die Category-Liste des entsprechenden Fields
            */
            Data.populateCategories(XMLParser.parseFormeln(aman.open("MathsFormeln.xml")), Data.mathsCategories);
            Data.populateCategories(XMLParser.parseFormeln(aman.open("PhysicsFormeln.xml")), Data.physicsCategories);
            Data.populateCategories(XMLParser.parseFormeln(aman.open("ChemistryFormeln.xml")), Data.chemistryCategories);

        } catch (Exception e) {e.printStackTrace();}

    }

    public static void saveFavorite(Formel formel, Context context) throws IOException, XmlPullParserException {

        File favoritesDataFile;
        favoritesDataFile = new File(context.getFilesDir() + File.separator + "favorites.xml");

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        serializer.setOutput(stringWriter);

        ArrayList<String> contentTempStorage = null;

        if (favoritesDataFile.exists()) {
            FileInputStream inputStream = new FileInputStream(favoritesDataFile);
            contentTempStorage = XMLParser.parseFavorites(inputStream);
            favoritesDataFile.delete();
        }

        favoritesDataFile.createNewFile();

        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "Favorites");

        if(contentTempStorage != null) {
            for (String identifier : contentTempStorage) {
                serializer.startTag("", "Favorite");
                serializer.text(identifier);
                serializer.endTag("", "Favorite");
            }
        }

        serializer.startTag("", "Favorite");
        serializer.text(formel.favoriteIdentifier);
        serializer.endTag("", "Favorite");

        serializer.endTag("", "Favorites");
        serializer.endDocument();

        FileWriter writer = new FileWriter(favoritesDataFile);
        writer.write(stringWriter.toString());
        writer.close();
        stringWriter.close();

        Data.favorites.add(formel);

    }

    public static void deleteFavorite(Formel formel, Context context) throws IOException, XmlPullParserException {

        File favoritesDataFile;
        favoritesDataFile = new File(context.getFilesDir() + File.separator + "favorites.xml");

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        serializer.setOutput(stringWriter);

        ArrayList<String> contentTempStorage;

        FileInputStream inputStream = new FileInputStream(favoritesDataFile);
        contentTempStorage = XMLParser.parseFavorites(inputStream);
        favoritesDataFile.delete();

        favoritesDataFile.createNewFile();

        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "Favorites");

        contentTempStorage.remove(formel.favoriteIdentifier);

        for (String identifier : contentTempStorage) {
            serializer.startTag("", "Favorite");
            serializer.text(identifier);
            serializer.endTag("", "Favorite");
        }

        serializer.endTag("", "Favorites");
        serializer.endDocument();

        FileWriter writer = new FileWriter(favoritesDataFile);
        writer.write(stringWriter.toString());
        stringWriter.close();
        writer.close();

        Data.removeFavorite(formel);

    }

    public static void loadFavorites(Context context) throws XmlPullParserException, IOException {

        Data.favorites = new ArrayList<>();

        File favoritesDataFile;
        favoritesDataFile = new File(context.getFilesDir() + File.separator + "favorites.xml");

        if (!favoritesDataFile.exists()) {
            return;
        }

        FileInputStream inputStream = new FileInputStream(favoritesDataFile);
        ArrayList<String> identifiers;

        identifiers = XMLParser.parseFavorites(inputStream);

        identifierLoopMaths: for (String identifier : identifiers) {
            for (Category cat : Data.mathsCategories) {
                for (Formel formel : cat.formeln) {
                    if ((formel.favoriteIdentifier).equals(identifier)) {
                        Data.favorites.add(formel);
                        continue identifierLoopMaths;
                    }
                }
            }
         identifierLoopPhysics: for (Category cat : Data.physicsCategories) {
                for (Formel formel : cat.formeln) {
                    if ((formel.favoriteIdentifier).equals(identifier)) {
                        Data.favorites.add(formel);
                        continue identifierLoopPhysics;
                    }
                }
            }
         identifierLoopChemistry: for (Category cat : Data.chemistryCategories) {
                for (Formel formel : cat.formeln) {
                    if ((formel.favoriteIdentifier).equals(identifier)) {
                        Data.favorites.add(formel);
                        continue identifierLoopChemistry;
                    }
                }
            }

        }
    }

    public static void loadOptions(Context context) throws IOException, XmlPullParserException {

        File optionsDataFile;
        optionsDataFile = new File(context.getFilesDir() + File.separator + "options.xml");

        if(!optionsDataFile.exists()) {

            XmlSerializer serializer = Xml.newSerializer();
            StringWriter stringWriter = new StringWriter();
            serializer.setOutput(stringWriter);

            optionsDataFile.createNewFile();

            serializer.startDocument("UTF-8", true);
            serializer.startTag(null, "Options");
            serializer.startTag(null, "Lang");
            serializer.text("DE");
            serializer.endTag(null, "Lang");
            serializer.startTag(null, "RoundScale");
            serializer.text("6");
            serializer.endTag(null, "RoundScale");
            serializer.startTag(null, "gconstant");
            serializer.text("false");
            serializer.endTag(null, "gconstant");
            serializer.endTag(null, "Options");
            serializer.endDocument();

            FileWriter writer = new FileWriter(optionsDataFile);
            writer.write(stringWriter.toString());
            stringWriter.close();
            writer.close();

            return;

        }

        ArrayList<String> savedOptions;
        savedOptions = XMLParser.parseOptions(new FileInputStream(optionsDataFile));

        if (!Options.lang.equals(savedOptions.get(0)))
            Options.lang = savedOptions.get(0);

        Options.roundScale = Integer.valueOf(savedOptions.get(1));
        Options.gconstant = Boolean.valueOf(savedOptions.get(2));

    }

    public static void editOptions(Context context, int option, String newValue) throws IOException, XmlPullParserException {

        File optionsDataFile;
        optionsDataFile = new File(context.getFilesDir() + File.separator + "options.xml");

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        serializer.setOutput(stringWriter);

        ArrayList<String> savedOptions;
        FileInputStream inputStream = new FileInputStream(optionsDataFile);
        savedOptions = XMLParser.parseOptions(inputStream);
        optionsDataFile.delete();

        savedOptions.set(option, newValue);

        optionsDataFile.createNewFile();

        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "Options");
        serializer.startTag(null, "Lang");
        serializer.text(savedOptions.get(0));
        serializer.endTag(null, "Lang");
        serializer.startTag(null, "RoundScale");
        serializer.text(savedOptions.get(1));
        serializer.endTag(null, "RoundScale");
        serializer.startTag(null, "gconstant");
        serializer.text(savedOptions.get(2));
        serializer.endTag(null, "gconstant");
        serializer.endTag(null, "Options");
        serializer.endDocument();

        FileWriter writer = new FileWriter(optionsDataFile);
        writer.write(stringWriter.toString());

        stringWriter.close();
        writer.close();

        Options.lang = savedOptions.get(0);
        Options.roundScale = Integer.valueOf(savedOptions.get(1));
        Options.gconstant = Boolean.valueOf(savedOptions.get(2));

        if (option == 2 && !Options.gconstant)
            reparsePhysics(context);

        if (option == 0)
            reparseAll(context);

    }

    private static void reparsePhysics(Context context) {

        AssetManager aman = context.getAssets();
        try {
            Data.populateCategories(XMLParser.parseFormeln(aman.open("PhysicsFormeln.xml")), Data.physicsCategories);
        } catch (Exception e) {e.printStackTrace();}

    }

    private static void reparseAll(Context context) {

        // Vergleiche void parseXMLData() in FileUtil
        AssetManager aman = context.getAssets();
        try {
            Data.mathsCategories = XMLParser.parseCategories(aman.open("MathsCategories.xml"));
            Data.physicsCategories = XMLParser.parseCategories(aman.open("PhysicsCategories.xml"));
            Data.chemistryCategories = XMLParser.parseCategories(aman.open("ChemistryCategories.xml"));
            Data.units = XMLParser.parseUnits(aman.open("Units.xml"));
            Data.variablen = XMLParser.parseVariablen(aman.open("Variablen.xml"));
            Data.storeConstants();
            Data.savegStates();
            Data.populateCategories(XMLParser.parseFormeln(aman.open("MathsFormeln.xml")), Data.mathsCategories);
            Data.populateCategories(XMLParser.parseFormeln(aman.open("PhysicsFormeln.xml")), Data.physicsCategories);
            Data.populateCategories(XMLParser.parseFormeln(aman.open("ChemistryFormeln.xml")), Data.chemistryCategories);
            FileUtil.loadFavorites(context);
        } catch (Exception e) {e.printStackTrace();}

    }

}