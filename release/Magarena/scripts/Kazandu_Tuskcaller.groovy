[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.CounterAtLeast(MagicCounterType.Level,2),
        ],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Level);
            return new MagicEvent(
                source,
                amount >= 6 ? 2 : 1,
                this,
                "PN creates RN 3/3 green Elephant creature tokens onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("3/3 green Elephant creature token"),
                event.getRefInt()
            ));
        }
    }
]
