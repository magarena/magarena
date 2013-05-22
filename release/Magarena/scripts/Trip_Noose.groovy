[
    new MagicTapCreatureActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ManaCost("{2}")
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{2}")
            ];
        }
    }
]
