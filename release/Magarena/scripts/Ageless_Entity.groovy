[
    new LifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            final int amount = lifeChange.amount;
            return permanent.isController(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    amount > 1 ?
                        "Put " + amount + " +1/+1 counters on SN." :
                        "Put a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.PlusOne,event.getRefInt()));
        }
    }
]
