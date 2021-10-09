package org.hoi.element.common.focus;

import org.hoi.system.hoi.HoiLoader;
import org.hoi.system.hoi.HoiMap;
import org.hoi.various.collection.map.MappedList;
import org.hoi.various.collection.readonly.ReadOnlyList;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FocusTree extends HoiMap {
    private static ReadOnlyList<FocusTree> DEFAULTS;

    final private List<Focus> focuses;

    public FocusTree (HoiMap other) {
        super(other);

        this.focuses = new MappedList<>(this.get("focus")) {
            @Override
            protected Focus map(Object input) {
                return new Focus((HoiMap) input);
            }
        };
    }

    public FocusTree (File file) throws IOException {
        super(file);

        this.focuses = new MappedList<>(this.get("focus")) {
            @Override
            protected Focus map(Object input) {
                return new Focus((HoiMap) input);
            }
        };
    }

    public FocusTree (Reader reader) throws IOException {
        super(reader);

        this.focuses = new MappedList<>(this.get("focus")) {
            @Override
            protected Focus map(Object input) {
                return new Focus((HoiMap) input);
            }
        };
    }

    public String getId () {
        return this.getFirstString("id");
    }

    public boolean isDefault () {
        return this.getFirstBoolOrElse("default", false);
    }

    public boolean resetOnCivilWar () {
        return this.getFirstBoolOrElse("reset_on_civil_war", false);
    }

    public Point getContinuousPoint () {
        HoiMap map = this.getFirstAs("continuous_focus_position");
        if (map == null) {
            return new Point(0, 0);
        }

        return new Point(map.getFirstIntegerOrElse("x", 0), map.getFirstIntegerOrElse("y", 0));
    }

    public List<Focus> getFocuses () {
        return focuses;
    }

    public Focus getFocus (String id) {
        return focuses.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    // STATIC
    public static ReadOnlyList<FocusTree> getDefaults () {
        return DEFAULTS;
    }

    public static void loadDefaults () throws IOException {
        File dir = HoiLoader.getFile("common/national_focus");
        File[] files = dir.listFiles();

        ArrayList<FocusTree> tree = new ArrayList<>();
        for (File file: files) {
            HoiMap map = new HoiMap(file);

            for (Map.Entry<String, Object> entry: map) {
                if (entry.getKey().equals("focus_tree")) {
                    tree.add(new FocusTree((HoiMap) entry.getValue()));
                } else if (entry.getKey().equals("shared_focus")) {
                    // TODO
                }
            }
        }

        DEFAULTS = new ReadOnlyList<FocusTree>(tree);
    }

    class Focus extends HoiMap {
        final public FocusTree parent;

        public Focus (HoiMap other) {
            super(other);
            this.parent = FocusTree.this;
        }

        final public String getId () {
            return this.getFirstString("id");
        }

        final public int getCost () {
            return this.getFirstInteger("cost");
        }

        final public int getDays () {
            return 7 * getCost();
        }

        final public boolean isAvailableIfCapitulated () {
            return this.getFirstBoolOrElse("available_if_capitulated", false);
        }

        final public boolean cancelIfInvalid () {
            return this.getFirstBoolOrElse("cancel_if_invalid", true);
        }

        final public boolean continueIfInvalid () {
            return this.getFirstBoolOrElse("cancel_if_invalid", false);
        }

        final public List<String> willLeadToWarWith () {
            return new MappedList<>(this.get("will_lead_to_war_with")) {
                @Override
                protected String map (Object input) {
                    return input.toString();
                }
            };
        }

        final public Focus pointRelativeTo () {
            try {
                String relative = this.getFirstString("relative_position_id");
                return this.parent.getFocuses().stream().filter(x -> x != this && x.getId().equals(relative)).findFirst().orElse(null);
            } catch (Exception ignore) {}

            return null;
        }

        final public Point getRelativePoint () {
            return new Point(this.getFirstIntegerOrElse("x", 0), this.getFirstIntegerOrElse("y", 0));
        }

        final public List<Focus> getPrerequisites () {
            HoiMap prereq = this.getFirstAs("prerequisite");
            return prereq.stream().map(x -> this.parent.getFocus(x.getValue().toString())).collect(Collectors.toList());
        }

        final public List<Focus> getMutuallyExclusive () {
            HoiMap exclusive = this.getFirstAs("mutually_exclusive");
            return exclusive.stream().map(x -> this.parent.getFocus(x.getValue().toString())).collect(Collectors.toList());
        }

        final public Point getAbsolutePoint () {
            Focus previous = pointRelativeTo();
            if (previous == null) {
                return getRelativePoint();
            }

            Point previousPoint = previous.getAbsolutePoint();
            Point relative = getRelativePoint();

            return new Point(previousPoint.x + relative.x, previousPoint.y + relative.y);
        }
    }
}
