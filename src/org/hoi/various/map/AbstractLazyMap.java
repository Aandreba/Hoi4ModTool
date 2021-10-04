package org.hoi.various.map;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractLazyMap<K,V> implements LazyMap<K,V> {
    public abstract Collection<Map.Entry<K,V>> entries ();
    public abstract int size ();

    @Override
    public boolean isEmpty() {
        return entries().isEmpty();
    }

    @Override
    public int keyCount(K key) {
        return (int) entries().stream().filter(x -> x.getKey().equals(key)).count();
    }

    @Override
    public int valueCount(V value) {
        return (int) entries().stream().filter(x -> x.getValue().equals(value)).count();
    }

    @Override
    public boolean containsKey(K key) {
        return entries().stream().anyMatch(x -> x.getKey().equals(key));
    }

    @Override
    public boolean containsValue(V value) {
        return entries().stream().anyMatch(x -> x.getValue().equals(value));
    }

    @Override
    public List<V> get (K key) {
        return entries().stream().filter(x -> x.getKey().equals(key)).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public V getFirst (K key) {
        return entries().stream().filter(x -> x.getKey().equals(key)).findFirst().map(Map.Entry::getValue).orElse(null);
    }

    @Override
    public boolean add (K key, V value) {
        return entries().add(new Map.Entry<K, V>() {
            K k = key;
            V v = value;

            @Override
            public K getKey() {
                return k;
            }

            @Override
            public V getValue() {
                return v;
            }

            @Override
            public V setValue(V value) {
                V last = v;
                v = value;

                return last;
            }
        });
    }

    @Override
    public List<V> set (K key, V value, Predicate<V> where) {
        ArrayList<V> past = new ArrayList<>();
        entries().stream().filter(x -> x.getKey().equals(key) && where.test(x.getValue())).forEach(x -> {
            past.add(x.getValue());
            x.setValue(value);
        });

        return past;
    }

    @Override
    public V setFirst (K key, V value, Predicate<V> where) {
        return entries().stream().filter(x -> x.getKey().equals(key) && where.test(x.getValue())).findFirst().map(kvEntry -> kvEntry.setValue(value)).orElse(null);
    }

    @Override
    public boolean remove (K key, Predicate<V> where) {
        return entries().removeIf(x -> x.getKey().equals(key) && where.test(x.getValue()));
    }

    @Override
    public V removeFirst (K key, Predicate<V> where) {
        Iterator<Map.Entry<K,V>> it = entries().iterator();

        while (it.hasNext()) {
            Map.Entry<K,V> next = it.next();
            if (next.getKey().equals(key) && where.test(next.getValue())) {
                it.remove();
                return next.getValue();
            }
        }

        return null;
    }

    @Override
    public void clear () {
        entries().clear();
    }

    @Override
    public Collection<K> keys() {
        return entries().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    public Collection<V> values() {
        return entries().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return entries().iterator();
    }

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return entries().spliterator();
    }

    @Override
    public Stream<Map.Entry<K, V>> stream() {
        return entries().stream();
    }

    @Override
    public Stream<Map.Entry<K, V>> parallelStream() {
        return entries().parallelStream();
    }
}
