def ISLAND_AND_FOREST_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(MagicSubType.Island) >= 1 && 
        source.getController().getOpponent().getNrOfPermanents(MagicSubType.Forest) >= 1;
    }
};

[
     new MagicCardActivation(
        [ISLAND_AND_FOREST_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "NoCost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{0}")];
        }
    }
]
