def MOUNTAIN_AND_PLAINS_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(MagicSubType.Mountain) >= 1 && 
        source.getController().getOpponent().getNrOfPermanents(MagicSubType.Plains) >= 1;
    }
};

[
     new MagicCardActivation(
        [MOUNTAIN_AND_PLAINS_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "NoCost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{0}")];
        }
    }
]
