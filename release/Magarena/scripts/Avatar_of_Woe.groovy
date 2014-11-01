def TEN_CREATURES_IN_GRAVEYARDS_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final int number = source.getGame().filterCards(source.getController(),MagicTargetFilterFactory.CREATURE_CARD_FROM_ALL_GRAVEYARDS).size();
        return number >= 10;
    }
};

[
     new MagicCardActivation(
        [TEN_CREATURES_IN_GRAVEYARDS_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "-Cost"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [new MagicPayManaCostEvent(source,"{B}{B}")];
        }
    }
]
