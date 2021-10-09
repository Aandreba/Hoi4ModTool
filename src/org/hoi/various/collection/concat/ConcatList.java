package org.hoi.various.collection.concat;

import java.util.*;
import java.util.stream.Collector;

public class ConcatList<T> extends AbstractList<T> {
    final private List<? extends T> first, last;

    public ConcatList() {
        this.first = List.of();
        this.last = List.of();
    }

    public ConcatList (List<? extends T> first, List<? extends T> last) {
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
