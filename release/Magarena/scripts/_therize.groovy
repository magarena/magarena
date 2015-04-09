[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all attacking creatures to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            ATTACKING_CREATURE.filter(game) each {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            }
        }
    }
]
