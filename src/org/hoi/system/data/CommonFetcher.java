package org.hoi.system.data;

import org.hoi.element.common.Autonomy;
import org.hoi.element.common.Building;
import org.hoi.element.common.Idea;
import org.hoi.element.common.Ideology;
import org.hoi.element.common.focus.FocusTree;
import org.hoi.various.collection.concat.ConcatList;

import java.util.List;

public abstract class CommonFetcher {
    final public static DefaultsCommonFetcher DEFAULTS = new DefaultsCommonFetcher();

    public abstract List<FocusTree> getFocusTrees ();
    public abstract List<Autonomy> getAutonomies ();
    public abstract List<Building.OfProvince> getProvinceBuildings ();
    public abstract List<Building.OfState> getStateBuildings ();
    public abstract List<Idea> getIdeas ();
    public abstract List<Idea.Category> getIdeaCategories();
    public abstract List<Ideology> getIdeologies();

    final public List<Building> getBuildings () {
        return new ConcatList<>(getProvinceBuildings(), getStateBuildings());
    }

    // STATIC
    private static class DefaultsCommonFetcher extends CommonFetcher {
        private DefaultsCommonFetcher() {}

        @Override
        public List<FocusTree> getFocusTrees() {
            return FocusTree.getDefaults();
        }

        @Override
        public List<Autonomy> getAutonomies() {
            return Autonomy.getDefaults();
        }

        @Override
        public List<Building.OfProvince> getProvinceBuildings() {
            return Building.OfProvince.getDefaults();
        }

        @Override
        public List<Building.OfState> getStateBuildings() {
            return Building.OfState.getDefaults();
        }

        @Override
        public List<Idea> getIdeas() {
            return Idea.getDefaults();
        }

        @Override
        public List<Idea.Category> getIdeaCategories() {
            return Idea.Category.getDefaults();
        }

        @Override
        public List<Ideology> getIdeologies() {
            return Ideology.getDefaults();
        }
    }
}
