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
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new ChangeCountersAction(
                    it,
                    MagicCounterType.PlusOne,
                    it.getCounters(MagicCounterType.PlusOne)
                ));
            }
        }
    }
]
