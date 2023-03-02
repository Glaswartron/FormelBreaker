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

public class SearchUtil {

    public static ArrayList<Category> searchCategory(String query, ArrayList<Category> categories) {

        ArrayList<Category> results = new ArrayList<>();

        for (Category cat : categories) {

            if(cat.buttonText.toLowerCase().contains(query.toLowerCase())) {
                    results.add(cat);
            }

        }

        return results;

    }

    public static ArrayList<Formel> searchByFormelName(String query, ArrayList<Category> categories) {

        ArrayList<Formel> results = new ArrayList<>();

        for (Category cat : categories) {

            for (Formel f : cat.formeln) {

                if(f.buttonText.toLowerCase().contains(query.toLowerCase())) {
                    results.add(f);
                }

            }
        }

        return results;

    }

    public static ArrayList<Formel> searchByVariableName(String query, ArrayList<Category> categories) {

        ArrayList<Formel> results = new ArrayList<>();

        for (Category cat : categories) {

            for (Formel f : cat.formeln) {

                for (Variable v : f.variablen) {

                    if(v.desc.toLowerCase().contains(query.toLowerCase())) {
                        results.add(f);
                        break;
                    }

                }
            }
        }

        return results;

    }

    public static ArrayList<Formel> searchByVariableSymbol(String query, ArrayList<Category> categories) {

        ArrayList<Formel> results = new ArrayList<>();

        for (Category cat : categories) {

            for (Formel f : cat.formeln) {

                for (Variable v : f.variablen) {

                    if(v.preview.contains(query)) {
                        results.add(f);
                        break;
                    }

                }
            }
        }

        return results;

    }




}
