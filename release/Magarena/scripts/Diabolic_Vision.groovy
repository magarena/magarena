def DrawOne = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
    });    
};

def PutBackRest = {
    final MagicGame prevGame, final MagicEvent prevEvent ->
    final MagicCardList putBackList = new MagicCardList();
    final int size = prevEvent.getPlayer().getLibrary().size();
    if (5 > size && size >= 2) {
        for(int i = 1; i <= size; i++) {
            putBackList.add(prevEvent.getPlayer().getLibrary().get(size-i));
        }
        prevGame.addEvent(new MagicEvent(
            prevEvent.getSource(),
            new MagicFromCardListChoice(putBackList,size),
            {
                final MagicGame game, final MagicEvent event ->
                event.processChosenCards(game, {
                    final MagicCard card ->
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
                });
            },
            ""
        ));           
    } else if (size > 4) {
        for(int i = 0; i < 4; i++) {
            putBackList.add(prevEvent.getPlayer().getLibrary().get(4-i));
        }
        prevGame.addEvent(new MagicEvent(
            prevEvent.getSource(),
            new MagicFromCardListChoice(putBackList,4),
            {
                final MagicGame game, final MagicEvent event ->
                event.processChosenCards(game, {
                    final MagicCard card ->
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
                });
            },
            ""
        ));
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN looks at the top seven cards of his or her library. Then PN puts two of them into your hand and the rest into his or her graveyard."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList choiceList = new MagicCardList();
            final int size = event.getPlayer().getLibrary().size();
            if (size == 1) game.doAction(new MagicDrawAction(event.getPlayer(),1));
            else if (6 > size && size >= 2) {
                for(int i = 1; i <= size; i++) {
                    choiceList.add(event.getPlayer().getLibrary().get(size-i));
                }
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList,1),
                    DrawOne,
                    "\$"
                ));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    PutBackRest,
                    ""
                ));
            } else if (size > 5) {
                for(int i = 0; i < 5; i++) {
                    choiceList.add(event.getPlayer().getLibrary().get(5-i));
                }
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList,1),
                    DrawOne,
                    "\$"
                ));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    PutBackRest,
                    ""
                ));
            } 
        }
    }
]
