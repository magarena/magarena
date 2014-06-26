[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Attacking red creatures get +2/+0 and attacking white creatures get +0/+2 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> attackers = game.filterPermanents(MagicTargetFilterFactory.ATTACKING_CREATURE);
            for (final MagicPermanent creature : attackers) {
                if (creature.hasColor(MagicColor.Red)) {
                    game.doAction(new MagicChangeTurnPTAction(creature,2,0));
                }
                if (creature.hasColor(MagicColor.White)) {
                    game.doAction(new MagicChangeTurnPTAction(creature,0,2));
                }
            }
        }
    }
]
