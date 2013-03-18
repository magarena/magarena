[
    new MagicTapCreatureActivation(
        [
            MagicCondition.CAN_UNTAP_CONDITION,
            MagicConditionFactory.ManaCost("{1}{W/U}")
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}{W/U}")),
                new MagicUntapEvent(source)
            ];
        }
    }
]
