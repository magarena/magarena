[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{1}"),
            MagicCondition.ONE_SAPROLING_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicSacrificePermanentEvent(
                    source,
                    MagicTargetChoice.SACRIFICE_SAPROLING
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Draw a card."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(), 1));
        }
    }
]
