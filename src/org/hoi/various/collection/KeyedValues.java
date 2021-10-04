package org.hoi.various.collection;

import java.util.*;

public abstract class KeyedValues<K,T> extends AbstractMap<K,T> {
    final private Collection<T> list;

    public KeyedValues (Collection<T> list) {
        this.list = list;
    }

    public KeyedValues (T... array) {
        this(new WrappedArray<>(array));
    }

    protected abstract K obtainKey(T value);

    @Override
    public Set<Entry<K, T>> entrySet() {
        return new KeyedValuesSet();
    }

    private class KeyedValuesSet extends AbstractSet<Entry<K,T>> {
        @Override
        public Iterator<Entry<K, T>> iterator() {
            Iterator<T> iter = list.iterator();

            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Entry<K, T> next() {
                    return new Entry<K, T>() {
                        T value = iter.next();
                        K key = KeyedValues.this.obtainKey(value);

                        @Override
                        public K getKey() {
                            return key;
                        }

                        @Override
                        public T getValue() {
                            return value;
                        }

                        @Override
                        public T setValue(T value) {
                            return null;
                        }
                    };
                }
            };
        }

        @Override
        public int size() {
            return list.size();
        }
    }
}
