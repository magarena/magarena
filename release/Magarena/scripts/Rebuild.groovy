[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all artifacts to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            ARTIFACT.filter(game) each {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            }
        }
    }
]
