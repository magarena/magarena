def LAND_CARD_IN_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Land);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }

};

def A_LAND_CARD_IN_HAND = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicTargetFilter<MagicCard> filter =new MagicOtherCardTargetFilter(
            LAND_CARD_IN_HAND, 
            (MagicCard)source
        );
        final MagicGame game = source.getGame();
        final MagicPlayer player = source.getController();
        return game.filterCards(player, filter).size() >= 1;
    }
};

def SOURCE_IN_GRAVEYARD = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicCard sourceCard = (MagicCard) source;
        return sourceCard.isInGraveyard();
    }
};

[
    new MagicCardActivation(
        [A_LAND_CARD_IN_HAND, SOURCE_IN_GRAVEYARD, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Retrace"
    )  {
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherCardTargetFilter(
                    LAND_CARD_IN_HAND, 
                    source
                ),
                MagicTargetHint.None,
                "a land card from your hand"
            );
            return [
                new MagicDiscardChosenEvent(source, targetChoice),
                new MagicPayManaCostEvent(source, source.getCost())
            ];
        }
    }
]
