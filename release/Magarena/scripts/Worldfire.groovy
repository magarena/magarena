[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Exile all permanents. Exile all cards from all hands and graveyards. Each player's life total becomes 1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            PERMANENT.filter(event) each {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard cardHand : hand) {
                    game.doAction(new RemoveCardAction(cardHand,MagicLocationType.OwnersHand));
                    game.doAction(new MoveCardAction(cardHand,MagicLocationType.OwnersHand,MagicLocationType.Exile));
                }
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                for (final MagicCard cardGraveyard : graveyard) {
                    game.doAction(new RemoveCardAction(cardGraveyard,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(cardGraveyard,MagicLocationType.Graveyard,MagicLocationType.Exile));
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final int changeLife = 1 - player.getLife();
                game.doAction(new ChangeLifeAction(player,changeLife));
            }
        }
    }
]
