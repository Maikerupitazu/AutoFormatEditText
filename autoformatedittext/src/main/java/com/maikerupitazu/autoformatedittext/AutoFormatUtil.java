package com.maikerupitazu.autoformatedittext;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by aldo on 21/08/16.
 */
public class AutoFormatUtil {

    private static final String FORMAT_NO_DECIMAL = "###,###";

    private static final String FORMAT_WITH_DECIMAL = "###,###.###";

    public static int getCharOccurance(String input, char c) {
        int occurance = 0;
        char[] chars = input.toCharArray();
        for (char thisChar : chars) {
            if (thisChar == c) {
                occurance++;
            }
        }
        return occurance;
    }

    public static String extractDigits(String input) {
        return input.replaceAll("\\D+", "");
    }

    public static String formatToStringWithoutDecimal(double value, Locale locale) {
        NumberFormat formatter = new DecimalFormat(FORMAT_NO_DECIMAL, createFormatSymbols(locale));
        return formatter.format(value);
    }

    public static String formatToStringWithoutDecimal(String value, Locale locale) {
        return formatToStringWithoutDecimal(Double.parseDouble(value), locale);
    }

    public static String formatWithDecimal(String price, Locale locale) {
        return formatWithDecimal(Double.parseDouble(price), locale);
    }

    public static String formatWithDecimal(double price, Locale locale) {
        NumberFormat formatter = new DecimalFormat(FORMAT_WITH_DECIMAL, createFormatSymbols(locale));
        return formatter.format(price);
    }

    private static DecimalFormatSymbols createFormatSymbols(Locale locale) {
        return new DecimalFormatSymbols(locale);
    }

    public static char getGroupingSeparator(Locale locale) {
        return DecimalFormatSymbols.getInstance(locale).getGroupingSeparator();
    }

    public static char getDecimalSeparator(Locale locale) {
        return DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();
    }

    public static String getGroupingSeparatorString(Locale locale) {
        return String.valueOf(getGroupingSeparator(locale));
    }

    public static String getDecimalSeparatorString(Locale locale) {
        return String.valueOf(getDecimalSeparator(locale));
    }
}