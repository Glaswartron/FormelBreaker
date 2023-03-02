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

import java.util.ArrayList;
import java.util.Iterator;

public class Data {

    // Listen mit allen Category-Objekten - Die Listen werden beim Start der App aus FileUtil (void parseXMLData()) initialisiert
    public static ArrayList<Category> mathsCategories;
    public static ArrayList<Category> physicsCategories;
    public static ArrayList<Category> chemistryCategories;

    // Liste mit allen Variable-Objekten - Die Liste wird beim Start der App aus FileUtil (void parseXMLData()) befüllt
    public static ArrayList<Variable> variablen = null;

    // Jeweils ein Variablen.xml-Objekt für das variable und das konstante g - ermöglicht das Wechseln zwischen beiden
    public static Variable gvariable = null;
    public static Variable gconstant = null;

    // Liste mit allen Unit-Objekten - Die Liste wird beim Start der App aus FileUtil (void parseXMLData()) befüllt
    public static ArrayList<Unit> units = null;

    /*
        Liste mit allen als Favoriten ausgewählten Formeln - Die Liste wird aus FileUtil heraus befüllt (void saveFavorite(Formel, Context)
        bzw. void loadFavorites(Context))
    */
    public static ArrayList<Formel> favorites = null;

    // Liste mit allen Konstanten - Die Liste wird beim Start der App aus Data.storeConstants() heraus befüllt
    public static ArrayList<Variable> constants = null;

    // Zum Übertragen von Formeln, Categories und Units.xml zwischen Activities
    public static Object transfer = null;

    /*
       Variable zum globalen Speichern des gerade benutzten Fields
    */
    public static String currentField = null;


    /*
       Algorithmus zum Einsortieren von Formeln in die Formel-Listen der einzenlen Category-Objekte
       Aufgerufen wird die Methode aus FileUtil (void parseXMLData()) mit einer Formel-Liste aus dem XML-Parser als Parameter
       Die Methode wird dreimal aufgerufen, für jedes Field bzw. jede Category-Liste einzeln
    */
    public static void populateCategories(ArrayList<Formel> formeln, ArrayList<Category> cats) {

        if(cats.get(0).formeln.size() > 0) {
            for (Category cat : cats) {
                cat.formeln.clear();
            }
        }
        for (Formel f : formeln) {

            for (Category c : cats) {

                if (c.identifier.equals(f.category)) {
                    c.formeln.add(f);
                    break;
                }

            }

        }

    }

    /*
        Methode zum seperaten Speichern des variablen und konstanten g
        Wird beim Start der App aus FileUtil (void parseXMLData()) heraus aufgerufen
    */
    public static void savegStates() {

        for (Variable var : variablen) {
            if (var.identifier.equals("gconstant")) {
                gconstant = var;
            } else if(var.identifier.equals("gvariable")) {
                gvariable = var;
            }
        }

    }

    /*
        Suchalgorithmus zum bestimmen, ob eine Formel als Favorit gespeichert ist
        Wird aus den FormelActivities heraus direkt nach deren Start aufgerufen
    */
    public static boolean isFavorite(Formel formel) {

        for (Formel frml : favorites) {
            if(frml.buttonText.equals(formel.buttonText)) {
                return true;
            }
        }
        return false;
    }

    /*
        Methode zum Löschen eines bestimmten Favoriten aus der favorites-Liste
        Wird aus void FileUtil.deleteFavorite(Formel, ...) heraus aufgerufen
    */
    public static void removeFavorite(Formel formel) {

        for (Iterator<Formel> iterator = favorites.iterator(); iterator.hasNext(); ) {
            if(iterator.next().buttonText.equals(formel.buttonText)) {
                iterator.remove();
                return;
            }
        }

    }

    /*
        Methode zum Befüllen der Konstantenliste beim Start der App
        Wird beim Start der App aus FileUtil (void parseXMLData()) heraus aufgerufen
    */
    public static void storeConstants() {

        Data.constants = new ArrayList<>();

        for (Variable var : variablen) {
            if(var.constantValue != null) {
                constants.add(var);
            }
        }

    }

}
