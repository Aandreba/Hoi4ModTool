package org.hoi.system.effects.types;

import org.hoi.various.collection.readonly.ReadOnlyList;
import org.hoi.system.effects.Scope;
import org.hoi.system.effects.Target;

public class Effect {
    protected static ReadOnlyList<Effect> DEFAULTS;

    final public String name, description;
    final public ReadOnlyList<Scope> scopes;
    final public ReadOnlyList<Target> targets;

    public Effect (String name, String description, ReadOnlyList<Scope> scopes, ReadOnlyList<Target> targets) {
        this.name = name;
        this.description = description;
        this.scopes = scopes;
        this.targets = targets;
    }

    public static Effect valueOf (String name) {
        return DEFAULTS.stream().filter(x -> x.name.equals(name)).findFirst().orElse(null);
    }

    public static ReadOnlyList<Effect> getDefaults () {
        return DEFAULTS;
    }
}
