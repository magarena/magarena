def TWO_OTHER_GREEN_CARDS_IN_HAND = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicTargetFilter<MagicCard> filter =new MagicOtherCardTargetFilter(
            MagicTargetFilterFactory.GREEN_CARD_FROM_HAND, 
            (MagicCard)source
        );
        final MagicGame game = source.getGame();
        final MagicPlayer player = source.getController();
        return game.filterCards(player, filter).size() >= 2;
    }
};
    
[
    new MagicCardActivation(
        [TWO_OTHER_GREEN_CARDS_IN_HAND, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Alt"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherCardTargetFilter(
                    MagicTargetFilterFactory.GREEN_CARD_FROM_HAND, 
                    source
                ),
                MagicTargetHint.None,
                "a green card from your hand"
            );
            return [
                new MagicExileCardEvent(source, targetChoice),
                new MagicExileCardEvent(source, targetChoice)
            ];
        }
    },
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int size = player.getNrOfPermanents(MagicType.Land);
            pt.set(1 + size, 1 + size);
        }
    }
]
