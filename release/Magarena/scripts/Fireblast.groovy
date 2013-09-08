[
    new MagicCardActivation(
        [MagicCondition.TWO_MOUNTAINS_CONDITION],
        new MagicActivationHints(MagicTiming.Removal,true),
        "Alt"
    ) {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_MOUNTAIN),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_MOUNTAIN)             
            ];
        }
    }
]
