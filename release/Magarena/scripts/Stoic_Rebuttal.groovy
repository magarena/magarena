[
     new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setCardAct(this);
        }

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
        return MagicCondition.METALCRAFT_CONDITION.accept(source) ?
            [new MagicPayManaCostEvent(source,"{U}{U}")] :
            [new MagicPayManaCostEvent(source,"{1}{U}{U}")];
        }
    }
]
