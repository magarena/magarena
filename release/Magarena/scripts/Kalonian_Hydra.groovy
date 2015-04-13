[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Double the number of +1/+1 counters on each creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    CREATURE_YOU_CONTROL
                );
            for (final MagicPermanent creature : targets) {
                game.doAction(new ChangeCountersAction(creature,MagicCounterType.PlusOne,creature.getCounters(MagicCounterType.PlusOne)));
            }
        }
    }
]
