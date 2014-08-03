def trigger = {
    final MagicTargetFilter filter ->
    return new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer) && upkeepPlayer.controlsPermanent(filter)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1));
            }
        }
    };
}
[
    trigger(MagicTargetFilterFactory.BLACK_PERMANENT_YOU_CONTROL),
    trigger(MagicTargetFilterFactory.GREEN_PERMANENT_YOU_CONTROL)
]
