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
            final int n = source.getOpponent().getHandSize() == 0 ? 6 : 0;
            return [
                new MagicPayManaCostEvent(source, source.getGameCost().reduce(n))
            ];
        }
    }
]
