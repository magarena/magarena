[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Other attacking creatures get +1/+1 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> attackers = game.filterPermanents(
                MagicTargetFilterFactory.ATTACKING_CREATURE.except(event.getPermanent())
            );
            for (final MagicPermanent creature:attackers) {
                game.doAction(new MagicChangeTurnPTAction(creature,1,1));
            }
        }
    }
]
