[
    new MagicCardActivation(
        [MagicCondition.THREE_BLACK_CREATURES_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main,true),
        "Alt"
    ) {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayLifeEvent(source, 6),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_BLACK_CREATURE),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_BLACK_CREATURE),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_BLACK_CREATURE)
            ];
        }
    }
]
