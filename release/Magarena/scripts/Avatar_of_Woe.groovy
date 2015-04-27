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
            final int n = source.getGame().filterCards(CREATURE_CARD_FROM_ALL_GRAVEYARDS).size();
            return n >= 10 ?
                [new MagicPayManaCostEvent(source,"{B}{B}")] :
                [new MagicPayManaCostEvent(source,"{6}{B}{B}")];
        }
    }
]
