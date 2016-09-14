def MOUNTAIN_AND_ISLAND_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().controlsPermanent(MagicSubType.Island) &&
               source.getController().controlsPermanent(MagicSubType.Mountain);
    }
};

[
    new MagicHandCastActivation(
        [MOUNTAIN_AND_ISLAND_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Removal, true),
        "Free"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [];
        }
    }
]
