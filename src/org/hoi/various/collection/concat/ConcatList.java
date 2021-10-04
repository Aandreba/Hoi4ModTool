package org.hoi.various.collection.concat;

import org.hoi.various.collection.MappedList;

import java.util.AbstractList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ConcatList<T> extends AbstractList<T> {
    final private List<T> first, last;

    public ConcatList (List<T> first, List<T> last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public T get (int index) {
        int len = first.size();
        return index >= len ? last.get(index - len) : first.get(index);
    }

    @Override
    public int size() {
        return first.size() + last.size();
    }

    @Override
    public T set (int index, T element) {
        int len = first.size();
        if (index >= len) {
            return last.set(index - len, element);
        }

        return first.set(index, element);
    }

    @Override
    public boolean add(T t) {
        return last.add(t);
    }
}
