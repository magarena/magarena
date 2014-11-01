def FOUR_MORE_CREATURES_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().getNrOfPermanents(MagicType.Creature) >= source.getController().getNrOfPermanents(MagicType.Creature) + 4;
    }
};

[
     new MagicCardActivation(
        [FOUR_MORE_CREATURES_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "-Cost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{G}{G}")];
        }
    }
]
