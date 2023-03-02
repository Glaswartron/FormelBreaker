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

import java.util.Locale;

public class Options {

    // Derzeit ausgewählte Sprache
    public static String lang = "DE";

    // Derzeit ausgewählte Rundungsgenauigkeit
    public static int roundScale = 6;

    // Ortsfaktor als Konstante ?
    public static boolean gconstant = false;

    /*
       Methode zum Erkennen der System-Standardsprache beim Start der App
       Unterstützt werden Deutsch (de) und Englisch (en), für alle anderen Sprachen wird standardmäßig Englisch verwendet
       Wird beim Start der App aus der StartActivity heraus aufgerufen
    */
    public static void setLanguageFromLocale() {

        String currentLang = Locale.getDefault().getLanguage();

        switch (currentLang) {
            case "de":
                lang = "DE";
                break;
            case "en":
                lang = "EN";
                break;
            default:
                lang = "EN";
        }

    }

    /*
       Methode zum Ändern der Konstantheit von g
       Wird beim Start der FormelActivities aus der entsprechenden FormelActivity heraus aufgerufen
    */
    public static void checkgConstant(Formel formel) {

        if(gconstant) {
            if(formel.variablen.contains(Data.gvariable)) {
                int indexOfVar = formel.variablen.indexOf(Data.gvariable);
                formel.variablen.set(indexOfVar, Data.gconstant);
                formel.umformungen.remove(indexOfVar);
                formel.latexStrings.remove(indexOfVar);
            }
        } else {
            for (Variable var : formel.variablen) {
                if(var == Data.gconstant) {
                    formel.variablen.set(formel.variablen.indexOf(var), Data.gvariable);
                    break;
                }
            }
        }

    }

}
