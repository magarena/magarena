def DrawTwo = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
    });    
};

def DiscardRest = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    final int amount = (player.getLibrary().size() < 6) ? player.getLibrary().size() : 5;
    for(int i = 0; i < amount; i++) {
        game.doAction(new MagicMillLibraryAction(player,1));
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
            else if (size == 2) game.doAction(new MagicDrawAction(event.getPlayer(),2)); //truth to be told: "index out of bounds" is something to be hated naturally
            else if (8 > size && size > 2) {
                for(int i = 1; i <= size; i++) {
                    choiceList.add(event.getPlayer().getLibrary().get(size-i));
                }
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList,2),
                    DrawTwo,
                    "\$"
                ));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    DiscardRest,
                    ""
                ));
            } else if (size > 7) {
                for(int i = 0; i < 7; i++) {
                    choiceList.add(event.getPlayer().getLibrary().get(7-i));
                }
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList,2),
                    DrawTwo,
                    "\$"
                ));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    DiscardRest,
                    ""
                ));
            } 
        }
    }
]
