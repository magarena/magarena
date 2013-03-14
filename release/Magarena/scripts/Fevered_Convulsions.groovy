[
    new MagicWeakenCreatureActivation(
        [MagicConditionFactory.ManaCost("{2}{B}{B}")],
        new MagicActivationHints(MagicTiming.Removal,true),
        "-1/-1") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getController(),
                    MagicManaCost.create("{2}{B}{B}")
                )
            ];
        }
    }
]
