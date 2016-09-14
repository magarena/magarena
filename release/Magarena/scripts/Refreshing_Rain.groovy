def FOREST_AND_SWAMP_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().controlsPermanent(MagicSubType.Swamp) &&
               source.getController().controlsPermanent(MagicSubType.Forest);
    }
};

[
    new MagicHandCastActivation(
        [FOREST_AND_SWAMP_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Removal, true),
        "Free"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [];
        }
    }
]
