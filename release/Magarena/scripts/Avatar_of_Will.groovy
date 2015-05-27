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
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return source.getOpponent().getHandSize() == 0 ?
                [new MagicPayManaCostEvent(source,"{U}{U}")] :
                [new MagicPayManaCostEvent(source,"{6}{U}{U}")];
        }
    }
]
