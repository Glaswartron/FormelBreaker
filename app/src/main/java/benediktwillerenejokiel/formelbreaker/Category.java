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

public class Category {

    // Attribute aller Categories - Entsprechende Tags mit Werten finden sich im XML-Dokument
    String identifier;
    String buttonText;

    // Liste mit allen Formeln dieser Category - wird aus Data.populateCaegories(...) heraus befüllt
    ArrayList<Formel> formeln = new ArrayList<>();


    /*
       Konstruktor - Wird aus dem XMLParser (Category readCategory(...)) heraus aufgerufen wenn zu jeder
       Category im XML-Dokument ein entsprechendes Objekt erstellt wird
    */
    public Category(String identifier, String buttonText) {

        this.identifier = identifier;
        this.buttonText = buttonText;

    }


}
