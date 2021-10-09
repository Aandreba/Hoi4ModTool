package org.hoi.system.data;

public abstract class ElementFetcher {
    final public static DefaultsFetcher DEFAULTS = new DefaultsFetcher();

    public abstract CommonFetcher getCommon ();
    public abstract HistoryFetcher getHistory ();
    public abstract MapFetcher getMap ();
    public abstract LocalisationFetcher getLocalisation();

    // STATIC
    private static class DefaultsFetcher extends ElementFetcher {
        private DefaultsFetcher () {}

        @Override
        public CommonFetcher getCommon() {
            return CommonFetcher.DEFAULTS;
        }

        @Override
        public HistoryFetcher getHistory() {
            return HistoryFetcher.DEFAULTS;
        }

        @Override
        public MapFetcher getMap() {
            return MapFetcher.DEFAULTS;
        }

        @Override
        public LocalisationFetcher getLocalisation() {
            return LocalisationFetcher.DEFAULTS;
        }
    }
}
