[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains control of all Enchantments."
            )
        }

       @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.ENCHANTMENT);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicGainControlAction(event.getPlayer(), target));
            }
        }
    }
]
