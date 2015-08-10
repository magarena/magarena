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
            game.doAction(new RemoveAllFromPlayAction(
                CREATURE.filter(event),
                MagicLocationType.OwnersHand
            ));
            CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(event) each {
                game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
