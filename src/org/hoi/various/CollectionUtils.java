package org.hoi.various;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils {
    public static <T> Collection<T> empty () {
        return new AbstractCollection<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public T next() {
                        return null;
                    }
                };
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }
}
