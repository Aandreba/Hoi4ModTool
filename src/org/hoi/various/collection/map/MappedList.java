package org.hoi.various.collection.map;

import org.hoi.various.collection.WrappedArray;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class MappedList<I,O> extends AbstractList<O> {
    final private List<I> list;

    public MappedList(List<I> list) {
        this.list = list;
    }

    public MappedList(I... array) {
        this(new WrappedArray<>(array));
    }

    protected abstract O map (I input);

    @Override
    public O get (int index) {
        return map(list.get(index));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<O> iterator() {
        Iterator<I> iter = list.iterator();
        return new Iterator<O>() {
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
    public ListIterator<O> listIterator() {
        return new MappedListIterator();
    }

    @Override
    public ListIterator<O> listIterator(int index) {
        return new MappedListIterator(index);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    private class MappedListIterator implements ListIterator<O> {
        ListIterator<I> iter;

        public MappedListIterator () {
            this.iter = MappedList.this.list.listIterator();
        }

        public MappedListIterator (int index) {
            this.iter = MappedList.this.list.listIterator(index);
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public O next() {
            return map(iter.next());
        }

        @Override
        public boolean hasPrevious() {
            return iter.hasPrevious();
        }

        @Override
        public O previous() {
            return map(iter.previous());
        }

        @Override
        public int nextIndex() {
            return iter.nextIndex();
        }

        @Override
        public int previousIndex() {
            return iter.previousIndex();
        }

        @Override
        public void remove() {
            iter.remove();
        }

        @Override
        public void set(O o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(O o) {
            throw new UnsupportedOperationException();
        }
    }
}
