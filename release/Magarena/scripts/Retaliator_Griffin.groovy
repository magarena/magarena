[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getDealtAmount();
            return (amount > 0 &&
                    permanent.isController(damage.getTarget()) &&
                    permanent.isEnemy(damage.getSource())) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "Put RN +1/+1 counters on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPlayer(),
                event.getPermanent(),
                MagicCounterType.PlusOne,
                event.getRefInt()
            ));
        }
    }
]
