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

public class Unit {

    // Attribute aller Units - Entsprechende Tags mit Werten finden sich im XML-Dokument
    String identifier;

    ArrayList<String> singleUnits;
    ArrayList<Double> values;

    /*
       Konstruktor
       Wird aus dem XMLParser (Unit readUnit(...)) heraus aufgerufen wenn beim Start der App zu jeder
       Unit im XML-Dokument ein entsprechendes Objekt erstellt wird
    */
    Unit(String identifier, String unitName, String baseUnitName, ArrayList<String> singleUnits, ArrayList<Double> values) {

        this.identifier = identifier;

        this.singleUnits = singleUnits;
        this.values = values;

        /*System.out.println("-------");
        for (String unit : singleUnits) {
            System.out.printf("SingleUnit aus %s: %s \n", identifier, unit);
        }
        System.out.println("-------\n");*/

    }


}
