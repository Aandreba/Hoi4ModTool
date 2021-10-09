package org.hoi.various;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {
    public static String toUpperCaseFirst (String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        return new String(chars);
    }

    public static String toUpperCaseWords (String str, String split, String join) {
        return Arrays.stream(str.split(split)).map(StringUtils::toUpperCaseFirst).collect(Collectors.joining(join));
    }

    public static String toUpperCaseWords (String str, String split) {
        return toUpperCaseWords(str, split, split);
    }

    public static String toUpperCaseWords (String str) {
        return toUpperCaseWords(str, " ");
    }
}
