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
            final Collection<MagicPermanent> attackers = ATTACKING_CREATURE.except(event.getPermanent()).filter(event);
            final int amount = attackers.size()
            for (final MagicPermanent creature:attackers) {
                game.doAction(new ChangeTurnPTAction(creature,amount,amount));
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
            final Collection<MagicPermanent> blockers = BLOCKING_CREATURE.except(event.getPermanent()).filter(event);
            final int amount = blockers.size()
            for (final MagicPermanent creature:blockers) {
                game.doAction(new ChangeTurnPTAction(creature,amount,amount));
            }
        }
    }
]
