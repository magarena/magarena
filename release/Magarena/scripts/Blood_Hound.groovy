[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(),
                    amount,
                    this,
                    "PN may\$ put RN +1/+1 counters on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,event.getRefInt()));
            }
        }
    },
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return (permanent.getCounters(MagicCounterType.PlusOne)>0 && permanent.isController(eotPlayer)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Remove all +1/+1 counters from SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,-event.getPermanent().getCounters(MagicCounterType.PlusOne)));
        }
    }
]
