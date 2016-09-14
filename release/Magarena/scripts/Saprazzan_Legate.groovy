def ISLAND_AND_MOUNTAIN_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().controlsPermanent(MagicSubType.Mountain) &&
               source.getController().controlsPermanent(MagicSubType.Island);
    }
};

[
    new MagicHandCastActivation(
        [ISLAND_AND_MOUNTAIN_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Free"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [];
        }
    }
]
