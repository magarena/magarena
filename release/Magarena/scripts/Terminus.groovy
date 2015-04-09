[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put all creatures on the bottom of their owners' libraries."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(game) each {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.BottomOfOwnersLibrary));
            }
        }
    }
]
