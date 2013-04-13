[
    new MagicWeakenCreatureActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ManaCost("{4}")
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "-1/-1") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(
                source,
                source.getController(),
                MagicManaCost.create("{4}")
            )];
        }
    }
]
