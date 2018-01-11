[
    new MagicStatic(Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            source.getController().setMaxHandSize(source.getCounters(MagicCounterType.Hour));
        }
    }
]

