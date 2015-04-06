[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            new MagicEvent(
                permanent,
                this,
                "PN untaps each other creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL.except(event.getPermanent())
            );
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicUntapAction(target));
            }
        }
    }
]
