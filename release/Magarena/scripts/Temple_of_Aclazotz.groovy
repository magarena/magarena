[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal)
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    SACRIFICE_CREATURE
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = ((MagicPermanent)payedCost.getTarget()).getToughness();
            return new MagicEvent(
                source,
                amount,
                this,
                "PN gains ${amount} life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), event.getRefInt()));
        }
    }
]

