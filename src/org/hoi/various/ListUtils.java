package org.hoi.various;

import javax.swing.*;
import java.util.*;

public class ListUtils {
    public static <T> List<T> empty () {
        return new AbstractList<T>() {
            @Override
            public T get (int index) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    public static <T> ListModel<T> toListModel (List<T> list) {
        Objects.requireNonNull(list);
        return new AbstractListModel<T>() {
            @Override
            public int getSize() {
                return list.size();
            }

            @Override
            public T getElementAt(int index) {
                return list.get(index);
            }
        };
    }
}
