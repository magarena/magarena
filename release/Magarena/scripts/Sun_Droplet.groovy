[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return permanent.isController(damage.getTarget()) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "Put RN charge counters on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,event.getRefInt()));
        }
    },
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.getCounters(MagicCounterType.Charge)>0) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(),
                    this,
                    "PN may\$ remove a charge counter from SN. If PN does, PN gains 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().getCounters(MagicCounterType.Charge)>=1 && event.isYes()) {
                game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,-1));
                game.doAction(new ChangeLifeAction(event.getPlayer(),1));
            }
        }
    }
]
