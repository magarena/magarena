[
    new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Counter, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setCardAct(this);
        }

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final int n = MagicCondition.METALCRAFT_CONDITION.accept(source) ? 1 : 0;
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getCost().reduce(MagicCostManaType.Colorless, n)
                )
            ];
        }
    }
]
