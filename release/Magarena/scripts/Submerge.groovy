def ISLAND_AND_FOREST_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().controlsPermanent(MagicSubType.Forest) &&
               source.getController().controlsPermanent(MagicSubType.Island);
    }
};

[
    new MagicHandCastActivation(
        [ISLAND_AND_FOREST_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Removal, true),
        "Free"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [];
        }
    }
]
