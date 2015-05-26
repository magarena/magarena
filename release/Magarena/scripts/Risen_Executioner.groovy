[
    new MagicGraveyardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Cast"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final int amount = CREATURE_CARD_FROM_GRAVEYARD.except(source).filter(source.getController()).size() + 2;
            return [
                new MagicPayManaCostEvent(source, "{"+amount+"}{B}{B}")
            ];
        }
    }
]
