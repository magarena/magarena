[
    new MagicHandCastActivation(
        [MagicCondition.SWAMP_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Removal,true),
        "Pay Life"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayLifeEvent(source, 4)
            ];
        }
    }
]
