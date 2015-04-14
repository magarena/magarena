[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all creatures on the battlefield and all creature cards in graveyards to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(game) each {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            }
            CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(game) each {
                game.doAction(new RemoveCardAction(it,MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
