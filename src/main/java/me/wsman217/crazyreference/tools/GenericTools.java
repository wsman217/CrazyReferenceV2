package me.wsman217.crazyreference.tools;

public class GenericTools {

    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
