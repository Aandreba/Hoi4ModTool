package org.hoi.various.collection.concat;

import org.hoi.various.collection.MappedList;

import java.util.*;
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

    // STATIC
    public static <A extends Collection<B>, B extends List<T>, T> ConcatList<T> of (A values) {
        Iterator<B> iter = values.iterator();
        ConcatList<T> list = new ConcatList<T>(iter.next(), iter.next());

        while (iter.hasNext()) {
            list = new ConcatList<>(list, iter.next());
        }

        return list;
    }
}
