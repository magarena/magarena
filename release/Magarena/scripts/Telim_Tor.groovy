[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent attacker) {
            return (permanent == attacker) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Attacking creatures with flanking get +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_ATTACKING_CREATURE);
            for (final MagicPermanent creature : targets) {
                if (creature.hasAbility(MagicAbility.Flanking)) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                }
            }
        }
    }
]
