[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player shuffles his or her hand and graveyard into his or her library, then draws seven cards. "+
                "Exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard card : graveyard) {
                    game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
                }
                for (final MagicCard card : hand) {
                    game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersHand));
                    game.doAction(new MoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.OwnersLibrary));
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DrawAction(player,7));
            }
            game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.Exile));
        }
    }
]
