package org.hoi.system.effects.types;

import org.hoi.various.collection.readonly.ReadOnlyList;

public class Modifier {
    protected static ReadOnlyList<Modifier> DEFAULTS;

    final public String name;
    final public Type type;
    final public int decimalPlaces;
    final public ReadOnlyList<Category> categories;

    protected Modifier (String name, Type type, int decimalPlaces, ReadOnlyList<Category> categories) {
        this.name = name;
        this.type = type;
        this.decimalPlaces = decimalPlaces;
        this.categories = categories;
    }

    public static Modifier valueOf (String name) {
        return DEFAULTS.stream().filter(x -> x.name.equals(name)).findFirst().orElse(null);
    }

    public static ReadOnlyList<Modifier> getDefaults () {
        return DEFAULTS;
    }

    public enum Type {
        NUMBER,
        BOOL
    }

    public enum Category {
        AIR,
        NAVAL,
        UNIT_LEADER,
        AGGRESSIVE,
        COUNTRY,
        ARMY,
        MILITARY_ADVANCEMENTS,
        STATE,
        WAR_PRODUCTION,
        DEFENSIVE,
        PEACE,
        POLITICS,
        AUTONOMY,
        GOVERNMENT_IN_EXILE,
        AI,
        INTELLIGENCE_AGENCY
    }
}
