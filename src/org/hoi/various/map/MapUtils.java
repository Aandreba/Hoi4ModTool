package org.hoi.various.map;

import java.util.Map;

public class MapUtils {
    public static <K,V> Map.Entry<K,V> entry (K key, V value) {
        return new Map.Entry<K, V>() {
            K k = key;
            V v = value;

            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return value;
            }

            @Override
            public V setValue (V value) {
                V last = v;
                v = value;

                return last;
            }
        };
    }
}
