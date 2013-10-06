[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all creatures."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicRemoveFromPlayAction(
                    target,
                    MagicLocationType.Exile
                ));
            }
        }
    }
]
