[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer) &&
                    upkeepPlayer.getNrOfPermanents(MagicType.Land) >= 5) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put four +1/+1 counters on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,4));
        }
    }
]
