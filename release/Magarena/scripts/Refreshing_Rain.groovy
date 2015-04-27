def FOREST_AND_SWAMP_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().controlsPermanent(MagicSubType.Swamp) &&
               source.getController().controlsPermanent(MagicSubType.Forest);
    }
};

[
    new MagicCardActivation(
        [FOREST_AND_SWAMP_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Removal, true),
        "Free"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [];
        }
    }
]
