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

public class Variable {

    // Attribute aller Variablen.xml - Entsprechende Tags mit Werten finden sich im XML-Dokument
    String field;
    String identifier;
    String symbol;
    String preview;
    String desc;
    String completePreview;
    Unit unit;

    // Konstantenwert - Wenn keine Konstante: null bzw. 0
    String constantPreview;
    String constantValue;

    /*
       Konstruktor - Wird aus dem XMLParser (Variable readVariable(...)) heraus aufgerufen wenn zu jeder
       Formel im XML-Dokument ein entsprechendes Objekt mit Variable-Objekten als Attributen erstellt wird
    */
    public Variable(String field, String identifier, String symbol, String preview, String desc, Unit unit, String constantPreview, String constantValue) {

        this.field = field;
        this.identifier = identifier;
        this.symbol = symbol;
        this.preview = preview;
        this.desc = desc;
        this.unit = unit;

        if (this.preview == null) {
            this.preview = symbol;
        }

        this.completePreview = this.desc + " " + this.preview;

        this.constantPreview = constantPreview;

        this.constantValue = constantValue;

    }

}
