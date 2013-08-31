[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ChargeCountersAtLeast(2),
        ],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getCounters(MagicCounterType.Charge);
            return new MagicEvent(
                source,
                amount >= 6 ? 2 : 1,
                this,
                "PN puts RN 3/3 green Elephant creature tokens onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Elephant"),
                event.getRefInt()
            ));
        }
    }
]
