[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Other attacking creatures get +1/+1 until end of turn for each attacking creature other than SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> attackers = game.filterPermanents(MagicTargetFilterFactory.ATTACKING_CREATURE.except(event.getPermanent()));
            final int amount = attackers.size()
            for (final MagicPermanent creature:attackers) {
                game.doAction(new MagicChangeTurnPTAction(creature,amount,amount));
            }
        }
    },
    
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Other blocking creatures get +1/+1 until end of turn for each blocking creature other than SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> blockers = game.filterPermanents(MagicTargetFilterFactory.BLOCKING_CREATURE.except(event.getPermanent()));
            final int amount = blockers.size()
            for (final MagicPermanent creature:blockers) {
                game.doAction(new MagicChangeTurnPTAction(creature,amount,amount));
            }
        }
    }
]
