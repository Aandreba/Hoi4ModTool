package org.hoi.system;

import org.hoi.various.collection.MappedList;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class HoiList extends ArrayList<Object> {
    public HoiList () {
       super();
    }

    public <T> T getAs (int pos) {
        return (T) get(pos);
    }

    public <T> T getAs (Class<T> type, int pos) {
        return getAs(pos);
    }

    public <T> T getAsOrElse (int pos, T other) {
        try {
            return (T) get(pos);
        } catch (Exception e) {
            return other;
        }
    }

    public boolean getBool (int pos) {
        return getAs(HoiTag.class, pos).toString().equals("yes");
    }

    public boolean getBoolOrElse (int pos, boolean other) {
        try {
            return getAs(HoiTag.class, pos).toString().equals("yes");
        } catch (Exception e) {
            return other;
        }
    }

    public int getInteger (int pos) {
        return getAs(Number.class, pos).intValue();
    }

    public int getIntegerOrElse (int pos, int other) {
        try {
            return getAs(Number.class, pos).intValue();
        } catch (Exception e) {
            return other;
        }
    }

    public float getFloat (int pos) {
        return getAs(Number.class, pos).floatValue();
    }

    public float getFloatOrElse (int pos, float other) {
        try {
            return getAs(Number.class, pos).floatValue();
        } catch (Exception e) {
            return other;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Object obj: this) {
            builder.append(", ").append(obj);
        }

        return "{"+builder.substring(2)+"}";
    }
}
