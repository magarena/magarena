def EventAction2 = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
    });
}

def EventAction1 = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
    });
    final MagicCardList choiceList = event.getPlayer().getLibrary().getCardsFromTop(4);
    game.addEvent(new MagicEvent(
        event.getSource(),
        new MagicFromCardListChoice(choiceList, choiceList.size()),
        EventAction2,
        ""
    ));
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN looks at the top five cards of his or her library. Then PN puts one of them into his or her hand and the rest on top of his or her library in any order."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList choiceList = event.getPlayer().getLibrary().getCardsFromTop(5);
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(choiceList, 1),
                EventAction1,
                ""
            )); 
        }
    }
]
