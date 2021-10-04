package org.hoi.various.map;

import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface LazyMap<K,V> extends Iterable<Map.Entry<K,V>> {
    int size ();
    boolean isEmpty();

    int keyCount(K key);
    int valueCount(V value);

    List<V> get (K key);
    V getFirst (K key);

    boolean add (K key, V value);
    List<V> set (K key, V value, Predicate<V> where);
    V setFirst (K key, V value, Predicate<V> where);

    boolean remove (K key, Predicate<V> where);
    V removeFirst (K key, Predicate<V> where);

    void clear ();
    Collection<V> values ();
    Collection<K> keys ();
    Collection<Map.Entry<K,V>> entries ();

    // DEFAULTABLE
    default boolean containsKey (K key) {
        return keyCount(key) > 0;
    }

    default boolean containsValue (V value) {
        return valueCount(value) > 0;
    }

    default List<V> set (K key, V value) {
        return set(key, value, x -> true);
    }

    default V setFirst (K key, V value) {
        return setFirst(key, value, x -> true);
    }

    default boolean remove (K key) {
        return remove(key, x -> true);
    }

    default V removeFirst (K key) {
        return removeFirst(key, x -> true);
    }

    default void forEach (BiConsumer<K,V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : entries()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }

            action.accept(k, v);
        }
    }

    default Spliterator<Map.Entry<K,V>> spliterator() {
        return Spliterators.spliterator(entries(), 0);
    }

    default Stream<Map.Entry<K,V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<Map.Entry<K,V>> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
