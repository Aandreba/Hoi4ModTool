package org.hoi.various;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Stringx {
    public static String toUpperCaseFirst (String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        return new String(str);
    }

    public static String toUpperCaseWords (String str) {
        return Arrays.stream(str.split(" ")).map(Stringx::toUpperCaseFirst).collect(Collectors.joining(" "));
    }
}
