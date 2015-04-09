[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN returns all artifacts he or she controls to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            ARTIFACT_YOU_CONTROL.filter(event.getPlayer()) each {
                game.doAction(new MagicRemoveFromPlayAction(it, MagicLocationType.OwnersHand));
            }
        }
    }
]
