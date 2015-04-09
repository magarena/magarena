[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{B}"), 
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "All creatures get -X/-X until end of turn, where X is the number of cards in PN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getPlayer().getHandSize();
            final Collection<MagicPermanent> creatures = game.filterPermanents(CREATURE)
            for (final MagicPermanent it : creatures) {
                game.doAction(new MagicChangeTurnPTAction(it, -X, -X));
            }
        }
    }
]
