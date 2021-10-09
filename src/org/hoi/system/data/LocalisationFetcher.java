package org.hoi.system.data;

import org.hoi.element.localisation.Localisation;
import org.hoi.various.collection.WrappedCollection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class LocalisationFetcher {
    final public static DefaultsLocalisationFetcher DEFAULTS = new DefaultsLocalisationFetcher();

    public abstract List<Locale> getLocales ();
    public abstract List<Localisation> getLocalisations ();

    final public Locale getDefaultLocale () {
        return getLocales().stream().filter(x -> x.getLanguage().equals("en")).findFirst().orElse(getLocales().get(0));
    }

    final public Locale getTrueLocale (Locale locale) {
        Locale langLocale = new Locale(locale.getLanguage());
        return getLocales().contains(langLocale) ? langLocale : getDefaultLocale();
    }

    final public String get (String key, Locale locale) {
        Locale langLocale = getTrueLocale(locale);
        return getEntries(locale).filter(x -> x.getKey().equals(key)).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    final public String get (String key) {
        return get(key, Locale.getDefault());
    }

    final public Map<String, String> getEntriesOfLocale (Locale locale) {
        return new AbstractMap<String, String>() {
            final Set<Entry<String, String>> set = getEntries(locale).collect(Collectors.toSet());

            @Override
            public Set<Entry<String, String>> entrySet() {
                return set;
            }
        };
    }

    private Stream<Map.Entry<String, String>> getEntries (Locale locale) {
        Locale langLocale = new Locale(locale.getLanguage());
        return getLocalisations().stream().filter(x -> x.getLocale().equals(langLocale)).flatMap(x -> x.entrySet().stream()).filter(x -> x.getKey() != null && x.getValue() != null);
    }

    // STATIC
    private static class DefaultsLocalisationFetcher extends LocalisationFetcher {
        private DefaultsLocalisationFetcher () {}

        @Override
        public List<Locale> getLocales () {
            Collection<Locale> locales = Localisation.LOCALE_MAP.values();
            return locales instanceof List ? (List<Locale>) locales : new WrappedCollection<>(locales);
        }

        @Override
        public List<Localisation> getLocalisations() {
            return Localisation.getDefaults();
        }
    }
}
