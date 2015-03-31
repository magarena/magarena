[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final int counters = permanent.getCounters(MagicCounterType.Charge);
            return (permanent.isController(eotPlayer) &&
                    counters > 0) ?
                new MagicEvent(
                    permanent,
                    permanent.getOpponent(),
                    this,
                    "SN deals damage equal to the number of charge counters on it to PN. ("+counters+")"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final int amount=permanent.getCounters(MagicCounterType.Charge);
            game.doAction(new MagicDealDamageAction(permanent,event.getPlayer(),amount));
        }
    }
]
