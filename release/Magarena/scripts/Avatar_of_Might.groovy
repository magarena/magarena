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
            return source.getOpponent().getNrOfPermanents(MagicType.Creature) >= source.getController().getNrOfPermanents(MagicType.Creature) + 4 ?
                [MagicPayManaCostEvent.Cast(source,"{G}{G}")] :
                [MagicPayManaCostEvent.Cast(source,"{6}{G}{G}")];
        }
    }
]
