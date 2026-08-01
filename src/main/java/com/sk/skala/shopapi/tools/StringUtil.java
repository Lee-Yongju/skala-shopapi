package com.sk.skala.shopapi.tools;

public class StringUtil {

    public static boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isAnyEmpty(String... values) {
        for (String value : values) {
            if (isEmpty(value)) {
                return true;
            }
        }
        return false;
    }
}
