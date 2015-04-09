[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all creatures PN controls to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicRemoveFromPlayAction(target,MagicLocationType.OwnersHand));
            }
        }
    }
]
