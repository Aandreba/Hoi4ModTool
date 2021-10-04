package org.hoi.system;

import org.hoi.various.collection.MappedList;
import org.hoi.various.map.AbstractLazyMap;
import org.hoi.various.map.LazyMap;

import java.io.*;
import java.util.*;

public class HoiMap extends AbstractLazyMap<String, Object> {
    final private ArrayList<Map.Entry<String, Object>> entries;

    public HoiMap () {
        this.entries = new ArrayList<>();
    }

    public HoiMap (HoiMap other) {
        this.entries = other.entries;
    }

    public HoiMap (File file) throws IOException {
        this(new FileReader(file));
    }

    public HoiMap (Reader reader) throws IOException {
        this(reader, null, false);
    }

    private HoiMap (Reader reader, String firstKey, boolean separated) throws IOException {
        this();

        if (firstKey != null && !firstKey.startsWith("#")) {
            this.add(firstKey, getValue(reader));
        }

        int read;
        StringBuilder builder = new StringBuilder();
        boolean commentStarted = false;

        while ((read = reader.read()) != -1) {
            char c = (char) read;

            if (c == '#') {
                commentStarted = true;
            } else if (commentStarted) {
                commentStarted = c != '\n' && c != '\r';
            } else {
                if (c == '}' && separated) {
                    break;
                } else if (c == '=') {
                    Object value = getValue(reader);
                    this.add(builder.toString(), value);
                    builder = new StringBuilder();
                } else if (c != '\n' && c != '\r' && c != '\t' && c != ' ') {
                    builder.append(c);
                }
            }
        }
    }

    @Override
    public List<Map.Entry<String, Object>> entries () {
        return entries;
    }

    @Override
    public int size() {
        return entries.size();
    }

    public <T> List<T> getAs (String key) {
        return new MappedList<>(get(key)) {
            @Override
            protected T map (Object input) {
                return (T) input;
            }
        };
    }

    public <T> List<T> getAs (Class<T> type, String key) {
        return getAs(key);
    }

    public <T> T getFirstAs (String key) {
        return (T) getFirst(key);
    }

    public <T> T getFirstAs (Class<T> type, String key) {
        return getFirstAs(key);
    }

    public <T> T getFirstAsOrElse (String key, T other) {
        try {
            return (T) getFirst(key);
        } catch (Exception e) {
            return other;
        }
    }

    public List<String> getString (String key) {
        return new MappedList<>(get(key)) {
            @Override
            protected String map (Object input) {
                return input.toString();
            }
        };
    }

    public List<Boolean> getBool (String key) {
        return new MappedList<>(get(key)) {
            @Override
            protected Boolean map (Object input) {
                return input.toString().equals("yes");
            }
        };
    }

    public List<Integer> getInteger (String key) {
        return new MappedList<>(get(key)) {
            @Override
            protected Integer map (Object input) {
                return ((Number) input).intValue();
            }
        };
    }

    public List<Float> getFloat (String key) {
        return new MappedList<>(get(key)) {
            @Override
            protected Float map (Object input) {
                return ((Number) input).floatValue();
            }
        };
    }

    public String getFirstString (String key) {
        return getFirst(key).toString();
    }

    public boolean getFirstBool (String key) {
        return getFirstAs(HoiTag.class, key).toString().equals("yes");
    }

    public boolean getFirstBoolOrElse (String key, boolean other) {
        try {
            return getFirstAs(HoiTag.class, key).toString().equals("yes");
        } catch (Exception e) {
            return other;
        }
    }

    public int getFirstInteger (String key) {
        return getFirstAs(Number.class, key).intValue();
    }

    public int getFirstIntegerOrElse (String key, int other) {
        try {
            return getFirstAs(Number.class, key).intValue();
        } catch (Exception e) {
            return other;
        }
    }

    public float getFirstFloat (String key) {
        return getFirstAs(Number.class, key).floatValue();
    }

    public float getFirstFloatOrElse (String key, float other) {
        try {
            return getFirstAs(Number.class, key).floatValue();
        } catch (Exception e) {
            return other;
        }
    }

    @Override
    public String toString () {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry: entries) {
            builder.append(entry.getKey()).append(" = ");

            if (entry.getValue() instanceof LazyMap || entry.getValue() instanceof List) {
                builder.append("{\n").append(entry.getValue()).append("}\n");
            } else if (entry.getValue() instanceof String) {
                builder.append('"').append(entry.getValue()).append("\"\n");
            } else {
                builder.append(entry.getValue()).append('\n');
            }
        }

        return builder.toString();
    }

    // READERS
    private static Object getValue (Reader reader) throws IOException {
        int read;
        while ((read = reader.read()) != -1) {
            char c = (char) read;
            if (c == '\n' || c == '\t' || c == ' ') {
                continue;
            }

            return decideValue(reader, c);
        }

        return null;
    }

    private static Object decideValue (Reader reader, char first) throws IOException {
        if (Character.isDigit(first)) {
            return integerValue(reader, first);
        } else if (first == '"') {
            return stringValue(reader);
        } else if (first == '{') {
            return collectionValue(reader);
        } else if (first == '#') {
            int read;
            boolean commentEnded = false;

            while ((read = reader.read()) != -1) {
                char c = (char) read;
                if (!commentEnded && (c == '\n' || c == '\r')) {
                    commentEnded = true;
                }

                if (commentEnded && c > 32) {
                    return decideValue(reader, c);
                }
            }
        }

        return tagValue(reader, first);
    }

    private static Object collectionValue (Reader reader) throws IOException {
        HoiList list = new HoiList();

        int read;
        while ((read = reader.read()) != -1) {
            char c = (char) read;
            if (c == '\n' || c == '\r' || c == '\t' || c == ' ') {
                continue;
            } else if (c == '}') {
                break;
            } else if (c == '=') {
                return new HoiMap(reader, list.get(0).toString(), true);
            }

            Object value = decideValue(reader, c);
            if (value instanceof HoiTag && value.toString().contains("=")) {
                String[] split = value.toString().split("=");
                HoiMap map = new HoiMap(reader, null, true);

                map.add(split[0], parseValue(split[1]));
                return map;
            }

            list.add(value);
        }

        return list;
    }

    private static String stringValue(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();

        int read;
        while ((read = reader.read()) != -1) {
            char c = (char) read;
            if (c == '"') {
                break;
            }

            builder.append(c);
        }

        return builder.toString();
    }

    private static HoiTag tagValue(Reader reader, char first) throws IOException {
        StringBuilder builder = new StringBuilder().append(first);
        int read;
        while ((read = reader.read()) != -1) {
            char c = (char) read;
            if (c == '\n' || c == '\r' || c == '\t' || c == ' ') {
                break;
            }

            builder.append(c);
        }

        return new HoiTag(builder.toString());
    }

    private static Number integerValue(Reader reader, char first) throws IOException {
        int integer = first - 48;

        int read;
        while ((read = reader.read()) != -1) {
            if (read == 46) {
                return floatValue(reader, integer);
            } else if (read < 48 || read > 57) {
                break;
            }

            integer *= 10;
            integer += read - 48;
        }

        return integer;
    }

    private static float floatValue(Reader reader, int integer) throws IOException {
        float value = integer;
        int i = 10;

        int read;
        while ((read = reader.read()) != -1) {
            if (read < 48 || read > 57) {
                break;
            }

            value += (read - 48f) / i;
            i *= 10f;
        }

        return value;
    }

    private static Object parseValue (String value) throws IOException {
        char first = value.charAt(0);

        if (Character.isDigit(first)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                return Float.parseFloat(value);
            }
        } else if (first == '"') {
            return value.substring(1, value.length() - 1);
        } else if (first == '{') {
            return collectionValue(new StringReader(value));
        }

        return new HoiTag(value);
    }
}
