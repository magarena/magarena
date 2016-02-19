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
                for (final MagicCard card : graveyard) {
                    game.doAction(new ShiftCardAction(card,MagicLocationType.Graveyard,MagicLocationType.TopOfOwnersLibrary));
                }
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard card : hand) {
                    game.doAction(new ShiftCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.TopOfOwnersLibrary));
                }
                game.doAction(new ShuffleLibraryAction(player));
            }
            final MagicCardList exile = new MagicCardList(event.getPlayer().getLibrary().getCardsFromTop(10));
            for (final MagicCard card : exile) {
                game.doAction(new ShiftCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                //FIXME Requires a choice of how many cards to draw - as X dialog with max 7 but min 0
                game.doAction(new DrawAction(player,7));
            }
        }
    }
]
