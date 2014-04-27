def EventAction = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.BottomOfOwnersLibrary));
    });
    game.doAction(new MagicDrawAction(event.getPlayer(),event.getRefInt()));
};

[
    new MagicAtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer drawPlayer) {
            return new MagicEvent(
                permanent,
                drawPlayer,
                this,
                "PN puts the cards in his or her hand on the bottom of his or her library in any order, then draws that many cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final List<MagicCard> choiceList = event.getPlayer().getHand();
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicFromCardListChoice(choiceList, choiceList.size()),
                choiceList.size(),
                EventAction,
                ""
            )); 
        }
    }
]
