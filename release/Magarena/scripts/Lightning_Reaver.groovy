[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a charge counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1,true));
        }
    },
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return (permanent.isController(eotPlayer) &&
                    permanent.getCounters(MagicCounterType.Charge) > 0) ?
                new MagicEvent(
                    permanent,
                    permanent.getOpponent(),
                    this,
                    "SN deals damage equal to the number of charge counters on it to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final int counters=permanent.getCounters(MagicCounterType.Charge);
            final MagicDamage damage=new MagicDamage(permanent,event.getPlayer(),counters);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
