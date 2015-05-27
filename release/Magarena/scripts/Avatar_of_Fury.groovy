[
    new MagicHandCastActivation(
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
            return source.getOpponent().getNrOfPermanents(MagicType.Land) >= 7 ?
                [new MagicPayManaCostEvent(source,"{R}{R}")] :
                [new MagicPayManaCostEvent(source,"{6}{R}{R}")];
        }
    }
]
