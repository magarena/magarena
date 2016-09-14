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
            return source.getOpponent().getNrOfPermanents(MagicType.Land) >= 7 ?
                [MagicPayManaCostEvent.Cast(source,"{R}{R}")] :
                [MagicPayManaCostEvent.Cast(source,"{6}{R}{R}")];
        }
    }
]
