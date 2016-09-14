[
    new MagicHandCastActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setHandAct(this);
        }

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            final int n = CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(source).size();
            return n >= 10 ?
                [MagicPayManaCostEvent.Cast(source,"{B}{B}")] :
                [MagicPayManaCostEvent.Cast(source,"{6}{B}{B}")];
        }
    }
]
