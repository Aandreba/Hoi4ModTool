package org.hoi.system.hoi;

import java.util.stream.IntStream;

public class HoiTag implements CharSequence {
    final private String value;

    public HoiTag (String value) {
        this.value = value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public IntStream chars() {
        return value.chars();
    }

    @Override
    public IntStream codePoints() {
        return value.codePoints();
    }

    @Override
    public String toString() {
        return value;
    }
}
