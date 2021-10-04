package org.hoi.various.collection;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.stream.Stream;

public class WrappedArray<T> extends AbstractList<T> {
    final private T[] array;

    public WrappedArray(T... array) {
        this.array = array;
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public T set (int index, T element) {
        T last = array[index];
        array[index] = element;

        return last;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public Spliterator<T> spliterator() {
        return Arrays.spliterator(array);
    }

    @Override
    public Stream<T> stream() {
        return Arrays.stream(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
