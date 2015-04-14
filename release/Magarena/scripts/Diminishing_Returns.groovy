[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player shuffles his or her hand and graveyard into his or her library. "+
                "PN exiles the top ten cards of his or her library. Then each player draws up to seven cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard card : graveyard) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
                }
                for (final MagicCard card : hand) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                    game.doAction(new MoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.OwnersLibrary));
                }
            }
            final MagicCardList exile = new MagicCardList(event.getPlayer().getLibrary().getCardsFromTop(10));
            for (final MagicCard card : exile) {
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DrawAction(player,7));
            }
        }
    }
]
