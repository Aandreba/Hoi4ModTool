package org.hoi.various;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class IteratorUtils {
    public static <T> Iterator<T> fromArray (T... array) {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public T next() {
                return array[i++];
            }
        };
    }

    public static <T> Iterator<T> fromCollection (Collection<T> collection) {
        return collection.iterator();
    }

    public static Iterator<Character> fromReader (Reader reader) {
        return new Iterator<Character>() {
            Integer next = null;

            @Override
            public boolean hasNext() {
                if (next == null) {
                    try {
                        return (next = reader.read()) != -1;
                    } catch (Exception e) {
                        next = -1;
                        return false;
                    }
                }

                return next != -1;
            }

            @Override
            public Character next() {
                if (next == null) {
                    try {
                        next = reader.read();
                    } catch (Exception e) {
                        return null;
                    }
                }

                char ret = next == -1 ? null : (char) next.intValue();
                next = null;

                return ret;
            }
        };
    }
}
