def ISLAND_AND_MOUNTAIN_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(MagicSubType.Island) >= 1 && 
        source.getController().getOpponent().getNrOfPermanents(MagicSubType.Mountain) >= 1;
    }
};

[
     new MagicCardActivation(
        [ISLAND_AND_MOUNTAIN_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "NoCost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{0}")];
        }
    }
]
