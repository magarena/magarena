[
    new MagicHandCastActivation(
        [MagicCondition.SWAMP_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Sacrifice"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicSacrificePermanentEvent(source, SACRIFICE_CREATURE)             
            ];
        }
    }
]
