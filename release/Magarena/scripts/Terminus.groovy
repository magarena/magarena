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
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),CREATURE);
            for (final MagicPermanent permanent : targets) {
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.BottomOfOwnersLibrary));
            }
        }
    }
]
