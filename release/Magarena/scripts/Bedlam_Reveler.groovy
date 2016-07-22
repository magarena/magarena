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
            final int n = INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD.filter(source).size();
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getGameCost().reduce(n)
                )
            ];
        }
    }
]
