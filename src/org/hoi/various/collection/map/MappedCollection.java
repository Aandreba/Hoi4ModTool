package org.hoi.various.collection.map;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public abstract class MappedCollection<I,O> extends AbstractCollection<O> {
    final private Collection<I> collection;

    public MappedCollection (Collection<I> collection) {
        this.collection = collection;
    }

    protected abstract O map (I value);

    @Override
    public Iterator<O> iterator() {
        return new Iterator<O>() {
            Iterator<I> iter = collection.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public O next() {
                return map(iter.next());
            }
        };
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public void clear() {
        collection.clear();
    }
}
