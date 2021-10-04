package org.hoi.system.effects.types;

import org.hoi.various.collection.readonly.ReadOnlyList;
import org.hoi.system.effects.Scope;
import org.hoi.system.effects.Target;

public class Trigger {
    protected static ReadOnlyList<Trigger> DEFAULTS;

    final public String name, description;
    final public ReadOnlyList<Scope> scopes;
    final public ReadOnlyList<Target> targets;

    protected Trigger (String name, String description, ReadOnlyList<Scope> scopes, ReadOnlyList<Target> targets) {
        this.name = name;
        this.description = description;
        this.scopes = scopes;
        this.targets = targets;
    }

    public static Trigger valueOf (String name) {
        return DEFAULTS.stream().filter(x -> x.name.equals(name)).findFirst().orElse(null);
    }

    public static ReadOnlyList<Trigger> getDefaults () {
        return DEFAULTS;
    }
}
