[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent left) {
            return (permanent == left) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains life equal to the number of age counters on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge);
            game.doAction(new MagicChangeLifeAction(
                event.getPlayer(),
                amount
            ));
        }
    }
]
