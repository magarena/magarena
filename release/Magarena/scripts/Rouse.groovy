[
    new MagicHandCastActivation(
        [MagicCondition.SWAMP_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Pay Life"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayLifeEvent(source, 2)
            ];
        }
    }
]
