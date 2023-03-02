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

import java.math.BigDecimal;

public class MathUtil {

    public static BigDecimal roundBigDecimal(BigDecimal number) {

        if (!number.toPlainString().contains("."))
            return number;

        BigDecimal result;

        result = number.setScale(countDecimalZeros(number) + Options.roundScale, BigDecimal.ROUND_HALF_UP);

        result = result.stripTrailingZeros();

        return result;

    }

    public static String formatNumber(BigDecimal number) {

        String numberEngString = number.toEngineeringString();
        String numberPlainString = number.toPlainString();

        if(!numberEngString.contains("E"))
            return numberEngString;

        if(number.toEngineeringString().contains("E-") && countDecimalZeros(number) <= 5)
            return numberPlainString;

        String mantissa = "";
        String exponent = "";

        if(numberEngString.contains("E+")) {
            mantissa = numberEngString.split("E")[0];
            exponent = numberEngString.split("E")[1].replace("+", "");
            if (Integer.valueOf(exponent) <= 5) {
                return numberPlainString;
            }
        } else if (numberEngString.contains("E-")) {
            mantissa = numberEngString.split("E")[0];
            exponent = numberEngString.split("E")[1];
        }

        mantissa = roundBigDecimal(new BigDecimal(mantissa)).toPlainString();

        return mantissa.concat(" · 10^" + exponent);

    }

    private static int countDecimalZeros(BigDecimal number) {

        String numberString = number.toPlainString();
        if(!numberString.contains("."))
            return 0;
        int zeros = 0;
        char [] decimalDigits = numberString.split("\\.")[1].toCharArray();

        for(char digit : decimalDigits) {

            if(digit != '.' && Character.getNumericValue(digit) == 0)
                zeros++;
            else
                return zeros;

        }

        return 0;

    }

}
