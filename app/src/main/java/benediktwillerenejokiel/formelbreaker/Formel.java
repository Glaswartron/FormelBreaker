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

public class Formel {


    // Attribute aller Formeln - Entsprechende Tags mit Werten finden sich im XML-Dokument
    String category;
    String buttonText;
    String preview;
    ArrayList<String> umformungen;
    ArrayList<String> latexStrings;
    ArrayList<Variable> variablen = new ArrayList<>();
    ArrayList<String> variablePreviews = new ArrayList<>();
    ArrayList<Unit> units = new ArrayList<>();

    /* Ein eindeutiger Schlüssel mit dem die Formel als Favorit gespeichert wird
       Besteht aus Category-Identifier und deutschem Namen der Formel */
    String favoriteIdentifier;

    /*
       Konstruktor
       Wird aus dem XMLParser (Formel readFormel(...)) heraus aufgerufen wenn zu jeder
       Formel im XML-Dokument ein entsprechendes Objekt erstellt wird
    */
    public Formel(String cat, String text, String textDE, String preview, ArrayList<String> umformungen,
                  ArrayList<String> latexStrings, ArrayList<String> vars) {

        this.category = cat;
        this.buttonText = text;
        this.preview = preview;
        this.umformungen = umformungen;
        this.latexStrings = latexStrings;

        for (String varIdentifier : vars) {
            for (Variable var : Data.variablen) {
                if(var.identifier.equals(varIdentifier)) {
                    variablen.add(var);
                }
            }
        }

        for (Variable var : variablen) {
            variablePreviews.add(var.completePreview);
            units.add(var.unit);
        }

        favoriteIdentifier = category + textDE;

    }


}
