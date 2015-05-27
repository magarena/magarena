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
            final int n = source.getController().getNrOfPermanents(GREEN_CREATURE);
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getCost().reduce(MagicCostManaType.Green, n)
                )
            ];
        }
    }
]
