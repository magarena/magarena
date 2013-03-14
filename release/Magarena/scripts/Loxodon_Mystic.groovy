[
    new MagicTapCreatureActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ManaCost("{W}")
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,source.getController(),
                MagicManaCost.create("{W}"))
            ];
        }
    }
]
