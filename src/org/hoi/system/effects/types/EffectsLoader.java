package org.hoi.system.effects.types;

import org.hoi.system.HoiList;
import org.hoi.system.HoiLoader;
import org.hoi.various.collection.readonly.ReadOnlyList;
import org.hoi.system.effects.Scope;
import org.hoi.system.effects.Target;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.sjr.ArrayReader;
import org.sjr.ObjectReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class EffectsLoader {
    public static void loadDefaults () throws IOException, ParseException {
        File file = HoiLoader.getFile("documentation/script_documentation.json");
        ObjectReader reader = new ObjectReader(Files.readString(file.toPath()));

        // TRIGGERS
        ArrayList<Trigger> triggerLists = new ArrayList<>();
        ObjectReader triggers = reader.getObject("triggers");

        for (Map.Entry<String, Object> entry: triggers.entrySet()) {
            ObjectReader data = new ObjectReader((JSONObject) entry.getValue());

            String name = entry.getKey();
            String desc = data.getString("description");

            ReadOnlyList<Scope> scopes = new ReadOnlyList<>(data.getArray("supported_scope").stream()
                    .map(x -> Scope.valueOf(x.toString().toUpperCase()))
                    .collect(Collectors.toList()));

            ReadOnlyList<Target> targets = new ReadOnlyList<>(data.getArray("supported_target").stream()
                    .map(x -> Target.valueOf(x.toString().toUpperCase()))
                    .collect(Collectors.toList()));

            triggerLists.add(new Trigger(name, desc, scopes, targets));
        }

        Trigger.DEFAULTS = new ReadOnlyList<>(triggerLists);

        // EFFECTS
        ArrayList<Effect> effectList = new ArrayList<>();
        ObjectReader effects = reader.getObject("effects");

        for (Map.Entry<String, Object> entry: effects.entrySet()) {
            ObjectReader data = new ObjectReader((JSONObject) entry.getValue());

            String name = entry.getKey();
            String desc = data.getString("description");

            ReadOnlyList<Scope> scopes = new ReadOnlyList<>(data.getArray("supported_scope").stream()
                    .map(x -> Scope.valueOf(x.toString().toUpperCase()))
                    .collect(Collectors.toList()));

            ReadOnlyList<Target> targets = new ReadOnlyList<>(data.getArray("supported_target").stream()
                    .map(x -> Target.valueOf(x.toString().toUpperCase()))
                    .collect(Collectors.toList()));

            effectList.add(new Effect(name, desc, scopes, targets));
        }

        Effect.DEFAULTS = new ReadOnlyList<>(effectList);

        // MODIFIERS
        ArrayList<Modifier> modifierList = new ArrayList<>();
        ArrayReader modifiers = reader.getArray("modifiers");

        for (Object entry: modifiers) {
            ObjectReader data = new ObjectReader((JSONObject) entry);
            int decimal;

            String name = data.getString("name");
            Modifier.Type type = Modifier.Type.valueOf(data.getString("type").toUpperCase());
            ReadOnlyList<Modifier.Category> categories = new ReadOnlyList<>(data.getArray("categories").stream().map(x -> Modifier.Category.valueOf(x.toString().toUpperCase())).collect(Collectors.toList()));

            try {
                decimal = data.getInt("decimal_places");
            } catch (Exception e) {
                decimal = 0;
            }

            modifierList.add(new Modifier(name, type, decimal, categories));
        }

        Modifier.DEFAULTS = new ReadOnlyList<>(modifierList);
    }
}
