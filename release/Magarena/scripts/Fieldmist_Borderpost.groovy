[
    new MagicCardActivation(
        [MagicCondition.CARD_CONDITION, MagicCondition.BASIC_LAND_CONDITION],
        new MagicActivationHints(MagicTiming.Main,true),
        "Alt"
    ) {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicBounceChosenPermanentEvent(source, MagicTargetChoice.TARGET_BASIC_LAND_YOU_CONTROL)
            ];
        }
    }
]
