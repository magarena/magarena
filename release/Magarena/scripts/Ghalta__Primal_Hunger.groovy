[
    new MagicHandCastActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Draw, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setHandAct(this);
        }

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            final int x = (int)CREATURE_YOU_CONTROL.filter(source)*.getPower().sum(0);
            return [
                new MagicPayManaCostEvent(source, source.getGameCost().reduce(x))
            ];
        }
    }
]

