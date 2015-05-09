[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player returns all creature cards from his or her graveyard to his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                CREATURE_CARD_FROM_GRAVEYARD.filter(player) each {
                    game.doAction(new RemoveCardAction(it,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
            }
        }
    }
]
