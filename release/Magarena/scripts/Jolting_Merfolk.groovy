[
    new MagicTapCreatureActivation(
        [MagicConditionFactory.ChargeCountersAtLeast(1)],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,1)
            ];
        }
    }
]
