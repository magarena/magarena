def ISLAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
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
    new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Counter,true),
        "Alt"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicDiscardEvent(source),
                new MagicDiscardChosenEvent(source, AN_ISLAND_CARD_FROM_HAND)
            ];
        }
    }
]
