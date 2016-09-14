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
            return source.getOpponent().getHandSize() == 0 ?
                [MagicPayManaCostEvent.Cast(source,"{U}{U}")] :
                [MagicPayManaCostEvent.Cast(source,"{6}{U}{U}")];
        }
    }
]
