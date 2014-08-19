[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getNrOfPermanents(MagicType.Land) >= 5 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls five or more lands, put four +1/+1 counters on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(MagicType.Land) >= 5) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,4));
            }
        }
    }
]
