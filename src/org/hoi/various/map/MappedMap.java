package org.hoi.various.map;

import java.util.*;

public abstract class MappedMap<A,B,K,V> extends AbstractMap<K,V> {
    final protected Map<A,B> map;

    public MappedMap (Map<A, B> map) {
        this.map = map;
    }

    protected abstract K mapKey (A key);

    protected abstract V mapValue (B value);

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public KeySet keySet() {
        return new KeySet();
    }

    @Override
    public ValueCollection values() {
        return new ValueCollection();
    }

    @Override
    public Set<Entry<K,V>> entrySet() {
        return new EntrySet();
    }

    // STATIC
    public class EntrySet extends AbstractSet<Entry<K,V>> {
        final private Set<Entry<A,B>> set;

        public EntrySet () {
            this.set = MappedMap.this.map.entrySet();
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            Iterator<Entry<A,B>> iter = set.iterator();
            return new Iterator<Entry<K, V>>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Entry<K, V> next() {
                    return new Entry<K, V>() {
                        Entry<A,B> entry = iter.next();

                        @Override
                        public K getKey() {
                            return mapKey(entry.getKey());
                        }

                        @Override
                        public V getValue() {
                            return mapValue(entry.getValue());
                        }

                        @Override
                        public V setValue (V value) {
                            return null;
                        }
                    };
                }
            };
        }

        @Override
        public int size() {
            return set.size();
        }
    }

    public class KeySet extends AbstractSet<K> {
        final private Set<A> set;

        public KeySet () {
            this.set = MappedMap.this.map.keySet();
        }

        @Override
        public Iterator<K> iterator() {
            Iterator<A> iter = set.iterator();
            return new Iterator<K>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public K next() {
                    return mapKey(iter.next());
                }
            };
        }

        @Override
        public int size() {
            return set.size();
        }
    }

    public class ValueCollection extends AbstractCollection<V> {
        final private Collection<B> collection;

        public ValueCollection () {
            this.collection = MappedMap.this.map.values();
        }

        @Override
        public Iterator<V> iterator() {
            Iterator<B> iter = collection.iterator();
            return new Iterator<V>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public V next() {
                    return mapValue(iter.next());
                }
            };
        }

        @Override
        public int size() {
            return collection.size();
        }
    }

    public abstract static class OfKey<A,K,V> extends MappedMap<A,V,K,V> {
        public OfKey (Map<A, V> map) {
            super(map);
        }

        @Override
        protected V mapValue(V value) {
            return value;
        }

        @Override
        public Set<Entry<K,V>> entrySet() {
            return new AbstractSet<Entry<K, V>>() {
                Set<Entry<A,V>> set = OfKey.this.map.entrySet();

                @Override
                public Iterator<Entry<K, V>> iterator() {
                    Iterator<Entry<A,V>> iter = set.iterator();
                    return new Iterator<Entry<K, V>>() {
                        @Override
                        public boolean hasNext() {
                            return iter.hasNext();
                        }

                        @Override
                        public Entry<K, V> next() {
                            return new Entry<K, V>() {
                                Entry<A,V> next = iter.next();

                                @Override
                                public K getKey() {
                                    return mapKey(next.getKey());
                                }

                                @Override
                                public V getValue() {
                                    return next.getValue();
                                }

                                @Override
                                public V setValue(V value) {
                                    return next.setValue(value);
                                }
                            };
                        }
                    };
                }

                @Override
                public int size() {
                    return set.size();
                }
            };
        }
    }

    public abstract static class OfValue<B,K,V> extends MappedMap<K,B,K,V> {
        public OfValue(Map<K, B> map) {
            super(map);
        }

        @Override
        protected K mapKey (K value) {
            return value;
        }
    }
}
