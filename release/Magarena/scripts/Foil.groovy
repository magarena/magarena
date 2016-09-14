def ISLAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Island);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};
def AN_ISLAND_CARD_FROM_HAND = new MagicTargetChoice(
    ISLAND_CARD_FROM_HAND,
    MagicTargetHint.None,
    "an Island card from your hand"
);

[
    new MagicHandCastActivation(
        [
            MagicConditionFactory.HandAtLeast(3),
            MagicCondition.CARD_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Counter,true),
        "Alt"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            final MagicTargetChoice ANOTHER_CARD_FROM_HAND = new MagicTargetChoice(
                CARD_FROM_HAND.except(source),
                MagicTargetHint.None,
                "another card from your hand"
            );
            return [
                new MagicDiscardChosenEvent(source, AN_ISLAND_CARD_FROM_HAND),
                new MagicDiscardChosenEvent(source, ANOTHER_CARD_FROM_HAND)
            ];
        }
    }
]
