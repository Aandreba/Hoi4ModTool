package org.hoi.system;

import org.hoi.various.collection.MappedList;
import org.hoi.various.map.AbstractLazyMap;
import org.hoi.various.map.LazyMap;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

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
                return (Boolean) input;
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

    public List<Color> getColor (String key) {
        return new MappedList<>(get(key)) {
            @Override
            protected Color map (Object input) {
                if (input instanceof Color) {
                    return (Color) input;
                }

                HoiList list = (HoiList) input;
                return new Color(list.getInteger(0), list.getInteger(1), list.getInteger(2));
            }
        };
    }

    public String getFirstString (String key) {
        return getFirst(key).toString();
    }

    public boolean getFirstBool (String key) {
        return getFirstAs(key);
    }

    public boolean getFirstBoolOrElse (String key, boolean other) {
        try {
            return getFirstAs(key);
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

    public Color getFirstColor (String key) {
        Object input = getFirst(key);
        if (input instanceof Color) {
            return (Color) input;
        }

        HoiList list = (HoiList) input;
        return new Color(list.getInteger(0), list.getInteger(1), list.getInteger(2));
    }

    public Color getFirstColorOrElse (String key, Color other) {
        try {
            return getFirstColor(key);
        } catch (Exception ignore) {}

        return other;
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
        while (first == '\n' || first == '\r' || first == '\t' || first == ' ') {
            first = (char) reader.read();
        }

        if (Character.isDigit(first) || first == '-') {
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

        Object tag = tagValue(reader, first);
        if (tag instanceof HoiTag && tag.toString().equals("rgb")) {
            HoiList next = ((HoiList) collectionValue(reader)).getAs(0);
            return new Color(next.getInteger(0), next.getInteger(1), next.getInteger(2));
        } else if (tag instanceof HoiTag && tag.toString().equalsIgnoreCase("hsv")) {
            HoiList next = ((HoiList) collectionValue(reader)).getAs(0);
            return Color.getHSBColor(next.getFloat(0), next.getFloat(1), next.getFloat(2));
        }

        return tag;
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
                if (split.length >= 2) {
                    HoiMap map = new HoiMap(reader, null, true);
                    map.add(split[0], parseValue(split[1]));
                    return map;
                }

                return new HoiMap(reader, split[0], true);
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

    private static Object tagValue(Reader reader, char first) throws IOException {
        StringBuilder builder = new StringBuilder().append(first);
        int read;
        while ((read = reader.read()) != -1) {
            char c = (char) read;
            if (c == '\n' || c == '\r' || c == '\t' || c == ' ') {
                break;
            }

            builder.append(c);
        }

        String str = builder.toString();
        return str.equals("yes") ? Boolean.TRUE : (str.equals("no") ? Boolean.FALSE : new HoiTag(str));
    }

    private static Number integerValue (Reader reader, char first) throws IOException {
        boolean isNeg = first == '-';
        int integer = isNeg ? 0 : first - 48;

        int read;
        while ((read = reader.read()) != -1) {
            if (read == 46) {
                return floatValue(reader, integer, isNeg);
            } else if (read < 48 || read > 57) {
                break;
            }

            integer *= 10;
            integer += read - 48;
        }

        return isNeg ? -integer : integer;
    }

    private static float floatValue(Reader reader, int integer, boolean isNeg) throws IOException {
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

        return isNeg ? -value : value;
    }

    private static Object parseValue (String value) throws IOException {
        char first = value.charAt(0);

        if (Character.isDigit(first) || first == '-') {
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

        return value.equals("yes") ? Boolean.TRUE : (value.equals("no") ? Boolean.FALSE : new HoiTag(value));
    }
}
