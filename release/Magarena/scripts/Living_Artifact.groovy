[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return permanent.isController(damage.getTarget()) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "Put RN vitality counters on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.Vitality,event.getRefInt()));
        }
    }
]
