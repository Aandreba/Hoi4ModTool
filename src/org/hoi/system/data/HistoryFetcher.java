package org.hoi.system.data;

import org.hoi.element.history.Country;
import org.hoi.element.history.State;
import org.hoi.element.map.Province;

import java.util.List;

public abstract class HistoryFetcher {
    final public static DefaultsHistoryFetcher DEFAULTS = new DefaultsHistoryFetcher();

    public abstract List<Country> getCountries ();
    public abstract List<State> getStates ();
    public abstract List<State.Category> getStateCategories();

    // STATE
    final public State getState (int id) {
        return getStates().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    final public State getState (Province province) {
        return getStates().stream().filter(x -> x.getProvinces().contains(province.getId())).findFirst().orElse(null);
    }

    final public Country getStateOwner (State state) {
        return state.getHistory().getOwner(getCountries());
    }

    final public Country getStateController (State state) {
        return state.getHistory().getController(getCountries());
    }

    final public State.Category getStateCategory (String name) {
        return getStateCategories().stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    final public Country getCountry (String tag) {
        return getCountries().stream().filter(x -> x.getTag().equals(tag)).findFirst().orElse(null);
    }

    // STATIC
    public static class DefaultsHistoryFetcher extends HistoryFetcher {
        private DefaultsHistoryFetcher () {}

        @Override
        public List<Country> getCountries() {
            return Country.getDefaults();
        }

        @Override
        public List<State> getStates() {
            return State.getDefaults();
        }

        @Override
        public List<State.Category> getStateCategories() {
            return State.Category.getDefaults();
        }
    }
}
