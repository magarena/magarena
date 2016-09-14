[
    new MagicPermanentActivation(
        [new MagicArtificialCondition(MagicConditionFactory.HandAtLeast(3))],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(source, SACRIFICE_CREATURE)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "PN draw cards equal to RN's power, then discards three cards."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=event.getRefPermanent().getPower();
            game.logAppendValue(player,amount);
            game.doAction(new DrawAction(player, amount));
            game.addEvent(new MagicDiscardEvent(event.getSource(), player, 3));
        }
    }
]
