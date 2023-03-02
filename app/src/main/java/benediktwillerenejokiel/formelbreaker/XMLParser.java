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

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLParser {

    private static final String ns = null;

    public static ArrayList<Formel> parseFormeln(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFormelData(parser);
        } finally {
            in.close();
        }

    }

    public static ArrayList<Variable> parseVariablen(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readVariablenData(parser);
        } finally {
            in.close();
        }
    }

    public static ArrayList<Unit> parseUnits(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readUnitData(parser);
        } finally {
            in.close();
        }

    }

    public static ArrayList<Category> parseCategories(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readCategoryData(parser);
        } finally {
            in.close();
        }

    }

    public static ArrayList<String> parseFavorites(InputStream in) throws XmlPullParserException, IOException{
        ArrayList<String> results = new ArrayList<>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, ns, "Favorites");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("Favorite"))
                    results.add(readTag(parser, name));
            }
            return results;
        } finally {
            in.close();
        }
    }

    public static ArrayList<String> parseOptions(InputStream in) throws XmlPullParserException, IOException {
        ArrayList<String> options = new ArrayList<>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, ns, "Options");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();

                if(name.equals("Lang") || name.equals("RoundScale") || name.equals("gconstant"))
                    options.add(readTag(parser, name));
            }
            return options;
        } finally {
            in.close();
        }
    }

    private static ArrayList<Formel> readFormelData(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Formel> formeln = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "Formeln");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Formel"))
                formeln.add(readFormel(parser));
        }
        return formeln;

    }

    private static Formel readFormel(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Formel");
        String category = "";
        String buttonText = "";

        String previewDE = null;
        String previewEN = null;

        ArrayList<String> umformungen = new ArrayList<>();

        ArrayList<String> latexStringsDE = new ArrayList<>();
        ArrayList<String> latexStringsEN = new ArrayList<>();

        ArrayList<String> variablen = new ArrayList<>();

        String favoriteIdentifier = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Category"))
                category = readTag(parser, name);
            else if (name.equals("ButtonText") && parser.getAttributeValue(ns, "lang").equals("DE")) {
                String temp = readTag(parser, name);
                favoriteIdentifier = temp;
                if (Options.lang.equals("DE"))
                    buttonText = temp;
            }
            else if (name.equals("ButtonText") && parser.getAttributeValue(ns, "lang").equals("ENG")
                    && Options.lang.equals("EN")) {
                buttonText = readTag(parser, name);
            }
            else if (name.equals("Preview") && parser.getAttributeValue(ns, "lang").equals("DE")) {
                previewDE = readTag(parser, name);
            }
            else if (name.equals("Preview") && parser.getAttributeValue(ns, "lang").equals("ENG")) {
                previewEN = readTag(parser, name);
            }
            else if (name.equals("Umformung"))
                umformungen.add(readTag(parser, name));
            else if (name.equals("LaTeX") && parser.getAttributeValue(ns, "lang").equals("DE")) {
                latexStringsDE.add(readTag(parser, name));
            }
            else if (name.equals("LaTeX") && parser.getAttributeValue(ns, "lang").equals("ENG")) {
                latexStringsEN.add(readTag(parser, name));
            }
            else if (name.equals("Variable")) {
                variablen.add(readTag(parser, name));
            } else {
                skip(parser);
            }
        }

        ArrayList<String> latexStrings = Options.lang.equals("DE") ? latexStringsDE : latexStringsEN;
        if (Options.lang.equals("EN") && latexStringsEN.isEmpty()) {
            latexStrings = latexStringsDE;
        }

        String preview = Options.lang.equals("DE") ? previewDE : previewEN;
        if (Options.lang.equals("EN") && preview == null) {
            preview = previewDE;
        }

        //System.out.println("bw: Formel geparsed -- " + buttonText);
        return new Formel(category, buttonText, favoriteIdentifier, preview, umformungen, latexStrings, variablen);
    }

    private static String readTag(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, name);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, name);
        return value;
    }

    private static ArrayList<Variable> readVariablenData(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Variable> variablen = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "Variablen");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Variable") && parser.getAttributeCount() == 0)
                variablen.add(readVariable(parser, false));
            else if (name.equals("Variable") && parser.getAttributeCount() == 2) {
                variablen.add(readVariable(parser, true));
            }
        }
        return variablen;
    }

    private static Variable readVariable(XmlPullParser parser, boolean isConstant) throws IOException, XmlPullParserException {

        String field = "";
        String identifier = "";
        String symbol = "";

        String previewDE = null;
        String previewENG = null;

        String desc = "";
        Unit unit = null;
        String constantPreview = null;
        String constantValue = null;

        if(isConstant) {
            constantValue = parser.getAttributeValue(ns, "const");
            constantPreview = parser.getAttributeValue(ns, "constPreviewDE");
        }

        parser.require(XmlPullParser.START_TAG, ns, "Variable");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("Field"))
                field = readTag(parser, name);
            else if (name.equals("Identifier"))
                identifier = readTag(parser, name);
            else if (name.equals("Symbol"))
                symbol = readTag(parser, name);
            else if (name.equals("Preview") && parser.getAttributeValue(ns, "lang").equals("DE"))
                previewDE = readTag(parser, name);
            else if (name.equals("Preview") && parser.getAttributeValue(ns, "lang").equals("ENG"))
                previewENG = readTag(parser, name);
            else if (name.equals("Description") && parser.getAttributeValue(ns, "lang").equals("DE")
                    && Options.lang.equals("DE")) {
                desc = readTag(parser, name);
            }
            else if (name.equals("Description") && parser.getAttributeValue(ns, "lang").equals("ENG")
                    && Options.lang.equals("EN")) {
                desc = readTag(parser, name);
            }
            else if(name.equals("Unit")) {
                String unitIdentifier = readTag(parser, name);
                for (Unit storedUnit : Data.units) {
                    if(storedUnit.identifier.equals(unitIdentifier))
                        unit = storedUnit;
                }
            } else {
                skip(parser);
            }

        }

        String preview = null;
        if (Options.lang.equals("DE") && previewDE != null)
            preview = previewDE;
        else if (Options.lang.equals("EN") && previewENG != null)
            preview = previewENG;
        else if (Options.lang.equals("EN") && previewENG == null && previewDE != null)
            preview = previewDE;

        //System.out.println("bw: Variable geparsed -- " + desc);
        return new Variable(field, identifier, symbol, preview, desc, unit, constantPreview, constantValue);
    }

    private static ArrayList<Category> readCategoryData(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Category> categories = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "Categories");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Category"))
                categories.add(readCategory(parser));
        }
        return categories;

    }

    private static Category readCategory(XmlPullParser parser) throws XmlPullParserException, IOException {
        String identifier = null;
        String buttonText = null;
        parser.require(XmlPullParser.START_TAG, ns, "Category");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("Identifier"))
                identifier = readTag(parser, name);
            else if (name.equals("ButtonText") && parser.getAttributeValue(ns, "lang").equals("DE")
                    && Options.lang.equals("DE")) {
                buttonText = readTag(parser, name);
            }
            else if (name.equals("ButtonText") && parser.getAttributeValue(ns, "lang").equals("ENG")
                    && Options.lang.equals("EN")) {
                buttonText = readTag(parser, name);
            } else {
                skip(parser);
            }

        }

        //System.out.println("bw: Category geparsed -- " + buttonText);
        return new Category(identifier, buttonText);

    }

    private static ArrayList<Unit> readUnitData(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Unit> units = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "Units");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Unit"))
                units.add(readUnit(parser));
        }
        return units;

    }

    private static Unit readUnit(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Unit");
        String identifier = null;
        String unitName = null;
        String baseUnitName = null;
        ArrayList<String> singleUnits = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("Identifier")) {
                identifier = readTag(parser, name);
            } else if (name.equals("UnitName") && parser.getAttributeValue(ns, "lang").equals("DE")
                    && Options.lang.equals("DE")) {
                unitName = readTag(parser, name);
            } else if (name.equals("UnitName") && parser.getAttributeValue(ns, "lang").equals("ENG")
                    && Options.lang.equals("EN")) {
                unitName = readTag(parser, name);
            } else if (name.equals("BaseUnitName") && parser.getAttributeValue(ns, "lang").equals("DE")
                    && Options.lang.equals("DE")) {
                baseUnitName = readTag(parser, name);
            } else if (name.equals("BaseUnitName") && parser.getAttributeValue(ns, "lang").equals("ENG")
                    && Options.lang.equals("EN")) {
                baseUnitName = readTag(parser, name);
            } else if (name.equals("SingleUnit")) {
                singleUnits.add(readTag(parser, name));
            } else if (name.equals("Value")) {
                values.add(Double.valueOf(readTag(parser, name)));
            } else {
                skip(parser);
            }
        }

        //System.out.println("bw: Unit geparsed -- " + identifier);
        return new Unit(identifier, unitName, baseUnitName, singleUnits, values);

    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
