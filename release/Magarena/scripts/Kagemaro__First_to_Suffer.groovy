[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {
		@Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{B}"), new MagicSacrificeEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "All creatures get -X/-X until end of turn, where X is the number of cards in your hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            Collection<MagicPermanent> creatures = game.filterPermanents(
                player,
                MagicTargetFilterFactory.CREATURE
            )
            final int amt = player.getHandSize();
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicChangeTurnPTAction(
                    creature,
                    -amt,
                    -amt
                ));
		}
        }
    }
]
