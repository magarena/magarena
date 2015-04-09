[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all green permanents to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> permanents = game.filterPermanents(GREEN_PERMANENT);
            for (final MagicPermanent permanent : permanents) {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
            }
        }
    }
]
