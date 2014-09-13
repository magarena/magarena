def FOREST_AND_SWAMP_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(MagicSubType.Forest) >= 1 && 
        source.getController().getOpponent().getNrOfPermanents(MagicSubType.Swamp) >= 1;
    }
};

[
     new MagicCardActivation(
        [FOREST_AND_SWAMP_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "NoCost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{0}")];
        }
    }
]
