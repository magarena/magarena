[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Exile all artifacts, creatures, and lands from the battlefield, all cards from all graveyards, and all cards from all hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            ARTIFACT.filter(event) each {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
            }
            CREATURE.filter(event) each {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
            }
            LAND.filter(event) each {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                for (final MagicCard cardGraveyard : graveyard) {
                    game.doAction(new RemoveCardAction(cardGraveyard,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(cardGraveyard,MagicLocationType.Graveyard,MagicLocationType.Exile));
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard cardHand : hand) {
                    game.doAction(new RemoveCardAction(cardHand,MagicLocationType.OwnersHand));
                    game.doAction(new MoveCardAction(cardHand,MagicLocationType.OwnersHand,MagicLocationType.Exile));
                }
            }
        }
    }
]
