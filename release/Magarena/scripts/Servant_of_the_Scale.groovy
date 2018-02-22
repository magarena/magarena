[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_YOU_CONTROL,
                permanent.getCounters(MagicCounterType.PlusOne),
                this,
                "PN puts RN +1/+1 counters on target creature PN controls \$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeCountersAction(it, MagicCounterType.PlusOne, event.getRefInt()));
            });
        }
    }
]

