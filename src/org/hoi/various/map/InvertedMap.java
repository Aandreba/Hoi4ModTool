package org.hoi.various.map;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class InvertedMap<K,V> extends AbstractLazyMap<K,V> {
    final private Map<V,K> map;

    public InvertedMap (Map<V, K> map) {
        this.map = map;
    }

    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return new AbstractCollection<Map.Entry<K, V>>() {
            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() {
                    Iterator<Map.Entry<V,K>> iter = map.entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        return new Map.Entry<K, V>() {
                            Map.Entry<V,K> next = iter.next();

                            @Override
                            public K getKey() {
                                return next.getValue();
                            }

                            @Override
                            public V getValue() {
                                return next.getKey();
                            }

                            @Override
                            public V setValue(V value) {
                                return next.getKey();
                            }
                        };
                    }
                };
            }

            @Override
            public int size() {
                return map.size();
            }
        };
    }
}
