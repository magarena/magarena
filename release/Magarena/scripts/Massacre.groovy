def SWAMP_AND_PLAINS_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getOpponent().controlsPermanent(MagicSubType.Plains) &&
               source.getController().controlsPermanent(MagicSubType.Swamp);
    }
};

[
     new MagicCardActivation(
        [SWAMP_AND_PLAINS_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Free"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [];
        }
    }
]
